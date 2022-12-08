package com.mangione.continuous.classifiers.unsupervised;

import java.util.List;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public interface ClusterFactoryInterface<OBSERVATION extends ObservationInterface<Integer>,
		PROVIDER extends ObservationProviderInterface<Integer, OBSERVATION>> {
	void cluster(PROVIDER provider);
	List<Cluster<Integer, OBSERVATION>> getClusters();
}
