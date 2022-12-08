package com.mangione.continuous.sampling.reservoir;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;

public class ReservoirSamplerWithoutReplacement<R> extends ReservoirSamplerWithoutReplacementInterface<R> {
	private final Reservoir<R> reservoir;

	public ReservoirSamplerWithoutReplacement(Iterator<R> sourceElements, int sampleSize, int seed) {
		this(sourceElements, sampleSize, new MersenneTwister(seed));
	}

	public ReservoirSamplerWithoutReplacement(Iterator<R> sourceElements, int sampleSize, MersenneTwister random) {
		reservoir = initializeReservoir(sourceElements, sampleSize, random);
		while (sourceElements.hasNext()) {
			R next = sourceElements.next();
			double v = expDistributed(random, 1);
			reservoir.present(next, v);
		}
	}

	@Override
	public List<R> sample() {
		return reservoir.getItems();
	}
}
