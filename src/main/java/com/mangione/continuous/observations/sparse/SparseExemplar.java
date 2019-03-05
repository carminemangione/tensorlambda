package com.mangione.continuous.observations.sparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SparseExemplar<T> extends SparseObservation<T> implements SparseExemplarInterface<T, T> {
	private final T target;
	private final int targetIndex;

	public SparseExemplar(T[] values, int[] columns, int numberOfColumns, T missingValue, int targetIndex) {
		super(createAndFillIndexToValueMapRemovingTarget(columns, values, targetIndex),
				numberOfColumns- 1, missingValue);
		if (numberOfColumns < 1) {
			throw new IllegalArgumentException("Number of columns must be greater than 0");
		}


		if (targetIndex < 0)
			throw new IllegalArgumentException("Column index may not be negative");

		this.targetIndex = targetIndex;
		int locationOfTargetIndex = Arrays.binarySearch(columns, targetIndex);
		if (locationOfTargetIndex < 0)
			throw new IllegalArgumentException(String.format("Target index %d must be set in values array", targetIndex));

		this.target = values[locationOfTargetIndex];
	}

	@SuppressWarnings("WeakerAccess")
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

	@Override
	public List<Integer> getColumnIndexes() {
		return super.getColumnIndexes();
	}

	private static <T> Map<Integer, T> createAndFillIndexToValueMapRemovingTarget(int[] columns, T[] values, int targetIndex) {
		Map<Integer, T> indexToValueMap = new HashMap<>(columns.length);

		fillUpToColumnIndex(columns, values, targetIndex, indexToValueMap);
		fillAfterColumnIndexSlidingToLeft(columns, values, targetIndex, indexToValueMap);
		return indexToValueMap;
	}

	private static <T> void fillAfterColumnIndexSlidingToLeft(int[] columns, T[] values, int targetIndex, Map<Integer, T> indexToValueMap) {
		IntStream.range(0, columns.length)
				.boxed()
				.filter(index -> columns[index] > targetIndex)
				.forEach(index -> indexToValueMap.put(columns[index] - 1, values[index]));
	}

	private static <T> void fillUpToColumnIndex(int[] columns, T[] values, int targetIndex, Map<Integer, T> indexToValueMap) {
		if (columns.length != values.length)
			throw new IllegalArgumentException(String.format("Number of columns %d does not equal number of values %d", columns.length, values.length));
		IntStream.range(0, columns.length)
				.boxed()
				.filter(index -> columns[index] < targetIndex)
				.forEach(index -> indexToValueMap.put(columns[index], values[index]));
	}

}
