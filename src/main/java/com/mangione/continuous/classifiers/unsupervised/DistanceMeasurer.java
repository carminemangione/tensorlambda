package com.mangione.continuous.classifiers.unsupervised;


import com.mangione.continuous.observations.ObservationInterface;

public interface DistanceMeasurer<S extends Number, T extends ObservationInterface<S>> {
	double distanceToCentroid(Cluster<S, T> cluster, T observation);
}
