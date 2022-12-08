package com.mangione.continuous.classifiers.unsupervised.smile;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import smile.clustering.HierarchicalClustering;
import smile.clustering.linkage.CompleteLinkage;
import smile.clustering.linkage.Linkage;

public class HACClusterFactory<OBSERVATION extends ObservationInterface<Integer>> implements ClusterFactoryInterface<OBSERVATION, ObservationProviderInterface<Integer, OBSERVATION>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(HACClusterFactory.class);
	private final int numClusters;
	private List<Cluster<Integer, OBSERVATION>> clusters;

	public HACClusterFactory(int numClusters) {
		this.numClusters = numClusters;
	}

	@Override
	public void cluster(ObservationProviderInterface<Integer, OBSERVATION> provider) {
		int numObs = provider.size();
		SparseHammingDistance<OBSERVATION> hammingDistance = new SparseHammingDistance<>();
		ListObservationProvider<Integer, OBSERVATION> listProvider = new ListObservationProvider<>(provider);
		double[][] proximity = createProximityMatrix(numObs, hammingDistance, listProvider);
		Linkage linkage = new CompleteLinkage(proximity);
		LOGGER.info(String.format("Clustering %d points.", proximity.length));
		HierarchicalClustering hc = HierarchicalClustering.fit(linkage);
		int[] partition = hc.partition(numClusters);
		LOGGER.info(String.format("Finished clustering %d points.", proximity.length));
		clusters = new ClusterArrayToClusters<>(listProvider, partition).getClusters();
	}

	@Override
	public List<Cluster<Integer, OBSERVATION>> getClusters() {
		return clusters;
	}

	private double[][] createProximityMatrix(int numObs, SparseHammingDistance<OBSERVATION> hammingDistance, ListObservationProvider<Integer, OBSERVATION> listProvider) {
		long time = System.currentTimeMillis();
		LOGGER.info("Calculating proximity matrix. ");
		double[][] proximity = new double[numObs][];
		for (int i = 0; i < numObs; i++) {
			proximity[i] = new double[i+1];
			for (int j = 0; j < i; j++) {
				proximity[i][j] = hammingDistance.calculateDistance(listProvider.getByIndex(i), listProvider.getByIndex(j));
			}
		}
		LOGGER.info(String.format("Finished calculating in %d seconds", System.currentTimeMillis() - time));
		return proximity;
	}
}
