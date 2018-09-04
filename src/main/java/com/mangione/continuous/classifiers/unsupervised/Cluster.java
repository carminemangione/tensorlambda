package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;


public class Cluster {
    private final int numDimensions;
    private double[] centroid;
    private final List<double[]> observations = new ArrayList<>();
    private final EuclideanDistance euclideanDistance = new EuclideanDistance();

    public Cluster(int numDimensions) {

        this.numDimensions = numDimensions;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void add(double[] observation) {
        observations.add(observation);
       // updateCentroid();
    }

    public List<double[]> getObservations() {
        return new ArrayList<>(observations);
    }

    public void remove(double[] observation) {
        observations.remove(observation);
       // updateCentroid();
    }

    public double distanceToCentroid(double[] observation) {
        return euclideanDistance.compute(centroid, observation);
    }

    public double withinClusterSumOfSquares() {
        final double[] sumOfSquares = {0};
        observations.forEach(x->
                sumOfSquares[0] +=
                        Math.pow(euclideanDistance.compute(centroid, x), 2));
        return sumOfSquares[0];
    }

    public void updateCentroid() {
        if (observations.isEmpty()) {
            centroid = null;
        } else {
            double[] sumsOfDimensions = new double[numDimensions];
            observations.forEach(x -> {
                for (int i = 0; i < sumsOfDimensions.length; i++) {
                    sumsOfDimensions[i] += x[i] / observations.size();
                }
            });
            centroid = sumsOfDimensions;
        }
    }
}
