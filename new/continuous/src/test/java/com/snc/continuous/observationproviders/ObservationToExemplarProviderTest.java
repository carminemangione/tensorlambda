package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.Observation;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ObservationToExemplarProviderTest {

	private ObservationToExemplarProvider<Integer, ExemplarInterface<Integer, Integer>> exemplarProvider;

	@Before
	public void setUp() throws Exception {
		Integer[][] observations = new Integer[][]{{1, 2, 3}, {4, 5, 6}};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		exemplarProvider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(data.getFeatures(Integer[]::new)), 1));
	}

	@Test
	public void provideExemplars() {
		Iterator<ExemplarInterface<Integer, Integer>> iterator = exemplarProvider.iterator();
		assertTrue(iterator.hasNext());
		ExemplarInterface<Integer, Integer> next = iterator.next();
		assertArrayEquals(new Integer[]{1, 2, 3}, next.getFeatures(Integer[]::new));
		assertEquals(Integer.valueOf(1), next.getLabel());

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertArrayEquals(new Integer[]{4, 5, 6}, next.getFeatures(Integer[]::new));
		assertEquals(Integer.valueOf(1), next.getLabel());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void forEach() {
		AtomicInteger i = new AtomicInteger();
		exemplarProvider.forEach(ex-> {
			if (i.get() == 0) {
				assertArrayEquals(new Integer[]{1, 2, 3}, ex.getFeatures(Integer[]::new));
				assertEquals(Integer.valueOf(1), ex.getLabel());
				i.getAndIncrement();
			}
			else if (i.get() == 1){
				assertArrayEquals(new Integer[]{4, 5, 6}, ex.getFeatures(Integer[]::new));
				assertEquals(Integer.valueOf(1), ex.getLabel());
			}
			else {
				fail("For each went too many times");
			}

		});
	}
}