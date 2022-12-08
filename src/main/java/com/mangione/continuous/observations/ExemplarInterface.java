package com.mangione.continuous.observations;

import java.util.stream.IntStream;

public interface ExemplarInterface <FEATURE, TAG> extends ObservationInterface<FEATURE> {
	TAG getLabel();

	default double[] convertExemplarToDoubleArray() {
		return IntStream.range(0, numberOfFeatures())
				.boxed()
				.map(this::getFeature)
				.map(feat -> (Number)feat)
				.mapToDouble(Number::doubleValue)
				.toArray();
	}
}
