package com.mangione.continuous.classifiers.unsupervised.smile;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.datagenerators.categoric.DiscreteClusterGenerator;
import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.dense.Observation;

public class HACClusterFactoryTest {
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
		HACClusterFactory<Observation<Integer>> hacCluster = new HACClusterFactory<>(4);
		DiscreteClusterGenerator discreteClusterGenerator = new DiscreteClusterGenerator(
				numPointsInCluster, numClusters, numDimensions, clusterSpread, new Random(33452));

		hacCluster.cluster(new ArrayObservationProvider<>(
				discreteClusterGenerator.generateClusters(), Observation::new));
		List<Cluster<Integer, Observation<Integer>>> clusters = hacCluster.getClusters();
		assertEquals(4, clusters.size());
	}
}