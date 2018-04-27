package com.mangione.continuous.observations;

public class Observation<T> extends Observation<S> {
    private final T[] features;

    public Observation(T[] features) {
        this.features = features;
    }

    public T[] getFeatures() {
        return features;
    }
}
