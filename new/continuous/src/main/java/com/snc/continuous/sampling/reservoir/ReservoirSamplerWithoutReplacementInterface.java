package com.mangione.continuous.sampling.reservoir;

import java.util.Iterator;

import org.apache.commons.math3.random.MersenneTwister;

abstract class ReservoirSamplerWithoutReplacementInterface<R> implements SamplerInterface<R> {

	Reservoir<R> initializeReservoir(Iterator<R> sourceElements, int sampleSize, MersenneTwister random) {
		Reservoir<R> reservoir = new Reservoir<>(sampleSize);
		for (int index = 0; index < sampleSize; index++) {
			if (!sourceElements.hasNext())
				throw new IllegalArgumentException("Not enough source elements for sample size: " + sampleSize);
			R next = sourceElements.next();

			double v = expDistributed(random, 1);
			reservoir.present(next, v);
		}
		return reservoir;

	}

	static double expDistributed(MersenneTwister random, double weight) {
		double ran = random.nextDouble();
		return -Math.log(ran) / weight;
	}
}
