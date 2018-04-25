package com.mangione.continuous.datagenerators;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.random.RandomGenerator;

public class RandomUnitVector {
    private final int numberOfDimensions;
    private final RandomGenerator randomGenerator;
    private final RandomGaussianVector randomGaussianVector;

    public RandomUnitVector(int numberOfDimensions, RandomGenerator randomGenerator) {
        this.numberOfDimensions = numberOfDimensions;
        this.randomGenerator = randomGenerator;
        this.randomGaussianVector = new RandomGaussianVector(numberOfDimensions,0.0,1.0,false,randomGenerator);
    }

    public double[] nextVector() {
        double[] vector = this.randomGaussianVector.nextVector();
        return new ArrayRealVector(vector).unitVector().toArray();
    }

    // This is wrong. It's not UNIFORMLY distributed on the sphere.
    private double[] createAndFillVector() {
        double[] vector = new double[numberOfDimensions];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = 1.0 - 2.0 * randomGenerator.nextDouble();
        }
        return vector;
    }
}
