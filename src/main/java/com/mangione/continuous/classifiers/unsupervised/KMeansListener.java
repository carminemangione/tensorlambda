package com.mangione.continuous.classifiers.unsupervised;


import java.util.List;

import com.mangione.continuous.observations.Observation;


public interface KMeansListener<T extends Observation> {
	void reassignmentCompleted(List<Cluster> currentClusters);
}
