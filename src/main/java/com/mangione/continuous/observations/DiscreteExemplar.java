package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.List;

public class DiscreteExemplar<T extends Number> implements ExemplarInterface<T, Integer> {
	private final List<T> features;
	private final T continuousValue;
	private final Integer target;
	private final List<T> allColumns;
	private final int targetColumnIndex;

	public DiscreteExemplar(List<T> features, T continuousValue, int target) {
		this.allColumns = new ArrayList<>(features);
		this.allColumns.add(continuousValue);
		this.features = features;
		this.continuousValue = continuousValue;
		this.target = target;
		targetColumnIndex = features.size();
	}

	public DiscreteExemplar(List<T> features) {
		this.allColumns = features;
		this.targetColumnIndex = features.size() - 1;
		this.features = features.subList(0, targetColumnIndex);
		this.continuousValue = features.get(targetColumnIndex);
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
