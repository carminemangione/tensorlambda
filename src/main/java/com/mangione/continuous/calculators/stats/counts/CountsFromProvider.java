package com.mangione.continuous.calculators.stats.counts;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class CountsFromProvider<S extends ObservationInterface<Integer>, T extends ObservationProviderInterface<Integer, S>,
        K> {

    private final Counts<K> counts;

    public CountsFromProvider(T provider, Function<S, K> keyGenerator) {
        counts = new Counts<>();
        for (S observation : provider) {
            counts.add(keyGenerator.apply(observation));
        }
    }

    public Counts<K> getCounts() {
        return counts;
    }
}
