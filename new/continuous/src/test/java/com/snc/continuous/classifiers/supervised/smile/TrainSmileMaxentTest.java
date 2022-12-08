package com.mangione.continuous.classifiers.supervised.smile;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

import org.junit.Test;

import com.mangione.continuous.observationproviders.BagOfWordsObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.csv.CsvObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.sampling.SampledObservationProvider;
import com.mangione.continuous.sampling.reservoir.ReservoirSamplerWithoutReplacementFactory;
import smile.validation.metric.Accuracy;


public class TrainSmileMaxentTest {
	@Test
	public void maxEnt() {
		CsvObservationProvider<String, Observation<String>> csvProvider = new CsvObservationProvider<>(
				null, Observation::new, true, readerFunction());
		BagOfWordsObservationProvider provider = new BagOfWordsObservationProvider(csvProvider, 3, 4);

		ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>> train =
				new SampledObservationProvider<>(provider, 350, 8382, new ReservoirSamplerWithoutReplacementFactory<>());

		ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>> test =
				new SampledObservationProvider<>(provider, 180, 9876, new ReservoirSamplerWithoutReplacementFactory<>());

		TrainSmileMaxent<SparseExemplar<Integer, Integer>, ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>>> maxent =
				new TrainSmileMaxent<>(train, 0, 0.0000001, 1000000, ExemplarInterface::getLabel);

		ProviderToSmileOneEncodedSparse<SparseExemplar<Integer, Integer>, ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>>> encodedTest
				= new ProviderToSmileOneEncodedSparse<>(test, ExemplarInterface::getLabel);

		int[] truth = test.getStream()
				.mapToInt(ExemplarInterface::getLabel)
				.toArray();
		int[] predict = maxent.getMaxent().predict(encodedTest.getColEndoced());
		assertTrue(Accuracy.of(truth, predict) > 0.90);
	}

	private Function<File, BufferedReader> readerFunction() {
		return f -> {
			InputStream data = TrainSmileMaxent.class.getClassLoader().getResourceAsStream("Youtube04-Eminem.csv");
			if (data == null)
				throw new RuntimeException("Could not load resource");
			return new BufferedReader(new InputStreamReader(data));
		};
	}
}