package com.mangione.continuous.classifiers.supervised.smile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import org.junit.Ignore;
import org.junit.Test;

import com.mangione.continuous.observationproviders.csv.CsvObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.ContinuousExemplar;
import com.mangione.continuous.sampling.SampledObservationProvider;
import com.mangione.continuous.sampling.reservoir.ReservoirSamplerWithoutReplacement;
import smile.classification.LogisticRegression;

public class TrainSmileLogisticRegressionTest {

	@Ignore
	@Test
	public void predictWithData() throws Exception {
		File observations = new File(Objects.requireNonNull(TrainSmileLogisticRegressionTest.class.getClassLoader().getResource("logisticData.csv")).toURI());
		CsvObservationProvider<Double, ExemplarInterface<Double, Double>> csvObservationProvider =
				new CsvObservationProvider<>(observations, new ExemplarCreator(), false, this::getReader);

		SampledObservationProvider<Double, ExemplarInterface<Double, Double>> trainDataSet =
				new SampledObservationProvider<>(csvObservationProvider, 5000, 76797, ReservoirSamplerWithoutReplacement::new);


		TrainSmileLogisticRegression<Double, ExemplarInterface<Double, Double>, SampledObservationProvider<Double, ExemplarInterface<Double, Double>>> regression
				= new TrainSmileLogisticRegression<>(trainDataSet, 0.02, 0.1, 10, ExemplarInterface::getLabel, ExemplarInterface::convertExemplarToDoubleArray);
		LogisticRegression train = regression.train();
		System.out.println(train.loglikelihood());

	}

	private BufferedReader getReader(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static class ExemplarCreator implements Function<String[], ExemplarInterface<Double, Double>> {
		@Override
		public ExemplarInterface<Double, Double> apply(String[] strings) {
			double[] featuresPlusTarget = Arrays.stream(strings)
					.mapToDouble(Double::parseDouble)
					.toArray();
			int locationOfTarget = featuresPlusTarget.length - 1;
			return new ContinuousExemplar(Arrays.copyOf(featuresPlusTarget, locationOfTarget),
					featuresPlusTarget[locationOfTarget]);
		}
	}

}