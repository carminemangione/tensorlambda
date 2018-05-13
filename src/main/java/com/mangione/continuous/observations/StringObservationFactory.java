package com.mangione.continuous.observations;

import java.util.List;

public class StringObservationFactory implements ObservationFactoryInterface<String, ObservationInterface<String>> {
	@Override
	public ObservationInterface<String> create(List<String> data) {
		return new Observation<>(data);
	}
}
