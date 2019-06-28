package com.mangione.continuous.classifiers.unsupervised;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.mangione.continuous.observations.ObservationInterface;

public class KMeansDistanceMeasure<S extends Number, T extends ObservationInterface<S>> implements DistanceMeasureInterface<S, T> {

	private final EuclideanDistance euclideanDistance = new EuclideanDistance();

	@Override
	public double distanceToCentroid(double[] centroid, T observation) {
		return euclideanDistance.compute(centroid, toDoubleArray(observation));
	}

	private double[] toDoubleArray(T observation) {
		return observation.getFeatures().stream().mapToDouble(Number::doubleValue).toArray();
	}
}
