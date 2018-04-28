package com.mangione.continuous.observations;

public class ObservationFactory<S> implements ObservationFactoryInterface<S, Observation<S>> {
    @Override
    public Observation<S> create(S[] data) {
        return new Observation<>(data);
    }
}
