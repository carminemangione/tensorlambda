package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.dense.Observation;

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

}