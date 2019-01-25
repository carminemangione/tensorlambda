package com.mangione.continuous.observations.sparse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SparseObservation<T> implements SparseObservationInterface<T> {

	private final Map<Integer, T> columnIndexToValueMap;
	private final int numberOfColumns;
	private final T missingValue;

	@SuppressWarnings("WeakerAccess")
	public SparseObservation(T[] values, int[] columns, int numberOfColumns, T missingValue) {
		this(createAndFillIndexToValueMap(columns, values), numberOfColumns, missingValue);
	}

	@SuppressWarnings("WeakerAccess")
	public SparseObservation(int numberOfColumns, T missingValue) {
		columnIndexToValueMap = new HashMap<>();
		this.numberOfColumns = numberOfColumns;
		this.missingValue = missingValue;
	}

	SparseObservation(Map<Integer, T> columnIndexToValueMap, int numberOfColumns, T missingValue) {
		this.columnIndexToValueMap = columnIndexToValueMap;
		this.numberOfColumns = numberOfColumns;
		this.missingValue = missingValue;
	}

	@Override
	public List<T> getFeatures() {
		List<T> features = new ArrayList<>(generateListWithMissingValues());
		columnIndexToValueMap.forEach(features::set);
		return Collections.unmodifiableList(features);
	}

	@Override
	public List<T> getAllColumns() {
		return getFeatures();
	}


	@Override
	public T getValueAt(int index) {
		validateIndex(index);
		return columnIndexToValueMap.getOrDefault(index, missingValue);
	}

	@Override
	public void setValueAt(int index, T value) {
		validateIndex(index);
		columnIndexToValueMap.put(index, value);
	}

	private void validateIndex(int index) {
		if (index < 0)
			throw new IllegalArgumentException("Index may not be negative");
		if (index >= numberOfColumns)
			throw new IllegalArgumentException(String.format("Index of value %d exceeds maximum of %d", index, numberOfColumns - 1));
	}

	private List<T> generateListWithMissingValues() {
		return Stream.generate(() -> missingValue)
				.limit(numberOfColumns)
				.collect(Collectors.toList());
	}

	public List<Integer> getColumnIndexes() {
		return Collections.unmodifiableList(new ArrayList<>(columnIndexToValueMap.keySet()));
	}

	private static <T> Map<Integer, T> createAndFillIndexToValueMap(int[] columns, T[] values) {
		if (values.length != columns.length) {
			throw new IllegalArgumentException("Length of values must equal number of columns");
		}

		Map<Integer, T> indexToValueMap = new HashMap<>(columns.length);

		IntStream.range(0, columns.length)
				.boxed()
				.forEach(index -> indexToValueMap.put(columns[index], values[index]));

		return indexToValueMap;
	}
}
