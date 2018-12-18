package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.List;

public class DiscreteExemplar<T extends Number> implements ExemplarInterface<T, Integer> {
	private final List<T> features;
	private final T continuousValue;
	private final Integer target;
	private final List<T> allColumns;
	private final int targetColumnIndex;

	public static <F extends Number> DiscreteExemplar<F> getExemplarTargetLastColumn(List<F> features) {
		return new DiscreteExemplar<>(features);
	}

	public static <F extends Number> DiscreteExemplar<F> getExemplarTargetWithColumn(List<F> allColumns, int targetColumnIndex) {
		return new DiscreteExemplar<>(allColumns, targetColumnIndex);
	}

	public DiscreteExemplar(List<T> features, T continuousValue, int target) {
		this.allColumns = new ArrayList<>(features);
		this.allColumns.add(continuousValue);
		this.features = features;
		this.continuousValue = continuousValue;
		this.target = target;
		targetColumnIndex = features.size();
	}

	private DiscreteExemplar(List<T> allColumns) {
		this(allColumns, allColumns.size() - 1);
	}

	private DiscreteExemplar(List<T> allColumns, int targetColumnIndex) {
		if (targetColumnIndex < 0 || targetColumnIndex >= allColumns.size())
			throw new IllegalArgumentException(String.format("Invalid column index %d as size is %d", targetColumnIndex, allColumns.size()));
		this.allColumns = new ArrayList<>(allColumns);
		this.features = new ArrayList<>(allColumns);
		this.continuousValue = this.features.remove(targetColumnIndex);
		this.targetColumnIndex = targetColumnIndex;
		this.target = this.continuousValue.intValue();
	}

	@Override
	public Integer getTarget() {
		return target;
	}

	@Override
	public int getTargetIndex() {
		return targetColumnIndex;
	}

	public T getContinuousValue() {
		return continuousValue;
	}

	@Override
	public List<T> getFeatures() {
		return features;
	}

	@Override
	public List<T> getAllColumns() {
		return allColumns;
	}

	@Override
	public String toString() {
		return "DiscreteExemplar{" +
				", continuousValue=" + continuousValue +
				", target=" + target +
				", targetColumnIndex=" + targetColumnIndex +
				'}';
	}
}
