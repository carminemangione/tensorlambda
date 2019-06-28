package com.mangione.continuous.classifiers.unsupervised;

import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.mangione.continuous.observations.ObservationInterface;

public class KMeans<S extends Number, T extends ObservationInterface<S>> implements DistanceMeasurer<S, T> {

	private final EuclideanDistance euclideanDistance = new EuclideanDistance();

	@Override
	public double distanceToCentroid(Cluster<S, T> cluster, T observation) {
		return 0; //euclideanDistance.compute();
	}
}
