package com.mangione.continuous.observations.dense;

import java.util.function.IntFunction;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.util.coersion.CoerceToDoubleArray;

public class ContinuousExemplar implements ExemplarInterface<Double, Double> {
	private final double[] features;
	private final double target;

	public ContinuousExemplar(double[] features, double target) {
		this.features = features;
		this.target = target;
	}

	@Override
	public Double getLabel() {
		return target;
	}

	@Override
	public Double[] getFeatures(IntFunction<Double[]> featureBuilder) {
		return CoerceToDoubleArray.coerce(features);
	}

	@Override
	public Double getFeature(Integer index) {
		return features[index];
	}

	@Override
	public int numberOfFeatures() {
		return features.length;
	}
}
