package com.mangione.continuous.classifiers.unsupervised.smile;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.datagenerators.categoric.DiscreteClusterGenerator;
import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class KModesClusterFactoryTest {
	private int numClusters;
	private int numDimensions;
	private int clusterSpread;
	private int numPointsInCluster;

	@Before
	public void setUp() {
		numClusters = 4;
		numDimensions = 40;
		clusterSpread = 2;
		numPointsInCluster = 100;
	}

	@Test
	public void cluster() {
		KModesClusterFactory<ObservationInterface<Integer>, ObservationProviderInterface<Integer, ObservationInterface<Integer>>> factory = new KModesClusterFactory<>(4);
		DiscreteClusterGenerator discreteClusterGenerator = new DiscreteClusterGenerator(
				numPointsInCluster, numClusters, numDimensions, clusterSpread, new Random(33452));

		factory.cluster(new ArrayObservationProvider<>(
				discreteClusterGenerator.generateClusters(), Observation::new));
		List<Cluster<Integer, ObservationInterface<Integer>>> clusters = factory.getClusters();
		assertEquals(4, clusters.size());
		for (Cluster<Integer, ObservationInterface<Integer>> cluster : clusters) {
			assertEquals(100., cluster.getObservations().size(), 8.0);
		}
	}

	@Test
	public void clusterSparseArray() {
		KModesClusterFactory<ObservationInterface<Integer>, ObservationProviderInterface<Integer, ObservationInterface<Integer>>> factory = new KModesClusterFactory<>(4);
		DiscreteClusterGenerator discreteClusterGenerator = new DiscreteClusterGenerator(
				numPointsInCluster, numClusters, numDimensions, clusterSpread, new Random(33452));

		Integer[][] clusterPoints = discreteClusterGenerator.generateClusters();
		List<ArrayList<Integer>> list = Arrays.stream(clusterPoints)
				.map(x -> new ArrayList<>(Arrays.asList(x))).toList();

		list.forEach(obs->obs.addAll(Arrays.asList(0, 0, 0)));
		List<Integer[]> sparse = list.stream()
				.map(l -> l.toArray(new Integer[0]))
				.toList();

		factory.cluster(new ListObservationProvider<>(
				sparse.stream().map(Observation::new).collect(Collectors.toList())));
		List<Cluster<Integer, ObservationInterface<Integer>>> clusters = factory.getClusters();
		assertEquals(4, clusters.size());
		for (Cluster<Integer, ObservationInterface<Integer>> cluster : clusters) {
			assertEquals(100., cluster.getObservations().size(), 8.0);
		}
	}

}