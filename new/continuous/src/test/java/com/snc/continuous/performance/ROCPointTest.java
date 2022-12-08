package com.mangione.continuous.performance;

import static org.junit.Assert.*;

import org.junit.Test;

public class ROCPointTest {
	@Test
	public void falseNegativeRate() {
		ROCPoint rocPoint = new ROCPoint(5, 10, 20, 30, .05);
		assertEquals(0.05, rocPoint.getThreshold(), Double.MIN_VALUE);
		assertEquals(1./3, rocPoint.getFalseNegativeRate(), Double.MIN_VALUE);
	}

	@Test
	public void trueNegativeRate() {
		ROCPoint rocPoint = new ROCPoint(5, 10, 20, 30, .05);
		assertEquals(0.25, rocPoint.getTrueNegativeRate(), Double.MIN_VALUE);
	}

	@Test
	public void falsePositiveRate() {
		ROCPointInterface rocPoint = new ROCPoint(5, 10, 20, 30, .05);
		assertEquals(.75, rocPoint.getFalsePositiveRate(), Double.MIN_VALUE);
	}

	@Test
	public void truePositiveRate() {
		ROCPointInterface rocPoint = new ROCPoint(5, 10, 20, 30, .05);
		assertEquals(1 - 1./3, rocPoint.getTruePositiveRate(), Double.MIN_VALUE);
	}
}