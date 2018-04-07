package com.mangione.continuous.observations;

public interface ObservationFactoryInterface<T extends Observation> {
    T create(double[] data);
}
