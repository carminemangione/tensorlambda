package com.mangione.continuous.classifiers.unsupervised.smile;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class SparseToDenseFromProviderTest {

	@Test
	public void sparseToDenseTwoColumns() {
		Integer[][] sparseObs = {{0, 0}, {0, 1}};
		SparseToDenseFromProvider<ObservationInterface<Integer>> sparseToDense = new SparseToDenseFromProvider<>(new ArrayObservationProvider<>(
				sparseObs, Observation::new));
		assertArrayEquals(new int[]{6}, sparseToDense.process(new Observation<>(new Integer[]{0, 6})));
	}

	@Test
	public void fourColumns() {
		Integer[][] sparseObs = {{0, 0, 0, 0}, {0, 1, 1, 0}};
		SparseToDenseFromProvider<ObservationInterface<Integer>> sparseToDense = new SparseToDenseFromProvider<>(new ArrayObservationProvider<>(
				sparseObs, Observation::new));
		assertArrayEquals(new int[]{6,7}, sparseToDense.process(new Observation<>(new Integer[] {0, 6, 7, 1})));
	}
}