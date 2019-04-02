package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observations.ObservationInterface;

public interface SparseObservationInterface<T> extends ObservationInterface<T> {
	void setFeature(int index, T value);

}
