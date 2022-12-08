package com.mangione.continuous.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.stream.IntStream;

import org.junit.Test;

public class MurmurHash3RandomTest {

	@Test
	public void nextDouble() {
		int numTests = 10000;
		MurmurHash3Random random = new MurmurHash3Random(18769);

		double var = 1. / 12.;
		double std = Math.sqrt(var);
		double k = 4;
		double expectedValue = 0.5;

		double prob = (double)IntStream.range(0, numTests)
				.mapToDouble(i -> Math.abs(random.nextDouble(i) - expectedValue))
				.filter(ran -> (ran >= std * k))
				.count() / (double)numTests;

		assertTrue(prob < 1.0 / Math.pow(numTests, 2));

	}

	@Test
	public void usesSeed() {
		MurmurHash3Random random = new MurmurHash3Random(18769);
		double[] first = IntStream.range(0, 10)
				.mapToDouble(random::nextDouble)
				.toArray();

		MurmurHash3Random sameSeedRandom = new MurmurHash3Random(18769);
		double[] sameSeed = IntStream.range(0, 10)
				.mapToDouble(sameSeedRandom::nextDouble)
				.toArray();

		assertArrayEquals(first, sameSeed, Double.MIN_VALUE);

		MurmurHash3Random otherSeedRandom = new MurmurHash3Random(666666);
		double[] otherSeed = IntStream.range(0, 10)
				.mapToDouble(otherSeedRandom::nextDouble)
				.toArray();
		boolean equal = true;
		for (int i = 0; i < otherSeed.length && equal; i++) {
			equal = !(Math.abs(first[i] - otherSeed[i]) > Double.MIN_VALUE);
		}
		assertFalse(equal);
	}
}