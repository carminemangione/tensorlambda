package com.mangione.continuous.observations.dense;

import java.util.List;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class StringObservationFactory implements ObservationFactoryInterface<String, ObservationInterface<String>> {
	@Override
	public ObservationInterface<String> create(List<String> data, int[] columns) {
		return new Observation<>(data);
	}
}
