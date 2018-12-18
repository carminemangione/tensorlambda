package com.mangione.continuous.observations;

import java.util.List;

public interface ObservationInterface<T> {
	List<T> getFeatures();
	List<T> getAllColumns();
	default T getFeature(int index) {
		return getFeatures().get(index);
	}
}
