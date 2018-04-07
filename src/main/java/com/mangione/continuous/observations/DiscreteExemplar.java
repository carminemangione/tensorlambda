package com.mangione.continuous.observations;

import java.util.Arrays;

public class DiscreteExemplar extends Observation {
    private final double continuousValue;
    private final int target;

    public DiscreteExemplar(double[] features, double continuousValue, int target) {
        super(features);
        this.continuousValue = continuousValue;
        this.target = target;
    }

    public DiscreteExemplar(double[] features) {
        super(Arrays.copyOfRange(features, 0, features.length - 1));
        this.continuousValue = features[features.length - 1];
        this.target = (int)features[features.length - 1];
    }

    public int getTarget() {
        return target;
    }

    public double getContinuousValue() {
        return continuousValue;
    }

    @Override
    public String toString() {
        return "DiscreteExemplar{" +
                "target=" + target +
                ", continuousValue=" + continuousValue +
                '}';
    }
}
