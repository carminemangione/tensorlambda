package com.mangione.continuous.classifiers.unsupervised.nearestneighbor;


import java.util.List;

import com.mangione.continuous.observations.dense.Observation;


public interface KMeansListener<T extends Observation> {
	void reassignmentCompleted(List<Cluster> currentClusters);
}
