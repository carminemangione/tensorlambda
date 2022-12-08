package com.mangione.continuous.observations.dense;

import java.util.List;
import java.util.function.IntFunction;

import com.mangione.continuous.observations.ExemplarInterface;

public class Exemplar<FEATURE, LABEL> implements ExemplarInterface<FEATURE, LABEL> {
	private final List<FEATURE> features;
	private final LABEL label;


	public Exemplar(List<FEATURE> features, LABEL label) {
		this.features = features;
		this.label = label;
	}

	@Override
	public LABEL getLabel() {
		return label;
	}

	@Override
	public FEATURE[] getFeatures(IntFunction<FEATURE[]> featureBuilder) {
		return features.toArray(featureBuilder);
	}

	@Override
	public FEATURE getFeature(Integer index) {
		return features.get(index);
	}

	@Override
	public int numberOfFeatures() {
		return features.size();
	}


	@Override
	public String toString() {
		return "DiscreteExemplar{" +
				", target=" + label +
				'}';
	}
}
