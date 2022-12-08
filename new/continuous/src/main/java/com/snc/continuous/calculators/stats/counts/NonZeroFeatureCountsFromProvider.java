package com.mangione.continuous.calculators.stats.counts;

import java.util.List;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class NonZeroFeatureCountsFromProvider<FEATURE extends Number, OBSERVATION extends ObservationInterface<FEATURE>,
		PROVIDER extends ObservationProviderInterface<FEATURE, OBSERVATION>, K> {

	private final List<Count<K>> counts;

	public NonZeroFeatureCountsFromProvider(PROVIDER provider, Function<OBSERVATION, K> keyFunction) {
		this.counts = new CountsFromProvider<>(provider, keyFunction).getCounts();
	}

	public List<Count<K>> getCounts() {
		return counts;
	}


}
