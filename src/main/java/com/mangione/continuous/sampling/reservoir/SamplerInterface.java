package com.mangione.continuous.sampling.reservoir;

import java.util.List;

public interface SamplerInterface<R> {
	List<R> sample();
}
