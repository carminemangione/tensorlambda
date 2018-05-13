package com.mangione.continuous.observations;

import java.util.List;

public class DoubleObservationFactory implements ObservationFactoryInterface<Double, ObservationInterface<Double>> {


    @Override
    public ObservationInterface<Double> create(List<Double> data) {
        return  new Observation<>(data);
    }
}
