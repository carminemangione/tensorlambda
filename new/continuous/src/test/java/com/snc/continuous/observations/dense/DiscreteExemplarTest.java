package com.mangione.continuous.observations.dense;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class DiscreteExemplarTest {
	@Test
	public void targetMiddleColumn() {
		DiscreteExemplar discreteExemplar = new DiscreteExemplar(Arrays.asList(1, 2, 3, 4), 2);
		assertEquals(Integer.valueOf(2), discreteExemplar.getLabel());
		assertArrayEquals(new Integer[]{1, 2, 3, 4}, discreteExemplar.getFeatures(Integer[]::new));
	}

}