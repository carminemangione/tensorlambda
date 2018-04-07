package com.mangione.continuous.datagenerators;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.random.RandomGenerator;

public class RandomUnitVector {
    private final int numberOfDimensions;
    private final RandomGenerator randomGenerator;

    public RandomUnitVector(int numberOfDimensions, RandomGenerator randomGenerator) {
        this.numberOfDimensions = numberOfDimensions;
        this.randomGenerator = randomGenerator;
    }

    public double[] nextVector() {
        double[] vector = createAndFillVector();
        return new ArrayRealVector(vector).unitVector().toArray();
    }

    private double[] createAndFillVector() {
        double[] vector = new double[numberOfDimensions];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = 1.0 - 2.0 * randomGenerator.nextDouble();
        }
        return vector;
    }
}
