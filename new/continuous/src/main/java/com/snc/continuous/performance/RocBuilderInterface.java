package com.mangione.continuous.performance;

public interface RocBuilderInterface<POINT extends ROCPointInterface> {
	RocBuilderInterface<POINT> add(double threshold, Integer actual, Integer predicted);
	ROCInterface<POINT> build();
}
