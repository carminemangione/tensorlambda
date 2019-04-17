package com.mangione.continuous.observations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ObservationInterface<T> {
	List<T> getFeatures();
	List<T> getAllColumns();

	default T getFeature(int index) {
		return getFeatures().get(index);
	}

	default Set<Integer> getColumnIndexes() {
		return IntStream.range(0,numberOfFeatures())
				.boxed()
				.collect(Collectors.toSet());
	}

	default int numberOfFeatures() {
		return getFeatures().size();
	}
}
