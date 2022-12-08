package com.mangione.continuous.performance.confusionmatrix;

import static org.junit.Assert.*;

import org.junit.Test;


public class ConfusionMatrixTest {

	@Test
	public void createConfusionMatrix() {
		ConfusionMatrix.Builder builder = new ConfusionMatrix.Builder(0.5);
		builder.add(1, 1)
				.add(1, 1)
				.add(1, 0)
				.add(1, 0)
				.add(1, 0)
				.add(1, 0)
				.add(0, 0)
				.add(0, 1)
				.add(0, 1)
				.add(0, 1);

		ConfusionMatrix matrix = builder.build();
		assertEquals(2, matrix.getNumTruePositive());
		assertEquals(4, matrix.getNumFalsePositive());
		assertEquals(1, matrix.getNumTrueNegative());
		assertEquals(3, matrix.getNumFalseNegative());
		assertEquals(0.5, matrix.getThreshold(), Double.MIN_VALUE);
	}


}