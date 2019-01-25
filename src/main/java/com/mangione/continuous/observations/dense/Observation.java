package com.mangione.continuous.observations.dense;

import java.util.List;

import com.mangione.continuous.observations.ObservationInterface;

public class Observation<T> implements ObservationInterface<T> {
    private final List<T> features;

    public Observation(List<T> features) {
        this.features = features;
    }

    public List<T> getFeatures() {
        return features;
    }

    @Override
    public List<T> getAllColumns() {
        return features;
    }
}
