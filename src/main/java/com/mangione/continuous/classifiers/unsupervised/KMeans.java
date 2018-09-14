package com.mangione.continuous.classifiers.unsupervised;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class KMeans implements DistanceMeasurer {

	private final EuclideanDistance euclideanDistance = new EuclideanDistance();

	@Override
	public double distanceToCentroid(Cluster cluster, double[] observation) {
		return cluster.distanceToCentroid(observation);
	}

	@Override
	public void updateCentroid(Cluster cluster) {
		cluster.updateCentroid();
	}

}
