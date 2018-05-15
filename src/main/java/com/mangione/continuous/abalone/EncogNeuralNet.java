package com.mangione.continuous.abalone;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.encog.ml.MLRegression;
import org.encog.ml.model.EncogModel;
import org.encog.util.simple.EncogUtility;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.thirdparty.encog.MLDataSetProviderDelegate;

public class EncogNeuralNet {
	private final static String EGA_FILENAME = "src/main/resources/Abalone/abalone.ega";

	private static String[] theInputHeadings = {"Sex", "Shell Length",
			"Shell Diameter", "Shell Height", "Total Abalone Weight",
			"Shucked Weight", "Viscera Weight", "Shell Weight", "Rings"};

	private EncogModel abaloneModel;

	private EncogNeuralNet() throws Exception {

		AbaloneObservationProviderFactory abaloneObservationProviderFactory = new AbaloneObservationProviderFactory();
		MLRegression bestMethod = trainModel(abaloneObservationProviderFactory.getAbaloneProvider(), .3, 5, true, 1001);

		System.out.println("Training error: " + EncogUtility.calculateRegressionError(bestMethod, abaloneModel.getTrainingDataset()));
		System.out.println("Validation error: " + EncogUtility.calculateRegressionError(bestMethod, abaloneModel.getValidationDataset()));

		System.out.println("Final model: " + bestMethod);

	}

	private MLRegression trainModel(ObservationProviderInterface<Double, DiscreteExemplar<Double>> provider,
			double validationFraction, int folds, boolean shuffle, int seed) {

		return null;
	}


	public static void main(String[] args) throws Exception {
		EncogNeuralNet nnet = new EncogNeuralNet();


	}
}
