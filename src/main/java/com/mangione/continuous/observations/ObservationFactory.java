package com.mangione.continuous.observations;

public class ObservationFactory implements ObservationFactoryInterface<Double, Observation<Double>> {
    @Override
    public Observation<Double> create(Double[] data) {
        return new Observation<>(data);
    }
}
