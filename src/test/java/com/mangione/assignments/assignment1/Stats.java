package com.mangione.assignments.assignment1;

public class Stats {

    private final double mean;
    private final double standardDeviation;

    public Stats(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
}
