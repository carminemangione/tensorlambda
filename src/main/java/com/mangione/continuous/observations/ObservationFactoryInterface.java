package com.mangione.continuous.observations;

public interface ObservationFactoryInterface<S> {
    Observation<S> create(S[] data);
}
