package com.mangione.continuous.classifiers.unsupervised;


public interface DistanceMeasurer<S> {
	double distanceToCentroid(Cluster<S> cluster, S observation);
	void updateCentroid(Cluster<S> cluster);

}
