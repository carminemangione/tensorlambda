package com.mangione.continuous.classifiers.supervised;


import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.ContinuousExemplar;

import java.util.Iterator;

@SuppressWarnings("WeakerAccess")
public class NaiveBayesClassifier {
	private double[] priorProbabilities;
	private double[][] conditionalProbabilities;

	public NaiveBayesClassifier(ObservationProviderInterface<Double, ? extends ExemplarInterface<Double, Double>> exemplars, int numberOfCategories) {
		final Iterator<? extends ExemplarInterface<Double, Double>> iterator = exemplars.iterator();
		if (iterator.hasNext()) {
			ExemplarInterface<Double, Double> next = iterator.next();
			int numberOfFeatures = next.numberOfFeatures();
			conditionalProbabilities = new double[numberOfFeatures][numberOfCategories];
			final double[] exemplarsPerCategory = new double[numberOfCategories];
			final double[] featureCountPerCategory = new double[numberOfCategories];

			for (ExemplarInterface<Double, Double> exemplar : exemplars) {
				int target = exemplar.getLabel().intValue();
				exemplarsPerCategory[target]++;
				for (int i = 0; i < exemplar.numberOfFeatures(); i++) {
					featureCountPerCategory[target] += exemplar.getFeature(i);
					conditionalProbabilities[i][target] += exemplar.getFeature(i);
				}
			}


			long numObservations = exemplars.size();
			priorProbabilities = new double[numberOfCategories];
			for (int i = 0; i < numberOfCategories; i++) {
				priorProbabilities[i] = Math.log(exemplarsPerCategory[i] / numObservations);
			}

			for (int i = 0; i < conditionalProbabilities.length; i++) {
				for (int j = 0; j < numberOfCategories; j++) {
					conditionalProbabilities[i][j] = Math.log((conditionalProbabilities[i][j] + 1) / (featureCountPerCategory[j] + numberOfFeatures));
				}
			}
		}
	}

	public int classify(ContinuousExemplar discreteExemplar) {
		double maximumLikelihood = Double.NEGATIVE_INFINITY;
		int category = -1;
		for (int i = 0; i < priorProbabilities.length; i++) {
			double currentLikelihood = priorProbabilities[i];
			for (int j = 0; j < conditionalProbabilities.length; j++) {
				currentLikelihood += conditionalProbabilities[j][i] * discreteExemplar.getFeature(j);
			}
			if (currentLikelihood > maximumLikelihood) {
				category = i;
				maximumLikelihood = currentLikelihood;
			}
		}
		return category;
	}
}
