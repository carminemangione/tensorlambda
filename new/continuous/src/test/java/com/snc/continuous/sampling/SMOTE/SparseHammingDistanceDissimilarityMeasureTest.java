package com.mangione.continuous.sampling.SMOTE;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class SparseHammingDistanceDissimilarityMeasureTest {
	@Test
	public void hammingDistance() {
		Integer[][] obs = {{1, 0, 0}, {1, 1, 1}, {0, 1, 1}};

		List<SparseExemplar<Integer, Integer>> exemplars = IntStream.range(0, obs.length)
				.boxed()
				.map(i -> new SparseExemplar<>(obs[i], new int[]{0, 1, 2}, 3, 0, 1))
				.collect(Collectors.toList());

		HammingDistanceDissimilarityMeasure dissimilarityMeasure = new HammingDistanceDissimilarityMeasure();
		DiscreteExperiment<ListObservationProvider<Integer, ObservationInterface<Integer>>> experiment = new DiscreteExperiment<>(new ListObservationProvider<>(exemplars));
		assertEquals(2, dissimilarityMeasure.computeDissimilarity(experiment, 0, 1), 0);
		assertEquals(3, dissimilarityMeasure.computeDissimilarity(experiment, 0, 2), 0);
		assertEquals(1, dissimilarityMeasure.computeDissimilarity(experiment, 1, 2), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nonHammingDistanceMeasureExcepts() {
		new HammingDistanceDissimilarityMeasure().computeDissimilarity(() -> 0, 0, 0);
	}


}