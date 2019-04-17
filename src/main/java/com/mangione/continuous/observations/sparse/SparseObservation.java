package com.mangione.continuous.observations.sparse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
public class SparseObservation<T> implements SparseObservationInterface<T> {

	private final Map<Integer, T> columnIndexToValueMap;
	private final int numberOfColumns;
	private final T missingValue;

	public SparseObservation(T[] values, int[] columns, int numberOfColumns, T missingValue) {
		this(createAndFillIndexToValueMap(columns, values), numberOfColumns, missingValue);
	}

	public SparseObservation(int numberOfColumns, T missingValue) {
		columnIndexToValueMap = new HashMap<>();
		this.numberOfColumns = numberOfColumns;
		this.missingValue = missingValue;
	}

	protected SparseObservation(Map<Integer, T> columnIndexToValueMap, int numberOfColumns, T missingValue) {
		this.columnIndexToValueMap = columnIndexToValueMap;
		this.numberOfColumns = numberOfColumns;
		this.missingValue = missingValue;
	}

	public Set<Integer> getColumnIndexes() {
		return Collections.unmodifiableSet(columnIndexToValueMap.keySet());
	}

	@Override
	public List<T> getFeatures() {
		List<T> features = new ArrayList<>(generateListWithMissingValues());
		columnIndexToValueMap.forEach(features::set);
		return Collections.unmodifiableList(features);
	}

	public List<T> getFeatureValues() {
		return Collections.unmodifiableList(new ArrayList<>(columnIndexToValueMap.values()));
	}

	@Override
	public List<T> getAllColumns() {
		return getFeatures();
	}


	@Override
	public T getFeature(int index) {
		validateIndex(index);
		return columnIndexToValueMap.getOrDefault(index, missingValue);
	}

	@Override
	public void setFeature(int index, T value) {
		validateIndex(index);
		columnIndexToValueMap.put(index, value);
	}

	@Override
	public int numberOfFeatures() {
		return numberOfColumns;
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

	private static <T> Map<Integer, T> createAndFillIndexToValueMap(int[] columns, T[] values) {
		if (values.length != columns.length) {
			throw new IllegalArgumentException("Length of values must equal number of columns");
		}

		Map<Integer, T> indexToValueMap = new TreeMap<>();

		IntStream.range(0, columns.length)
				.boxed()
				.forEach(index -> indexToValueMap.put(columns[index], values[index]));

		return indexToValueMap;
	}
}
