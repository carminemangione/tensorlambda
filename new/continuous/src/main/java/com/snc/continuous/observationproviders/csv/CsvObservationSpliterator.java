package com.mangione.continuous.observationproviders.csv;

import java.io.BufferedReader;
import java.io.File;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

class CsvObservationSpliterator<T> extends CsvObservationSpliteratorShared implements Spliterator<T> {

	private final File file;
	private final String splitVal;
	private final Function<String[], T> factory;
	private final boolean hasColumnHeader;
	private final CsvObservationIterator<T> iterator;
	private final int minimumBatchSize;
	private Function<File, BufferedReader> bufferedFunction;
	private int current;
	private int high;
	private boolean openEnded;

	CsvObservationSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int estimatedSize, int minimumBatchSize, Function<File, BufferedReader> bufferedFunction) {
		this(file, splitVal, factory, hasColumnHeader, 0, estimatedSize, true, minimumBatchSize,
				bufferedFunction);
	}

	private CsvObservationSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int low, int high, boolean openEnded, int minimumBatchSize, Function<File, BufferedReader> bufferedFunction) {

		this.file = file;
		this.splitVal = splitVal;
		this.factory = factory;
		this.hasColumnHeader = hasColumnHeader;
		this.current = low;
		this.high = high;
		this.openEnded = openEnded;
		this.minimumBatchSize = minimumBatchSize;
		this.bufferedFunction = bufferedFunction;

		iterator = new CsvObservationIterator<>(file, splitVal, factory, hasColumnHeader, bufferedFunction);
		for (int i = 0; i < low && iterator.hasNext(); i++) {
			iterator.next();
		}
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		boolean tryAdvance = (openEnded || current < high) && iterator.hasNext();
		if (tryAdvance) {
			current++;
			action.accept(iterator.next());
		} else
			closeResources(iterator);
		return tryAdvance;
	}

	@Override
	public Spliterator<T> trySplit() {
		CsvObservationSpliterator<T> split = null;
		int remaining = high - current;
		if (remaining > minimumBatchSize) {
			int half = remaining / 2;
			int mid = current + half;
			int rest = high;
			high = mid;
			split = new CsvObservationSpliterator<>(file, splitVal, factory, hasColumnHeader, mid, rest,
					openEnded, minimumBatchSize, bufferedFunction);
			openEnded = false;
		}
		return split;
	}

	@Override
	public long estimateSize() {
		return high - current;
	}

	@Override
	public int characteristics() {
		return IMMUTABLE;
	}

	@Override
	public void forEachRemaining(Consumer<? super T> action) {
		while (true) {
			if (!tryAdvance(action)) break;
		}
	}

}
