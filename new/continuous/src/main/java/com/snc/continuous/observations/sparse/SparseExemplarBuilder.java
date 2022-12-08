package com.mangione.continuous.observations.sparse;

import java.util.Arrays;
import java.util.function.IntFunction;

public class SparseExemplarBuilder<FEATURE, TARGET> {
	private final TARGET target;
	private final SparseObservationBuilder<FEATURE> sparseObservationBuilder;
	private final FEATURE missingValue;
	private final int numberOfFeatures;

	public SparseExemplarBuilder(int numberOfFeatures, FEATURE missingValue, TARGET target) {
		this.target = target;
		this.missingValue = missingValue;
		this.numberOfFeatures = numberOfFeatures;
		this.sparseObservationBuilder = new SparseObservationBuilder<>(numberOfFeatures, missingValue);
	}

	public void setFeature(int column, FEATURE feature) {
		sparseObservationBuilder.setFeature(column, feature);
	}

	public SparseExemplar<FEATURE, TARGET> build(IntFunction<FEATURE[]> generator) {
		SparseObservation<FEATURE> observation = sparseObservationBuilder.build(generator);
		return new SparseExemplar<>(Arrays.stream(observation.getColumnIndexes()).boxed().map(observation::getFeature).toArray(generator),
				observation.getColumnIndexes(), numberOfFeatures, missingValue, target);
	}
}
