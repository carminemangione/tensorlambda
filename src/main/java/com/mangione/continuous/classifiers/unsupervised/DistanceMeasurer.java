package com.mangione.continuous.classifiers.unsupervised;


public interface DistanceMeasurer<S> {
	double distanceToCentroid(Cluster<? extends S> cluster, S observation);
	void updateCentroid(Cluster<S> cluster);

}
