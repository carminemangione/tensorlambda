package com.mangione.continuous.observations.sparse;

import java.util.List;

import com.mangione.continuous.observations.ObservationInterface;

public interface SparseObservationInterface<T> extends ObservationInterface<T> {
	List<Integer> getColumnIndexes();
	void setFeature(int index, T value);
}
