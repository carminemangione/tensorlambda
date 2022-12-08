package com.mangione.continuous.sampling.reservoir;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;

/***
 * Algorithm from: Sequential Reservoir Sampling with a
 * Non-Uniform Distribution, Kolonko, M., Wasch, D. Nov 2004
 */
@SuppressWarnings("WeakerAccess")
public class ReservoirSamplerWithoutReplacementWFF<R> extends ReservoirSamplerWithoutReplacementInterface<R> {
	private final Reservoir<R> reservoir;

	public ReservoirSamplerWithoutReplacementWFF(Iterator<R> sourceElements, int sampleSize, int seed) {
		MersenneTwister random = new MersenneTwister(seed);
		reservoir = initializeReservoir(sourceElements, sampleSize, random);

		double r = reservoir.getLastValue();
		double y = expDistributed(random, r);
		double F = 0;
		while (sourceElements.hasNext()) {
			R next = sourceElements.next();
			F += 1;
			if (F > y) {
				double v = -Math.log(1 - random.nextDouble() * (1 - Math.exp(-1 * r)));
				reservoir.present(next, v);
				r = reservoir.getLastValue();
				y = expDistributed(random, r);
				F = 0;
			}
		}
	}

	public List<R> sample() {
		return reservoir.getItems();
	}

}
