package com.mangione.continuous.classifiers.supervised;


import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

import java.util.Iterator;

public class NaiveBayesClassifier {
	private double[] priorProbabilities;
	private double[][] conditionalProbabilities;

	public NaiveBayesClassifier(ObservationProviderInterface<Double, ExemplarInterface<Double, Integer>> exemplars, int numberOfCategories) throws Exception {
		final Iterator<ExemplarInterface<Double, Integer>> iterator = exemplars.iterator();
		if (iterator.hasNext()) {
			ExemplarInterface<Double, Integer> next = iterator.next();
			int numberOfFeatures = next.getFeatures().size();
			conditionalProbabilities = new double[numberOfFeatures][numberOfCategories];
			final double[] exemplarsPerCategory = new double[numberOfCategories];
			final double[] featureCountPerCategory = new double[numberOfCategories];

			for (ExemplarInterface<Double, Integer> exemplar : exemplars) {
				int target =  exemplar.getTarget();
				exemplarsPerCategory[target]++;
				for (int i = 0; i < exemplar.getFeatures().size(); i++) {
					featureCountPerCategory[target] += exemplar.getFeatures().get(i);
					conditionalProbabilities[i][target] += exemplar.getFeatures().get(i);
				}
			}


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

	public int classify(ExemplarInterface<Double, Integer> discreteExemplar) {
		double maximumLikelihood = Double.NEGATIVE_INFINITY;
		int category = -1;
		for (int i = 0; i < priorProbabilities.length; i++) {
			double currentLikelihood = priorProbabilities[i];
			for (int j = 0; j < conditionalProbabilities.length; j++) {
				currentLikelihood += conditionalProbabilities[j][i] * discreteExemplar.getFeatures().get(j);
			}
			if (currentLikelihood > maximumLikelihood) {
				category = i;
				maximumLikelihood = currentLikelihood;
			}
		}
		return category;
	}
}
