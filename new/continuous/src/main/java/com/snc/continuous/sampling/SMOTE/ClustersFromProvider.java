package com.mangione.continuous.sampling.SMOTE;


import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Streams;
import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.sampling.reservoir.ReservoirSamplerWithoutReplacement;

public class ClustersFromProvider<EXEMPLAR extends ObservationInterface<Integer>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClustersFromProvider.class);
	private final List<Cluster<Integer, EXEMPLAR>> clusters;
	private final List<EXEMPLAR> majorityObservations;

	ClustersFromProvider(ObservationProviderInterface<Integer, EXEMPLAR> provider, Function<EXEMPLAR, Integer> tagFunction,
			ClusterFactoryInterface<EXEMPLAR, ObservationProviderInterface<Integer,EXEMPLAR>> clusterFactory) {
		this(provider, tagFunction, clusterFactory, -1, null);
	}

	ClustersFromProvider(ObservationProviderInterface<Integer, EXEMPLAR> provider,
			Function<EXEMPLAR, Integer> tagFunction, ClusterFactoryInterface<EXEMPLAR, ObservationProviderInterface<Integer,EXEMPLAR>> clusterFactory, int numMajorityDesired,
			MersenneTwister random) {

		LOGGER.info("Calculating minority class");
		TreeMap<Integer, List<EXEMPLAR>> countsOnClasses = findCountsOnClasses(provider, tagFunction);
		if (countsOnClasses.size() > 2)
			throw new RuntimeException("Too many classes, only binary classes currently supported");

		int minority = countsOnClasses.lastKey();
		int majority = countsOnClasses.firstKey();

		LOGGER.info("Filtering majority observations");
		List<EXEMPLAR> allMajorityObservations = countsOnClasses.get(majority);
		if (numMajorityDesired > 0)
			majorityObservations = new ReservoirSamplerWithoutReplacement<>(allMajorityObservations.iterator(),
					Math.min(numMajorityDesired, allMajorityObservations.size()), random).sample();
		else
			majorityObservations = allMajorityObservations;
		LOGGER.info("Filtering minority observations");
		List<EXEMPLAR> minorityExemplars = countsOnClasses.get(minority);
		LOGGER.info("Clustering...");
		clusterFactory.cluster(new ListObservationProvider<>(minorityExemplars));
		clusters = clusterFactory.getClusters();
	}

	public List<Cluster<Integer, EXEMPLAR>> getClusters() {
		return clusters;
	}

	@Nonnull
	private TreeMap<Integer, List<EXEMPLAR>> findCountsOnClasses(ObservationProviderInterface<Integer, EXEMPLAR> provider,
			Function<EXEMPLAR, Integer> tagFunction) {
		return new TreeMap<>(Streams.stream(provider)
				.collect(Collectors.groupingBy(tagFunction)));
	}

	public List<EXEMPLAR> getMajorityObservations() {
		return majorityObservations;
	}
}
