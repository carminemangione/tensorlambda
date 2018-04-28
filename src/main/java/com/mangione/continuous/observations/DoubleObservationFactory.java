package com.mangione.continuous.observations;

public class DoubleObservationFactory<S> implements ObservationFactoryInterface<S, ObservationInterface<S>> {
    @Override
    public Observation<S> create(S[] data) {
        return new Observation<>(data);
    }
}
