package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class ListObservationProviderTest {
	private static final List<Observation<Double>> DATA =
			Arrays.asList(new Observation<>(new Double[]{3.}),
					new Observation<>(new Double[]{5.}),
					new Observation<>(new Double[]{6.}),
					new Observation<>(new Double[]{7.}));

	private ListObservationProvider<Double, ObservationInterface<Double>> listObservationProvider;

	@Before
	public void setUp() {
		listObservationProvider = new ListObservationProvider<>(DATA);
	}

	@Test
	public void iterator() {
		int number = 0;
		for (ObservationInterface<Double> next : listObservationProvider) {
			validateData(DATA.get(number), next.getFeatures(Double[]::new));
			number++;
		}
		assertEquals(DATA.size(), number);
	}

	@Test
	public void getByIndexFromProvider() {
		ListObservationProvider<Double, ObservationInterface<Double>> provider = new ListObservationProvider<>(listObservationProvider);
		assertEquals(DATA.get(2), provider.getByIndex(2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getByIndexTooHighExcepts() {
		ListObservationProvider<Double, ObservationInterface<Double>> provider = new ListObservationProvider<>(listObservationProvider);
		provider.getByIndex(4);
	}

	@Test
	public void forEach() {
		final int[] number = {0};
		listObservationProvider.forEach(observation -> {
			validateData(DATA.get(number[0]), observation.getFeatures(Double[]::new));
			number[0]++;
		});
		assertEquals(DATA.size(), number[0]);
	}

	@Test
	public void twoIteratorsWorks() {
		final int[] number = {0};
		listObservationProvider.forEach(observation -> {
			validateData(DATA.get(number[0]), observation.getFeatures(Double[]::new));
			number[0]++;

			final int[] innerNumber = {0};
			listObservationProvider.forEach(innerObservation -> {
				validateData(DATA.get(innerNumber[0]), innerObservation.getFeatures(Double[]::new));
				innerNumber[0]++;
			});
			assertEquals(DATA.size(), innerNumber[0]);

		});
		assertEquals(DATA.size(), number[0]);
	}


	@Test
	public void forEachRemaining() {
		Iterator<ObservationInterface<Double>> iterator = listObservationProvider.iterator();
		iterator.next();
		final int[] innerNumber = {1};
		iterator.forEachRemaining(innerObservation -> {
			validateData(DATA.get(innerNumber[0]), innerObservation.getFeatures(Double[]::new));
			innerNumber[0]++;
		});
		assertEquals(DATA.size(), innerNumber[0]);
	}

	@Test
	public void hasNextDoesNotConsume() {
		Iterator<ObservationInterface<Double>> iterator = listObservationProvider.iterator();
		for (int i = 0; i < listObservationProvider.size(); i++)
			assertTrue(iterator.hasNext());
		assertTrue(iterator.hasNext());
	}


	private void validateData(Observation<Double> datum, Double[] features) {
		assertEquals(1, features.length);
		assertEquals(datum.getFeatures(Double[]::new)[0], features[0], 0);
	}


}