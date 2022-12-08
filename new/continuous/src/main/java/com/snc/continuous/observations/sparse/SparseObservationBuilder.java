package com.mangione.continuous.observations.sparse;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntFunction;

public class SparseObservationBuilder<FEATURE> {
	private final int numberOfFeatures;
	private final FEATURE missingValue;
	private final Map<Integer, FEATURE> featureMap = new TreeMap<>();

	public SparseObservationBuilder(int numberOfFeatures, FEATURE missingValue) {
		this.numberOfFeatures = numberOfFeatures;
		this.missingValue = missingValue;
	}

	public void setFeature(int column, FEATURE feature) {
		if (column < 0 || column >= numberOfFeatures)
			throw new IllegalArgumentException("Invalid index " + column);
		featureMap.put(column, feature);
	}

	public SparseObservation<FEATURE> build(IntFunction<FEATURE[]> generator) {

		return new SparseObservation<>(
				featureMap.values().toArray(generator.apply(featureMap.size())),
				featureMap.keySet().stream().mapToInt(Integer::intValue).toArray(),
				numberOfFeatures, missingValue);
	}
}
