package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observations.ExemplarInterface;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("WeakerAccess")
public class SparseExemplar<T> extends SparseObservation<T> implements ExemplarInterface<T, T> {
	private final T target;
	private final int targetIndex;

	public SparseExemplar(List<T> values, List<Integer> columns, int numberOfColumns, T missingValue, int targetIndex) {
		super(createAndFillIndexToValueMapRemovingTarget(values, columns, targetIndex),
				numberOfColumns - 1, missingValue);
		if (numberOfColumns < 1) {
			throw new IllegalArgumentException("Number of columns must be greater than 0");
		}

		if (targetIndex < 0)
			throw new IllegalArgumentException("Column index may not be negative");

		this.targetIndex = targetIndex;
		int locationOfTargetIndex = Collections.binarySearch(columns, targetIndex);
		if (locationOfTargetIndex < 0)
			throw new IllegalArgumentException(String.format("Target index %d must be set in values array", targetIndex));

		this.target = values.get(locationOfTargetIndex);
	}

	public SparseExemplar(T[] values, Integer[] columns, int numberOfColumns, T missingValue, int targetIndex) {
		this(Arrays.asList(values), Arrays.asList(columns), numberOfColumns, missingValue, targetIndex);
	}

	public SparseExemplar(int numberOfColumns, T missingValue, T target) {
		super(numberOfColumns, missingValue);
		this.target = target;
		this.targetIndex = numberOfColumns;
	}

	@Override
	public T getTarget() {
		return target;
	}

	@Override
	public int getTargetIndex() {
		return targetIndex;
	}

	@Override
	public List<T> getAllColumns() {
		List<T> features = new ArrayList<>(getFeatures());
		features.add(targetIndex, getTarget());
		return features;
	}

	@Override
	public T getFeature(int index) {
		if (index == targetIndex)
			throw new IllegalArgumentException("May not return value at target index");
		int adjustedIndexForRemovalOfTarget = index > targetIndex ? index - 1 : index;
		return super.getFeature(adjustedIndexForRemovalOfTarget);
	}

	private static <T> Map<Integer, T> createAndFillIndexToValueMapRemovingTarget(List<T> values, List<Integer> columns, int targetIndex) {
		TreeMap<Integer, T> treeMap = IntStream.range(0, columns.size())
				.boxed()
				.distinct()
				.collect(Collectors.toMap(columns::get, values::get, throwingMerger(), TreeMap::new));

		treeMap.remove(targetIndex);
		return treeMap;
	}

	private static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}

}
