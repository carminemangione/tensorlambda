package com.mangione.continuous.performance.confusionmatrix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ROCConfusionMatrixPointTest {
	@Test
	public void calculateRates() {
		int truePositive = 10;
		int falsePositive = 20;
		int trueNegative = 30;
		int falseNegative = 40;

		ROCConfusionMatrixPoint rocPoint = new ROCConfusionMatrixPoint(
				new ConfusionMatrix(truePositive, falsePositive, trueNegative, falseNegative, .99));
		assertEquals(0.2, rocPoint.getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(0.6, rocPoint.getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(0.4, rocPoint.getFalsePositiveRate(), Double.MIN_VALUE);
		assertEquals(0.8, rocPoint.getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(.99, rocPoint.getThreshold(), Double.MIN_VALUE);
	}
}