package com.mangione.continuous.classifiers.unsupervised;

import java.util.List;

import com.mangione.continuous.observations.ObservationInterface;

public class Cluster<FEATURE extends Number, OBSERVATION extends ObservationInterface<FEATURE>> {
	private final double dissimilarity;
	private final List<OBSERVATION> observations;

	public Cluster(double dissimilarity, List<OBSERVATION> observations) {
		this.dissimilarity = dissimilarity;
		this.observations = observations;
	}

	public List<OBSERVATION> getObservations()  {
		return observations;
	}

	public double getDissimilarity() {
		return dissimilarity;
	}

	@Override
	public String toString() {
		return "Cluster{" +
				"dissimilarity=" + dissimilarity +
				", observations=" + observations.size() +
				'}';
	}
}
