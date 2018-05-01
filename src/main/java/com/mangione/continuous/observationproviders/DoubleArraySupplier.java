package com.mangione.continuous.observationproviders;

public class DoubleArraySupplier implements ArraySupplier<Double> {
    @Override
    public Double[] get(int length) {
        return new Double[length];
    }
}
