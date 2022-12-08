package com.mangione.continuous.performance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MultiCumulativeCounterTest {
	@Test
	public void testMCC() {
		MultiCumulativeCounter.Builder<Integer> mccBuilder = new MultiCumulativeCounter.Builder<>();
		mccBuilder
				.add(0.1, 0)
				.add(0.2, 0)
				.add(0.4, 0)
				.add(0.6, 0)
				.add(0.11, 1)
				.add(0.21, 1)
				.add(0.41, 1)
				.add(0.61, 1);
		MultiCumulativeCounter<Integer> multiCumulativeCounter = mccBuilder.build();
		CumulativeCounter allCounter = multiCumulativeCounter.getAllCounter();
		assertEquals(2, allCounter.countLE(0.11));
		assertEquals(3, allCounter.countLE(0.2));
		assertEquals(6, allCounter.countLE(0.41));
		assertEquals(6, allCounter.countLE(0.42));
		CumulativeCounter cc1 = multiCumulativeCounter.getCounter(1);
		assertEquals(1, cc1.countLE(0.11));
		assertEquals(1, cc1.countLE(0.2));
		assertEquals(3, cc1.countLE(0.41));
		assertEquals(3, cc1.countLE(0.42));
	}

	@Test
	public void getAllTags() {
		MultiCumulativeCounter.Builder<Integer> mccBuilder = new MultiCumulativeCounter.Builder<>();
		MultiCumulativeCounter<Integer> counter = mccBuilder
				.add(0.1, 0)
				.add(0.41, 1)
				.add(0.2, 0)
				.build();
		assertEquals(2, counter.getTags().size());
		assertEquals(0, (long)counter.getTags().first());
		assertEquals(1, (long)counter.getTags().last());

	}

}