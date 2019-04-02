package com.mangione.continuous.calculators.stats;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class CountsFromProvider<S extends ObservationInterface<Integer>, T extends ObservationProviderInterface<Integer, S>> {

    private final Counts counts;

    public CountsFromProvider(T provider, Function<S, String> keyGenerator) {
        counts = new Counts();
        for (S observation : provider) {
            counts.add(keyGenerator.apply(observation));
        }
    }

    public Counts getCounts() {
        return counts;
    }
}
