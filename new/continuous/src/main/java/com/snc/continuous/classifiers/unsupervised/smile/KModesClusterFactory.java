package com.mangione.continuous.classifiers.unsupervised.smile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import smile.clustering.KModes;

public class KModesClusterFactory<OBSERVATION extends ObservationInterface<Integer>, PROVIDER extends  ObservationProviderInterface<Integer, OBSERVATION>>
		implements ClusterFactoryInterface<OBSERVATION,PROVIDER> {
	private final int k;
	private List<Cluster<Integer, OBSERVATION>> clusters;
	private double distortion;

	public KModesClusterFactory(int k) {
		this.k = k;
	}

	@Override
	public void cluster(PROVIDER provider) {
		SparseToDenseFromProvider<OBSERVATION> denseFromSparse = new SparseToDenseFromProvider<>(provider);

		int[][] data = Streams.stream(provider)
				.map(denseFromSparse::process)
				.collect(Collectors.toList())
				.toArray(new int[0][0]);
		KModes fit = KModes.fit(data, k);
		distortion = fit.distortion;

		int[] clustersIndexes = Arrays.stream(data)
				.map(fit::predict)
				.mapToInt(Integer::intValue)
				.toArray();

		ListObservationProvider<Integer, OBSERVATION> listObservationProvider
				= new ListObservationProvider<>(
						Streams.stream(provider)
								.collect(Collectors.toList()));
		clusters = new ClusterArrayToClusters<>(listObservationProvider, clustersIndexes).getClusters();
	}

	@Override
	public List<Cluster<Integer, OBSERVATION>> getClusters() {
		return clusters;
	}

	public double getDistortion() {
		return distortion;
	}
}
