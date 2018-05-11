package com.mangione.continuous.classifiers;

import com.mangione.cse151.observationproviders.ObservationProvider;
import com.mangione.cse151.observations.DiscreteExemplar;

public class NaiveBayesClassifier {
	private double[] priorProbabilities;
	private double[][] conditionalProbabilities;

	public NaiveBayesClassifier(ObservationProvider<DiscreteExemplar> exemplars, int numberOfCategories) throws Exception {
		if (exemplars.hasNext()) {
			DiscreteExemplar next = exemplars.next();
			int numberOfFeatures = next.getFeatures().length;
			conditionalProbabilities = new double[numberOfFeatures][numberOfCategories];
			final double[] exemplarsPerCategory = new double[numberOfCategories];
			final double[] featureCountPerCategory = new double[numberOfCategories];

			exemplars.reset();
			for (DiscreteExemplar exemplar : exemplars) {
				int target = (int) exemplar.getTarget();
				exemplarsPerCategory[target]++;
				for (int i = 0; i < exemplar.getFeatures().length; i++) {
					featureCountPerCategory[target] += exemplar.getFeatures()[i];
					conditionalProbabilities[i][target] += exemplar.getFeatures()[i];
				}
			}

			exemplars.reset();

			long numObservations = exemplars.getNumberOfLines();
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

	public int classify(DiscreteExemplar discreteExemplar) {
		double maximumLikelihood = Double.NEGATIVE_INFINITY;
		int category = -1;
		for (int i = 0; i < priorProbabilities.length; i++) {
			double currentLikelihood = priorProbabilities[i];
			for (int j = 0; j < conditionalProbabilities.length; j++) {
				currentLikelihood += conditionalProbabilities[j][i] * discreteExemplar.getFeatures()[j];
			}
			if (currentLikelihood > maximumLikelihood) {
				category = i;
				maximumLikelihood = currentLikelihood;
			}
		}
		return category;
	}
}
