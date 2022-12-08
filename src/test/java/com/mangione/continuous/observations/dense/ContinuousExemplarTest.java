package com.mangione.continuous.observations.dense;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.stream.IntStream;

import org.junit.Test;

import com.mangione.continuous.util.coersion.CoerceToDoubleArray;

public class ContinuousExemplarTest {
	@Test
	public void creationAndUse() {
		double[] values = new double[] {1, 3, 5};
		int target = 2;

		ContinuousExemplar continuousExemplar = new ContinuousExemplar(values, target);
		assertArrayEquals(values, CoerceToDoubleArray.coerce(continuousExemplar.getFeatures(Double[]::new)), 0);
		Double[] features = continuousExemplar.getFeatures(Double[]::new);

		IntStream.range(0, values.length)
				.boxed()
				.forEach(i -> assertEquals(values[i], features[i], 0));
		assertEquals(2, continuousExemplar.getLabel().longValue());
	}
}