package com.mangione.continuous.observations;

import java.util.Arrays;

public class DiscreteExemplarFactory implements ExemplarFactoryInterface<Double, Integer, DiscreteExemplar<Double>> {
    @Override
    public DiscreteExemplar<Double> create(Double[] data) {
        return new DiscreteExemplar<>(Arrays.copyOfRange(data, 0, data.length - 1), data[data.length - 1],
                data[data.length - 1].intValue());
    }
}
