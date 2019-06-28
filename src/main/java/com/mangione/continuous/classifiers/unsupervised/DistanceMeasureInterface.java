package com.mangione.continuous.classifiers.unsupervised;


import com.mangione.continuous.observations.ObservationInterface;

public interface DistanceMeasureInterface<S extends Number, T extends ObservationInterface<S>> {
	double distanceToCentroid(double[] centroid, T observation);
}
