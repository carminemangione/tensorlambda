package com.mangione.continuous.observationproviders.csv;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

class CsvObservationInterleavedSpliterator<T> extends CsvObservationSpliteratorShared implements Spliterator<T> {

	private final File file;
	private final String splitVal;
	private final Function<String[], T> factory;
	private final boolean hasColumnHeader;
	private final CsvObservationIterator<T> iterator;
	private final int minimumBatchSize;
	private final Function<File, BufferedReader> bufferedFunction;
	private final int maximumNumberOfPartitions;

	private final int numberOfElements;
	private final AtomicBoolean endOfFileEncountered;
	private final AtomicInteger numberOfPartitions;

	private List<T> elementsRemaining;
	private boolean openEnded;
	private int partitionOffset;
	private boolean firstPartition = true;


	CsvObservationInterleavedSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int estimatedSize, int minimumBatchSize, Function<File, BufferedReader> bufferedFunction) {
		this(file, splitVal, factory, hasColumnHeader, estimatedSize, true, minimumBatchSize,
				bufferedFunction, estimatedSize / minimumBatchSize, new AtomicBoolean(),
				0, new AtomicInteger());
	}

	private CsvObservationInterleavedSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int numberOfElements, boolean openEnded, int minimumBatchSize, Function<File, BufferedReader> bufferedFunction,
			int maximumNumberOfPartitions, AtomicBoolean endOfFileEncountered, int partitionOffset, AtomicInteger numberOfPartitions) {

		this.file = file;
		this.splitVal = splitVal;
		this.factory = factory;
		this.numberOfElements = numberOfElements;
		this.openEnded = openEnded;
		this.minimumBatchSize = minimumBatchSize;
		this.bufferedFunction = bufferedFunction;
		this.endOfFileEncountered = endOfFileEncountered;
		this.partitionOffset = partitionOffset;
		this.hasColumnHeader = hasColumnHeader;
		this.maximumNumberOfPartitions = maximumNumberOfPartitions;
		this.numberOfPartitions = numberOfPartitions;

		this.iterator = createIteratorAndFastForwardToOffset(file, splitVal, factory, hasColumnHeader, bufferedFunction);
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		boolean tryAdvance = false;

		if (elementsRemaining == null) {
			if (openEnded && iterator.hasNext()) {
				tryAdvance = true;
				action.accept(iterator.next());
			} else {
				tryAdvance = findNextPartitionElementAndAccept(action);

			}
		} else if (elementsRemaining.size() > 0) {
			tryAdvance = true;
			action.accept(elementsRemaining.remove(0));
		}

		if (!tryAdvance)
			closeResources(iterator);
		firstPartition = false;

		return tryAdvance;

	}

	private boolean findNextPartitionElementAndAccept(Consumer<? super T> action) {
		boolean accepted = false;

		List<T> nextPartition = skipPartitionSizeToFindNextElementIfNotZerothPartition();
		if (nextPartition.size() == numberOfElements) {
			action.accept(nextPartition.get(nextPartition.size() - 1));
			accepted = true;
		} else {
			if (endOfFileEncountered.compareAndSet(false, true) && nextPartition.size() > 0) {
				elementsRemaining = nextPartition;
				action.accept(nextPartition.remove(0));
				accepted = true;
			}
		}

		return accepted;
	}

	@Override
	public Spliterator<T> trySplit() {
		CsvObservationInterleavedSpliterator<T> split = null;
		if (numberOfPartitions.incrementAndGet() <= maximumNumberOfPartitions - 1) {
			split = new CsvObservationInterleavedSpliterator<>(file, splitVal, factory, hasColumnHeader, numberOfElements,
					partitionOffset == maximumNumberOfPartitions - 1,
					minimumBatchSize, bufferedFunction, maximumNumberOfPartitions, endOfFileEncountered,
					numberOfPartitions.get(), numberOfPartitions);
		}
		return split;
	}

	@Override
	public long estimateSize() {
		return minimumBatchSize;
	}

	@Override
	public int characteristics() {
		return IMMUTABLE;
	}

	@Override
	public void forEachRemaining(Consumer<? super T> action) {
		while (true) {
			if (!tryAdvance(action))
				break;
		}
	}

	@NotNull
	private CsvObservationIterator<T> createIteratorAndFastForwardToOffset(File file, String splitVal,
			Function<String[], T> factory, boolean hasColumnHeader, Function<File, BufferedReader> bufferedFunction) {
		CsvObservationIterator<T> iterator = new CsvObservationIterator<>(file, splitVal, factory, hasColumnHeader, bufferedFunction);
		for (int i = 0; i < this.partitionOffset && iterator.hasNext(); i++) {
			iterator.next();
		}
		if (!iterator.hasNext())
			throw new IllegalStateException("Initial guess at size is way too big...");
		return iterator;
	}

	private List<T> skipPartitionSizeToFindNextElementIfNotZerothPartition() {
		List<T> elements = new ArrayList<>();
		if (!firstPartition) {
			for (int i = 0; i < minimumBatchSize && iterator.hasNext(); i++) {
				elements.add(iterator.next());
			}
		}
		return elements;
	}

}

