package com.mangione.continuous.observations;

import java.util.List;

public class Observation<T> implements ObservationInterface<T> {
    private final List<T> features;

    public Observation(List<T> features) {
        this.features = features;
    }

    public List<T> getFeatures() {
        return features;
    }
}
