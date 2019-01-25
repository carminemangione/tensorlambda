package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

public class ArrayObservationProviderTest {
	private static final Double[][] DATA = new Double[][]{{3d}, {5d}, {6d}, {7d}};

	private ArrayObservationProvider<Double, ObservationInterface<Double>> aop;

	@Before
	public void setUp() {
		aop = new ArrayObservationProvider<>(DATA,
				new DoubleObservationFactory());
	}

	@Test
	public void iterator() {
		int number = 0;
		for (ObservationInterface<Double> next : aop) {
			validateData(DATA[number], next.getFeatures());
			number++;
		}
		assertEquals(DATA.length, number);
	}


	@Test
	public void forEach() {
		final int[] number = {0};
		aop.forEach(observation -> {
			validateData(DATA[number[0]], observation.getFeatures());
			number[0]++;
		});
		assertEquals(DATA.length, number[0]);
	}


	@Test
	public void fromProvider() {
		final int[] number = {0};

		ArrayObservationProvider<Double, ObservationInterface<Double>> wrapped =
				new ArrayObservationProvider<>(aop, new DoubleObservationFactory());
		wrapped.forEach(observation -> {
			validateData(DATA[number[0]], observation.getFeatures());
			number[0]++;
		});
		assertEquals(DATA.length, number[0]);
	}

	@Test
	public void twoIteratorsWorks() {
		final int[] number = {0};
		aop.forEach(observation -> {
			validateData(DATA[number[0]], observation.getFeatures());
			number[0]++;

			final int[] innerNumber = {0};
			aop.forEach(innerObservation -> {
				validateData(DATA[innerNumber[0]], innerObservation.getFeatures());
				innerNumber[0]++;
			});
			assertEquals(DATA.length, innerNumber[0]);

		});
		assertEquals(DATA.length, number[0]);
	}

	@Test
	public void remove() {
		Iterator<ObservationInterface<Double>> iterator = aop.iterator();
		iterator.next();
		iterator.remove();
		assertArrayEquals(DATA[2], toArray(iterator));

		iterator = aop.iterator();
		assertArrayEquals(DATA[0], toArray(iterator));
		assertArrayEquals(DATA[2], toArray(iterator));
	}

	private Double[] toArray(Iterator<ObservationInterface<Double>> iterator) {
		final List<Double> features = iterator.next().getFeatures();
		return features.toArray(new Double[0]);
	}

	@Test
	public void forEachRemaining() {
		Iterator<ObservationInterface<Double>> iterator = aop.iterator();
		iterator.next();
		final int[] innerNumber = {1};
		iterator.forEachRemaining(innerObservation -> {
			validateData(DATA[innerNumber[0]], innerObservation.getFeatures());
			innerNumber[0]++;
		});
		assertEquals(DATA.length, innerNumber[0]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void spliteratorNotSupported() {
		aop.spliterator();
	}


	private void validateData(Double[] datum, List<Double> features) {
		assertEquals(1, features.size());
		assertEquals(datum[0], features.get(0), 0);
	}


}