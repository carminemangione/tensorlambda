package com.mangione.continuous.classifiers.unsupervised;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.mangione.continuous.observations.ObservationInterface;


public class Cluster<T extends Number, S extends ObservationInterface<T>> {

	private final int numDimensions;
	private double[] centroid;
	private final Set<S> observations = new HashSet<>();
	private final EuclideanDistance euclideanDistance = new EuclideanDistance();

	public Cluster(int numDimensions) {
		this.numDimensions = numDimensions;
	}

	@SuppressWarnings("WeakerAccess")
	public double[] getCentroid() {
		return centroid;
	}

	public void add(S observation) {
		observations.add(observation);
	}

	public Set<S> getObservations() {
		return observations;
	}

	public void remove(S observation) {
		observations.remove(observation);
	}

	public double distanceToCentroid(S observation) {
		try {
			return euclideanDistance.compute(centroid, toDoubleArray(observation));
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(0);
		}
		return 0.0;
	}

	private double[] toDoubleArray(S observation) {
		return observation.getFeatures().stream().mapToDouble(Number::doubleValue).toArray();
	}

	@Override
	public String toString() {
		return observations.size() + "";
	}

	void updateCentroid() {
		centroid = new double[numDimensions];
		observations.forEach(this::addToCentroid);
		centroid = Arrays.stream(centroid)
				.map(point -> point / observations.size())
				.toArray();
	}

	private void addToCentroid(S observation) {
		for (int i = 0; i < numDimensions; i++)
			centroid[i] = centroid[i] + observation.getFeature(i).doubleValue();
	}

	int getNumDimensions() {
		return numDimensions;
	}
}
