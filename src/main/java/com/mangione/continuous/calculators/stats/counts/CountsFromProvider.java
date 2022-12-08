package com.mangione.continuous.calculators.stats.counts;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;


public class CountsFromProvider<R extends Number, S extends ObservationInterface<R>, T extends ObservationProviderInterface<R, S>, K> {
    private final List<Count<K>> counts;

    public CountsFromProvider(T provider, Function<S, K> keyGenerator) {
        counts =  new TreeMap<>(Streams.stream(provider)
                .collect(Collectors.groupingBy(keyGenerator, Collectors.counting()))).entrySet()
                .stream()
                .map(this::createCount)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<Count<K>> getCounts() {
        return counts;
    }

    private Count<K> createCount(Map.Entry<K, Long> entry) {
        return new Count<>(entry.getKey(), entry.getValue().intValue());
    }
}

