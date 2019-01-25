package com.mangione.continuous.observations.dense;

import java.util.List;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class DoubleObservationFactory implements ObservationFactoryInterface<Double, ObservationInterface<Double>> {
    @Override
    public ObservationInterface<Double> create(List<Double> data) {
        return  new Observation<>(data);
    }
}
