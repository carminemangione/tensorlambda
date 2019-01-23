package com.mangione.continuous.classifiers.unsupervised;

import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class KMeans implements DistanceMeasurer<double[]> {

	private final EuclideanDistance euclideanDistance = new EuclideanDistance();

	@Override
	public double distanceToCentroid(Cluster cluster, double[] observation) {
		return cluster.distanceToCentroid(observation);
	}

	@Override
	public void updateCentroid(Cluster<double[]> cluster) {
		List<double[]> observations = cluster.getObservations();
		if (observations.isEmpty()) {
            cluster.setCentroid(null);
        } else {
            double[] sumsOfDimensions = new double[cluster.getNumDimensions()];
            observations.forEach(x -> {
                for (int i = 0; i < sumsOfDimensions.length; i++) {
	                try {
		                sumsOfDimensions[i] += x[i] / observations.size();
	                } catch (Throwable e) {
						if(x == null)
							continue;
	                }
                }
            });
            cluster.setCentroid(sumsOfDimensions);
        }
    }

}
