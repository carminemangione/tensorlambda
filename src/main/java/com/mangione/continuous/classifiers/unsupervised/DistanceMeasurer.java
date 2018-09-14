package com.mangione.continuous.classifiers.unsupervised;

import org.apache.commons.math3.random.MersenneTwister;

public interface DistanceMeasurer {
	double distanceToCentroid(Cluster cluster, double[] observation);
	void updateCentroid(Cluster cluster);

}
