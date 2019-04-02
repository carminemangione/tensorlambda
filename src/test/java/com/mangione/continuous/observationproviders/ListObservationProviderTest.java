package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListObservationProviderTest {
	private static final List<Observation<Double>> DATA =
			Arrays.asList(new Observation<>(Collections.singletonList(3d)),  
					new Observation<>(Collections.singletonList(5d)), 
					new Observation<>(Collections.singletonList(6d)), 
					new Observation<>(Collections.singletonList(7d)));

	private ListObservationProvider<Double, ObservationInterface<Double>> aop;

	@Before
	public void setUp() {
		aop = new ListObservationProvider<>(DATA);
	}

	@Test
	public void iterator() {
		int number = 0;
		for (ObservationInterface<Double> next : aop) {
			validateData(DATA.get(number), next.getFeatures());
			number++;
		}
		assertEquals(DATA.size(), number);
	}


	@Test
	public void forEach() {
		final int[] number = {0};
		aop.forEach(observation -> {
			validateData(DATA.get(number[0]), observation.getFeatures());
			number[0]++;
		});
		assertEquals(DATA.size(), number[0]);
	}

	@Test
	public void twoIteratorsWorks() {
		final int[] number = {0};
		aop.forEach(observation -> {
			validateData(DATA.get(number[0]), observation.getFeatures());
			number[0]++;

			final int[] innerNumber = {0};
			aop.forEach(innerObservation -> {
				validateData(DATA.get(innerNumber[0]), innerObservation.getFeatures());
				innerNumber[0]++;
			});
			assertEquals(DATA.size(), innerNumber[0]);

		});
		assertEquals(DATA.size(), number[0]);
	}


	@Test
	public void forEachRemaining() {
		Iterator<ObservationInterface<Double>> iterator = aop.iterator();
		iterator.next();
		final int[] innerNumber = {1};
		iterator.forEachRemaining(innerObservation -> {
			validateData(DATA.get(innerNumber[0]), innerObservation.getFeatures());
			innerNumber[0]++;
		});
		assertEquals(DATA.size(), innerNumber[0]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void spliteratorNotSupported() {
		aop.spliterator();
	}


	private void validateData(Observation<Double> datum, List<Double> features) {
		assertEquals(1, features.size());
		assertEquals(datum.getFeatures().get(0), features.get(0), 0);
	}


}