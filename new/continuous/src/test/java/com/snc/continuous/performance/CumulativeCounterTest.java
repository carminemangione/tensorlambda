package com.mangione.continuous.performance;

import static org.junit.Assert.*;

import org.junit.Test;

public class CumulativeCounterTest {
	@Test
	public void countLEEmptyReturnsZero() {
		CumulativeCounter counter = new CumulativeCounter.Builder().build();
		assertEquals(0, counter.countLE(.10));
	}

	@Test
	public void countLEWithNoLowerReturns0() {
		CumulativeCounter counter = new CumulativeCounter.Builder()
				.add(.50)
				.build();
		assertEquals(0, counter.countLE(.10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeExcepts()  {
		new CumulativeCounter.Builder().add(-0.1);
	}

	@Test
	public void countLE() {
		CumulativeCounter counter = new CumulativeCounter.Builder()
				.add(.10)
				.add(.20)
				.add(.30)
				.add(.40)
				.add(.50)
				.build();
		assertEquals(0, counter.countLE(.01));
		assertEquals(1, counter.countLE(.10));
		assertEquals(1, counter.countLE(.15));
		assertEquals(5, counter.countLE(.50));
	}

	@Test
	public void countGT() {
		CumulativeCounter counter = new CumulativeCounter.Builder()
				.add(.10)
				.add(.20)
				.add(.30)
				.add(.40)
				.add(.50)
				.build();
		assertEquals(5, counter.countGT(.01));
		assertEquals(4, counter.countGT(.10));
		assertEquals(4, counter.countGT(.15));
		assertEquals(0, counter.countGT(.50));
	}

}