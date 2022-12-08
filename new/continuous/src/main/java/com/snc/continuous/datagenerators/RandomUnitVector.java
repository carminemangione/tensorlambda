package com.mangione.continuous.datagenerators;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.random.RandomGenerator;

public class RandomUnitVector {

    private final RandomGaussianVector randomGaussianVector;

    public RandomUnitVector(int numberOfDimensions, RandomGenerator randomGenerator) {
        this.randomGaussianVector = new RandomGaussianVector(numberOfDimensions,0.0,1.0,false,
                randomGenerator);
    }

    public double[] nextVector() {
        double[] vector = this.randomGaussianVector.nextVector();
        return new ArrayRealVector(vector).unitVector().toArray();
    }
}
