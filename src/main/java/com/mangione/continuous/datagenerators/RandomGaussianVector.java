package com.mangione.continuous.datagenerators;

import org.apache.commons.math3.random.RandomGenerator;

public class RandomGaussianVector {
    private final int numberOfDimensions;
    private final double mean;
    private final double sd;
    private final boolean includeBias;
    private final RandomGenerator random;

    public RandomGaussianVector(int numberOfDimensions, double mean, double sd, boolean includeBias, RandomGenerator random) {
        this.numberOfDimensions = numberOfDimensions;
        this.mean = mean;
        this.sd = sd;
        this.includeBias = includeBias;
        this.random = random;
    }

    public double[] nextVector()  {
        int currentIndex;
        double[] nextVector;

        if (includeBias) {
            nextVector = new double[numberOfDimensions + 1];
            nextVector[0] = 1;
            currentIndex = 1;
        } else {
            nextVector = new double[numberOfDimensions];
            currentIndex = 0;
        }

        for (int i = currentIndex; i < nextVector.length; i++) {
            nextVector[i] = random.nextGaussian() * sd + mean;
        }
        return nextVector;
    }
}
