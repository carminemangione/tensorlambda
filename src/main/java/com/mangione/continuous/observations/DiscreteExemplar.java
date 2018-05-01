package com.mangione.continuous.observations;

import java.util.Arrays;

public class DiscreteExemplar<T> implements ExemplarInterface<T, Integer> {
    private final T[] features;
    private final T continuousValue;
    private final Integer target;
    
    public DiscreteExemplar(T[] features, T continuousValue, Integer target) {
        this.features = features;
        this.continuousValue = continuousValue;
        this.target = target;
    }

    @Override
    public Integer getTarget() {
        return target;
    }

    public T getContinuousValue() {
        return continuousValue;
    }

    @Override
    public T[] getExemplar() {
        return Arrays.copyOfRange(features, 0, features.length -1);
    }

    @Override
    public T[] getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "DiscreteExemplar{" +
                "target=" + target +
                ", continuousValue=" + continuousValue +
                '}';
    }
}
