package com.mangione.continuous.observations;

public class DoubleObservationFactory implements ObservationFactoryInterface<Double, ObservationInterface<Double>> {
    @Override
    public Observation<Double> create(Double[] data) {
        return new Observation<>(data);
    }
}
