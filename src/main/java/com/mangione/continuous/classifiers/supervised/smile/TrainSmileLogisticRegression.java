package com.mangione.continuous.classifiers.supervised.smile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import smile.classification.LogisticRegression;

public class TrainSmileLogisticRegression<FEATURE extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, FEATURE>,
		PROVIDER extends ObservationProviderInterface<FEATURE, EXEMPLAR>> {

	private final PROVIDER exemplarProvider;
	private final double lambda;
	private final double tol;
	private final int maxIter;
	private final Function<EXEMPLAR, FEATURE> targetFunction;
	private final Function<EXEMPLAR, double[]> featureVectorFunction;

	public TrainSmileLogisticRegression(PROVIDER exemplarProvider, double lambda, double tol, int maxIter,
			Function<EXEMPLAR, FEATURE> targetFunction, Function<EXEMPLAR, double[]> featureVectorFunction) {
		this.exemplarProvider = exemplarProvider;
		this.lambda = lambda;
		this.tol = tol;
		this.maxIter = maxIter;
		this.targetFunction = targetFunction;

		this.featureVectorFunction = featureVectorFunction;
	}

	public LogisticRegression train() {
		List<EXEMPLAR> exemplars = new ArrayList<>();
		for (EXEMPLAR exemplar : exemplarProvider) {
			exemplars.add(exemplar);
		}
		int[] targets = getTargets(exemplars);
		double[][] featuresArray = getFeaturesArray(exemplars);
		return LogisticRegression.fit(featuresArray, targets, lambda, tol, maxIter);
	}

	private double[][] getFeaturesArray(List<EXEMPLAR> exemplars) {
		return IntStream.range(0, exemplars.size())
				.boxed()
				.map(exemplars::get)
				.map(featureVectorFunction)
				.toArray(double[][]::new);
	}


	private int[] getTargets(List<EXEMPLAR> exemplars) {
		return exemplars.stream()
				.mapToInt(this::getTarget)
				.toArray();
	}

	private int getTarget(EXEMPLAR exemplar) {
		return targetFunction.apply(exemplar).intValue();
	}

}
