package com.mangione.continuous.abalone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.mangione.continuous.observationproviders.MersenneTwisterFactory;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.sampling.SampledObservationProvider;
import com.mangione.continuous.thirdparty.apache.LinearRegression;

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

		for (DiscreteExemplar<Double> exemplar : testSet) {
			providerFactory.
		}

		testSet.forEach(x->
				bw.write(String.format("%d,%d", linearRegression.score(x), x.getContinuousValue());
		);

	}
}
