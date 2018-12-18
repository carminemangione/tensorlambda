package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparseObservation<T> implements ObservationInterface<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparseObservation.class);
	private final T[] values;
	private final int[] columns;
	private final int numberOfColumns;

	public SparseObservation(T[] values, int[] columns, int numberOfColumns) {
		if (values.length != columns.length)
			throw new IllegalArgumentException("Length of values must equal number of columns");
		this.values = values;
		this.columns = columns;
		this.numberOfColumns = numberOfColumns;
	}

	@Override
	public List<T> getFeatures() {
		List<T> features = new ArrayList<>(generateNullFeatures());
		IntStream.range(0, columns.length)
				.filter(i -> columns[i] < numberOfColumns)
				.forEach(i -> features.set(columns[i], values[i]));
		return features;
	}

	@Override
	public List<T> getAllColumns() {
		return getFeatures();
	}

	@Override
	public T getFeature(int index) {
		int columnIndex = Arrays.binarySearch(columns, index);
		return columnIndex >= 0 ? values[columnIndex] : null;
	}

	private List<T> generateNullFeatures() {
		return Stream.generate(() -> (T)null)
				.limit(numberOfColumns)
				.collect(Collectors.toList());
	}
}
