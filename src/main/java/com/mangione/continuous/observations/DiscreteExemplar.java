package com.mangione.continuous.observations;

import java.util.List;

public class DiscreteExemplar<T extends Number> implements ExemplarInterface<T, Integer> {
	private final List<T> features;
	private final T continuousValue;
	private final Integer target;

	public DiscreteExemplar(List<T> features, T continuousValue, int target) {
		this.features = features;
		this.continuousValue = continuousValue;
		this.target = target;
	}

	public DiscreteExemplar(List<T> features) {
		this.features = features.subList(0, features.size() - 1);
		this.continuousValue = features.get(features.size() - 1);
		this.target = this.continuousValue.intValue();
	}

	@Override
	public Integer getTarget() {
		return target;
	}

	public T getContinuousValue() {
		return continuousValue;
	}

	@Override
	public List<T> getExemplar() {
		return features.subList(0, features.size() - 1);
	}

	@Override
	public List<T> getFeatures() {
		return features;
	}

	@Override
	public String toString() {
		return "DiscreteExemplar{" +
				"target=" + target +
				", continuousValue=" + continuousValue +
				'}';
	}
}
