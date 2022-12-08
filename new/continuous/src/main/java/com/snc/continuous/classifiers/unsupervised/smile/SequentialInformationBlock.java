package com.mangione.continuous.classifiers.unsupervised.smile;

import java.util.Arrays;
import java.util.List;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import smile.clustering.SIB;
import smile.util.SparseArray;

public class SequentialInformationBlock<OBSERVATION extends ObservationInterface<Integer>,
		PROVIDER extends ObservationProviderInterface<Integer, OBSERVATION>> implements ClusterFactoryInterface<OBSERVATION, PROVIDER> {

	private final int numClusters;
	private List<Cluster<Integer, OBSERVATION>> clusters;
	private double distortion;

	public SequentialInformationBlock(int numClusters) {
		this.numClusters = numClusters;
	}

	public double getDistortion() {
		return distortion;
	}

	@Override
	public void cluster(PROVIDER provider) {
		SparseArray[] sparseArrays = provider.getStream()
				.map(this::createSparseArray)
				.toArray(SparseArray[]::new);
		SIB sib = SIB.fit(sparseArrays, numClusters, 10000);
		distortion = sib.distortion;
		int[] clusters = Arrays.stream(sparseArrays)
				.mapToInt(sib::predict)
				.toArray();
		this.clusters = new ClusterArrayToClusters<>(provider, clusters).getClusters();
	}

	@Override
	public List<Cluster<Integer, OBSERVATION>> getClusters() {
		return clusters;
	}

	private SparseArray createSparseArray(OBSERVATION observation) {
		SparseArray sparseArray = new SparseArray(observation.getColumnIndexes().length);
		Arrays.stream(observation.getColumnIndexes()).forEach(index -> sparseArray.set(index, 1));
		return sparseArray;
	}
}
