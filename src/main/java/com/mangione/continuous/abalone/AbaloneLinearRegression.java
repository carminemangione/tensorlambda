package com.mangione.continuous.abalone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.mangione.continuous.calculators.VariableCalculator;
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

		providerFactory.getAbaloneProvider().forEach(exemplar -> System.out.println(StringUtils.join(exemplar.getAllColumns(), ", ")));

		LinearRegression linearRegression = new LinearRegression();
		linearRegression.train(trainingSet);

		File results = new File("lrresults.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(results));
		bw.write("predicted,actual");
		SampledObservationProvider<Double, DiscreteExemplar<Double>> testSet =
				new SampledObservationProvider<>(.30, providerFactory.getAbaloneProvider(),
						new DiscreteExemplarFactory(), new MersenneTwisterFactory(), 1001, true);

		VariableCalculator<Double, Double> invertTarget = providerFactory.getInvertedScaling(
				testSet.iterator().next().getTargetIndex());

		testSet.forEach(x -> {
			try {
				String resultString = String.format("%f,%f", invertTarget.apply(linearRegression.score(x)).get(0),
						invertTarget.apply(x.getContinuousValue()).get(0));
				bw.write(resultString);
				System.out.println(resultString);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

	}
}
