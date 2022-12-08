package com.mangione.continuous.classifiers.unsupervised.smile;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

class ClusterArrayToClusters<OBSERVATION extends ObservationInterface<Integer>> {

	private final List<Cluster<Integer, OBSERVATION>> clusters;
	private final Iterator<OBSERVATION> iterator;


	ClusterArrayToClusters(ObservationProviderInterface<Integer, OBSERVATION> exemplars, int[] clusterIndexes) {
		iterator = exemplars.iterator();
		if (clusterIndexes.length != exemplars.size())
			throw new IllegalArgumentException(String.format(
					"Length of exemplars and cluster index must be the same. Currently exemplars: %d, clusterIndexes: %d",
					exemplars.size(), clusterIndexes.length));
		clusters = IntStream.range(0, clusterIndexes.length)
				.boxed()
				.map(index->clusterIndexes[index])
				.map(this::getClusterObservation)
				.collect(Collectors.groupingBy(co -> co.clusterIndex, TreeMap::new, Collectors.toList()))
				.values()
				.stream()
				.map(this::createCluster)
				.collect(Collectors.toList());
	}

	List<Cluster<Integer, OBSERVATION>> getClusters() {
		return clusters;
	}

	private ClusterObservation getClusterObservation(int clusterIndex) {
		if (!iterator.hasNext())
			throw new IllegalStateException("wrong number of iterators");
		return new ClusterObservation(clusterIndex, iterator.next());
	}

	private Cluster<Integer, OBSERVATION> createCluster(List<ClusterObservation> clusterObservations) {
		return new Cluster<>(0, clusterObservations.stream()
				.map(co->co.observation)
				.collect(Collectors.toList()));
	}

	private class ClusterObservation {
		private final int clusterIndex;
		private final OBSERVATION observation;

		private ClusterObservation(int clusterIndex, OBSERVATION observation) {
			this.clusterIndex = clusterIndex;
			this.observation = observation;
		}
	}
}
