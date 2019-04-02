package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.Observation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ObservationToExemplarProviderTest {
	@Test
	@SuppressWarnings("Convert2Diamond")
	public void provideExemplars() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<>(observations,
                        (features) -> new Observation<Integer>(Arrays.asList(features)));

		ObservationToExemplarProvider<Integer, ExemplarInterface<Integer, Integer>> exemplarProvider =
				new ObservationToExemplarProvider<Integer, ExemplarInterface<Integer, Integer>>(aop, (data) ->
						DiscreteExemplar.getExemplarTargetWithColumn(data.getFeatures(), 1));
		Iterator<ExemplarInterface<Integer, Integer>> iterator = exemplarProvider.iterator();
		assertTrue(iterator.hasNext());
		ExemplarInterface<Integer, Integer> next = iterator.next();
		assertEquals(Arrays.asList(1, 3), next.getFeatures());
		assertEquals(new Integer(2), next.getTarget());

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertEquals(Arrays.asList(4, 6), next.getFeatures());
		assertEquals(new Integer(5), next.getTarget());
		assertFalse(iterator.hasNext());
	}



}