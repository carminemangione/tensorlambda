package com.mangione.continuous.sampling.SMOTE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;

public class BalancedSampledObservationProvider<EXEMPLAR extends ExemplarInterface<Integer, Integer>> implements ObservationProviderInterface<Integer, EXEMPLAR> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BalancedSampledObservationProvider.class);
	private final ObservationProviderInterface<Integer, EXEMPLAR> wrappedProvider;
	private final Function<EXEMPLAR, Integer> tagFunction;
	private final SparseExemplarFactoryInterface<Integer, EXEMPLAR> observationFactory;
	private final MersenneTwister random;
	private final int numNeeded;
	private final int numMinority;

	public BalancedSampledObservationProvider(ObservationProviderInterface<Integer,EXEMPLAR> provider,
			Function<EXEMPLAR, Integer> balanceFunction,
			ClusterFactoryInterface<EXEMPLAR, ObservationProviderInterface<Integer,EXEMPLAR>> clusterFactory,
			SparseExemplarFactoryInterface<Integer, EXEMPLAR> observationFactory,
			int randomSeed) {
		this(provider, balanceFunction, clusterFactory, observationFactory, -1, randomSeed);
	}

	public BalancedSampledObservationProvider(ObservationProviderInterface<Integer,EXEMPLAR> provider,
			Function<EXEMPLAR, Integer> balanceFunction, ClusterFactoryInterface<EXEMPLAR, ObservationProviderInterface<Integer,EXEMPLAR>> clusterFactory,
			SparseExemplarFactoryInterface<Integer, EXEMPLAR> observationFactory, int numMajorityDesired, int randomSeed) {
		LOGGER.info("Generating balanced sample");
		this.tagFunction = balanceFunction;
		this.observationFactory = observationFactory;
		this.random = new MersenneTwister(randomSeed);

		LOGGER.info("Generating clusters");
		ClustersFromProvider<EXEMPLAR> smoteCluster = new ClustersFromProvider<>(provider, balanceFunction, clusterFactory, numMajorityDesired, random);
		List<EXEMPLAR> minorityExemplars = getNumberInClusters(clusterFactory);
		this.numMinority = minorityExemplars.size();
		this.numNeeded = smoteCluster.getMajorityObservations().size() - numMinority;

		List<EXEMPLAR> balancedMinorityObservations = flattenClustersToGetMinority(smoteCluster);
		List<EXEMPLAR> allObservations = new ArrayList<>(smoteCluster.getMajorityObservations());
		allObservations.addAll(balancedMinorityObservations);
		Collections.shuffle(allObservations, new RandomAdaptor(random));
		wrappedProvider = new ListObservationProvider<>(allObservations);
		LOGGER.info("Finished generating balanced sample.");
	}

	private List<EXEMPLAR> getNumberInClusters(ClusterFactoryInterface<EXEMPLAR, ObservationProviderInterface<Integer,EXEMPLAR>> clusterFactory) {
		return clusterFactory.getClusters().stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public Iterator<EXEMPLAR> iterator() {
		return wrappedProvider.iterator();
	}

	@Nonnull
	private List<EXEMPLAR> flattenClustersToGetMinority(ClustersFromProvider<EXEMPLAR> smoteCluster) {
		return smoteCluster
				.getClusters().stream()
				.map(this::balanceCluster)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	@Override
	public int size() {
		return wrappedProvider.size();
	}

	private List<EXEMPLAR> balanceCluster(Cluster<Integer, EXEMPLAR> cluster) {
		int targetNumber = (int) Math.round(((double)cluster.getObservations().size() / numMinority) * numNeeded);
		LOGGER.info(String.format("Generating smote balance of size %d for cluster with %d elements", targetNumber, cluster.getObservations().size()));
 		return new SmoteSamplerForCluster<>(cluster, tagFunction, targetNumber, observationFactory, new RandomAdaptor(random))
				.getSample();
	}
}
