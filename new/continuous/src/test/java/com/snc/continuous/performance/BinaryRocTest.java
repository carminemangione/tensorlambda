package com.mangione.continuous.performance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BinaryRocTest {
	@Test
	public void outOfOrderScores() {
		BinaryRoc.Builder rocBuilder = new BinaryRoc.Builder();
		rocBuilder.add(0.3, 1, 0)
				.add(0.7, 0, 0)
				.add(0.2, 0, 0)
				.add(0.2, 1, 0)
				.add(0.9, 1, 0);

		List<ROCPoint> points = rocBuilder.build().getROC();
		assertEquals(4, points.size());
		assertEquals(.9, points.get(0).getThreshold(), Double.MIN_VALUE);
		assertEquals(1., points.get(0).getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(1., points.get(0).getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(0., points.get(0).getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(0., points.get(0).getFalsePositiveRate(), Double.MIN_VALUE);

		assertEquals(0.70, points.get(1).getThreshold(), Double.MIN_VALUE);
		assertEquals(2./3, points.get(1).getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(2./2, points.get(1).getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(1 - 2./3, points.get(1).getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(0, points.get(1).getFalsePositiveRate(), Double.MIN_VALUE);

		assertEquals(0.3, points.get(2).getThreshold(), Double.MIN_VALUE);
		assertEquals(2./3, points.get(2).getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(1./2, points.get(2).getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(1 - 2./3, points.get(2).getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(1 - 1./2, points.get(2).getFalsePositiveRate(), Double.MIN_VALUE);

		assertEquals(0.20, points.get(3).getThreshold(), Double.MIN_VALUE);
		assertEquals(1/3., points.get(3).getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(1/2., points.get(3).getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(1 - 1/3., points.get(3).getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(1 - 1/2., points.get(3).getFalsePositiveRate(), Double.MIN_VALUE);
	}

	@Test(expected = IllegalStateException.class)
	public void emptyExcepts() {
		new BinaryRoc.Builder().build();
	}

	@Test(expected = IllegalStateException.class)
	public void emptyPositiveExcepts()  {
	new BinaryRoc.Builder().add(0.3, 0, 0).build();
	}

	@Test(expected = IllegalStateException.class)
	public void emptyNegativeExcepts() {
		new BinaryRoc.Builder().add(0.3, 1, 0).build();

	}
}