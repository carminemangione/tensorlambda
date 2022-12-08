package com.mangione.continuous.observations;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public interface ObservationInterface<FEATURE> {
	FEATURE[] getFeatures(IntFunction<FEATURE[]> featureBuilder);

	FEATURE getFeature(Integer index);

	default int[] getColumnIndexes() {
		return IntStream.range(0,numberOfFeatures())
				.boxed()
				.mapToInt(i->i)
				.toArray();
	}

	int numberOfFeatures();
}
