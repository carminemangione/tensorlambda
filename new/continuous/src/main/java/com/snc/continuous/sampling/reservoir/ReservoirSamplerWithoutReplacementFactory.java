package com.mangione.continuous.sampling.reservoir;

import java.util.Iterator;

public class ReservoirSamplerWithoutReplacementFactory<R> implements SamplerFactoryInterface<R> {
	@Override
	public SamplerInterface<R> createSampler(Iterator<R> sourceElements, int sampleSize, int seed) {
		return new ReservoirSamplerWithoutReplacement<>(sourceElements, sampleSize, seed);
	}
}
