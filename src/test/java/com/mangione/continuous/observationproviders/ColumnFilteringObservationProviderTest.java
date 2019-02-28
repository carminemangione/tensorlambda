package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.sparse.SparseObservation;

public class ColumnFilteringObservationProviderTest {
	@Test
	public void provideOneFilteredColumn() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationFactoryInterface<Integer, Observation<Integer>> factory = (features, columns) -> new Observation<>(features);
		ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<>(observations, factory);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop = new ColumnFilteringObservationProvider<>(aop,
				new int[]{0, 2}, factory);

	    Iterator<Observation<Integer>> iterator = cfop.iterator();
		assertTrue(iterator.hasNext());
		Observation<Integer> next = iterator.next();
		assertEquals(Collections.singletonList(2), next.getFeatures());

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertEquals(Collections.singletonList(5), next.getFeatures());
		assertEquals(1, next.numberOfFeatures());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void doesNotCallGetFeaturesBecauseItExpandsSparseArrays() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationFactoryInterface<Integer, ObservationInterface<Integer>> factory = (features, columns) -> new Observation<>(features);
		ExceptionIfGetFeaturesCalledObservationProvider provider
				= new ExceptionIfGetFeaturesCalledObservationProvider(observations, factory);

		ColumnFilteringObservationProvider<Integer, ObservationInterface<Integer>> cfop = new ColumnFilteringObservationProvider<>(provider,
				new int[]{0, 2}, factory);

		Iterator<ObservationInterface<Integer>> iterator = cfop.iterator();
		//noinspection WhileLoopReplaceableByForEach
		while(iterator.hasNext())
			iterator.next();
	}


	private static class ExceptionIfGetFeaturesCalledObservationProvider
			extends ArrayObservationProvider<Integer, ObservationInterface<Integer>> {

		private ExceptionIfGetFeaturesCalledObservationProvider(Integer[][] data, ObservationFactoryInterface<Integer,
				ObservationInterface<Integer>> observationFactoryInterface) {
			super(data, observationFactoryInterface);
		}

		@Override
		public ObservationInterface<Integer> create(List<Integer> data, int[] columns) {
			return new SparseObservation<Integer>(data.toArray(new Integer[0]),
					IntStream.range(0, data.size()).toArray(), 3, 0) {
				@Override
				public List<Integer> getFeatures() {
					throw new RuntimeException("Get features expands the ");
				}
			};

		}
	}

}