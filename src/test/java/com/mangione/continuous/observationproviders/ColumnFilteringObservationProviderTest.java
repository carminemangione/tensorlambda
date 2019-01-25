package com.mangione.continuous.observationproviders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.Observation;

public class ColumnFilteringObservationProviderTest {
	@Test
	public void provideOneFilteredColumn() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		@SuppressWarnings("Convert2Diamond") ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<Integer, Observation<Integer>>(observations,
						Observation::new);



		ObservationToExemplarProvider<Integer> exemplarProvider = new ObservationToExemplarProvider<>(aop, 1);
		Iterator<DiscreteExemplar<Integer>> iterator = exemplarProvider.iterator();
		assertTrue(iterator.hasNext());
		DiscreteExemplar<Integer> next = iterator.next();
		assertEquals(Arrays.asList(1, 3), next.getFeatures());
		assertEquals(new Integer(2), next.getTarget());

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertEquals(Arrays.asList(4, 6), next.getFeatures());
		assertEquals(new Integer(5), next.getTarget());
		assertFalse(iterator.hasNext());
	}



}