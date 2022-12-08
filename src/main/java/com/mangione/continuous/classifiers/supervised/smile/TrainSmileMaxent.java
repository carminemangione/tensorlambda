package com.mangione.continuous.classifiers.supervised.smile;

import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import smile.classification.Maxent;

public class TrainSmileMaxent<EXEMPLAR extends ExemplarInterface<Integer, Integer>,
		PROVIDER extends ObservationProviderInterface<Integer, EXEMPLAR>> {

	private final Maxent maxent;
	public TrainSmileMaxent(PROVIDER provider, double lambda, double tol, int maxIter, Function<EXEMPLAR, Integer> labelFunction) {

		ProviderToSmileOneEncodedSparse<EXEMPLAR, PROVIDER> oneEncoded = new ProviderToSmileOneEncodedSparse<>(provider, labelFunction);
		maxent = Maxent.fit(oneEncoded.getNumberOfFeatures(), oneEncoded.getColEndoced(), oneEncoded.getTags(), lambda, tol, maxIter);
	}

	public Maxent getMaxent() {
		return maxent;
	}
}
