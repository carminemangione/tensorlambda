package com.mangione.continuous.observations;

public interface ObservationFactoryInterface<S, T extends ObservationInterface<S>> {
    T create(S[] data);
}
