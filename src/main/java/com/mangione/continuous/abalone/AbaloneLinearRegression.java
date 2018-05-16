package com.mangione.continuous.abalone;

import com.mangione.continuous.calculators.MinMaxInverter;
import com.mangione.continuous.calculators.VariableCalculator;
import com.mangione.continuous.observationproviders.MersenneTwisterFactory;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.sampling.SampledObservationProvider;
import com.mangione.continuous.thirdparty.apache.LinearRegression;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AbaloneLinearRegression {

	public static void main(String[] args) throws Exception {
		AbaloneObservationProviderFactory providerFactory = new AbaloneObservationProviderFactory();
		SampledObservationProvider<Double, DiscreteExemplar<Double>> trainingSet =
				new SampledObservationProvider<>(.30, providerFactory.getAbaloneProvider(),
						new DiscreteExemplarFactory(), new MersenneTwisterFactory(), 1001, false);

		LinearRegression linearRegression = new LinearRegression();
		linearRegression.train(trainingSet);

		File results = new File("lrresults.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(results));
		bw.write("predicted,actual");
		SampledObservationProvider<Double, DiscreteExemplar<Double>> testSet =
				new SampledObservationProvider<>(.30, providerFactory.getAbaloneProvider(),
						new DiscreteExemplarFactory(), new MersenneTwisterFactory(), 1001, true);

		VariableCalculator<Double, Double> invertTarget = getInvertTarget(providerFactory, testSet.iterator().next().getTargetIndex());;

		testSet.forEach(x -> {
			try {

				bw.write(String.format("%f,%f", linearRegression.score(x), x.getContinuousValue()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

	}

	private static MinMaxInverter getInvertTarget(AbaloneObservationProviderFactory providerFactory, int index) {
		return new MinMaxInverter(providerFactory
				.getMinMaxScaleCalculations().getCalculator(index));
	}
}
