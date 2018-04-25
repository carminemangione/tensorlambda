package com.mangione.continuous.observations;

public abstract class Exemplar extends Observation {

    public Exemplar(double[] features) {
        super(features);
    }

    public abstract double getTarget();
}
