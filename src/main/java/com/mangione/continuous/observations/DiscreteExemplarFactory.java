package com.mangione.continuous.observations;

import java.util.Arrays;

public class DiscreteExemplarFactory implements ObservationFactoryInterface<DiscreteExemplar> {
    @Override
    public DiscreteExemplar create(double[] data) {
        return new DiscreteExemplar(Arrays.copyOfRange(data, 0, data.length - 1), data[data.length - 1],
                (int)data[data.length - 1]);
    }
}
