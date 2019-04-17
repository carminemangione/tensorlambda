package com.mangione.continuous.observationproviders;

import java.io.File;
import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

class CsvObservationSpliterator<T> implements Spliterator<T> {

	private final File file;
	private final String splitVal;
	private final Function<String[], T> factory;
	private final boolean hasColumnHeader;
	private final CsvObservationIterator<T> iterator;
	private final int minimumBatchSize;
	private int current;
	private int high;
	private boolean openEnded;

	CsvObservationSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int estimatedSize, int minimumBatchSize) {
		this(file, splitVal, factory, hasColumnHeader, 0, estimatedSize, true, minimumBatchSize);
	}

	private CsvObservationSpliterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
			int low, int high, boolean openEnded, int minimumBatchSize) {

		this.file = file;
		this.splitVal = splitVal;
		this.factory = factory;
		this.hasColumnHeader = hasColumnHeader;
		this.current = low;
		this.high = high;
		this.openEnded = openEnded;
		this.minimumBatchSize = minimumBatchSize;

		iterator = new CsvObservationIterator<>(file, splitVal, factory, hasColumnHeader);
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
		}
		if (!tryAdvance) {
			try {
				iterator.closeReader();
			} catch (IOException e) {
				throw new ProviderException(e);
			}
		}
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
					openEnded, minimumBatchSize);
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
