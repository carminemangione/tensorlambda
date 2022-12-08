package com.mangione.continuous.sampling.reservoir;

import java.util.Iterator;

public interface SamplerFactoryInterface<R> {
	SamplerInterface<R> createSampler(Iterator<R> sourceElements, int sampleSize, int seed);
}
