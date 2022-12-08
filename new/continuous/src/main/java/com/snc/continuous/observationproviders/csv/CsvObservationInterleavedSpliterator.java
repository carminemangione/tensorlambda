package com.mangione.continuous.observationproviders.csv;

import java.io.BufferedReader;
import java.io.File;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

class CsvObservationInterleavedSpliterator<T> extends CsvObservationSpliteratorShared implements Spliterator<T> {
	private final CsvObservationIterator<T> iterator;
	private final int minimumBatchSize;
	private int currentBatchSize;

	CsvObservationInterleavedSpliterator(File file, Function<String[], T> factory, boolean hasColumnHeader,
			int estimatedSize, int minimumBatchSize, Function<File, BufferedReader> fileReaderFunction, String splitVal) {
		this(estimatedSize, minimumBatchSize,
				new CsvObservationIterator<>(file, splitVal, factory, hasColumnHeader, fileReaderFunction));
	}

	private CsvObservationInterleavedSpliterator(int currentBatchSize, int minimumBatchSize, CsvObservationIterator<T> iterator) {
		this.currentBatchSize = currentBatchSize;
		this.minimumBatchSize = minimumBatchSize;
		this.iterator = iterator;
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		boolean tryAdvance = false;
		T nextValue = iterator.getNext();
		if (nextValue != null) {
			tryAdvance = true;
			action.accept(nextValue);
		}
		return tryAdvance;
	}

	@Override
	public Spliterator<T> trySplit() {
		CsvObservationInterleavedSpliterator<T> split = null;
		if (currentBatchSize > minimumBatchSize) {
			int splitBatchSize = currentBatchSize / 2;
			split = new CsvObservationInterleavedSpliterator<>(currentBatchSize - splitBatchSize,
					minimumBatchSize, iterator);
			currentBatchSize = splitBatchSize;
			System.out.println("current split size: " + currentBatchSize);
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

}

