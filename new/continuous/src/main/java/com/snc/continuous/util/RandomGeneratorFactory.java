package com.mangione.continuous.util;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

public interface RandomGeneratorFactory {
	default RandomGenerator generate(long seed) {
		return new MersenneTwister(seed);
	}
}
