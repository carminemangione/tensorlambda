package com.mangione.continuous.observations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ObservationInterface<T> {
	List<T> getFeatures();
	List<T> getAllColumns();

	default T getFeature(int index) {
		return getFeatures().get(index);
	}

	default List<Integer> getColumnIndexes() {
		return IntStream.range(0, getFeatures().size())
				.boxed()
				.collect(Collectors.toList());
	}
}
