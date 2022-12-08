package com.mangione.continuous.classifiers.unsupervised.smile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.datagenerators.categoric.DiscreteRandomSparseExemplar;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;

public class ClusterArrayToClustersTest {

	private Random random;

	@Before
	public void setUp() {
		random = new Random(98272);
	}

	@Test
	public void fourClusters() {
		int[] clustersIndexes = {0, 0, 3, 2, 4, 3, 2, 4, 1};

		List<ExemplarInterface<Integer, Integer>> exemplars = IntStream.range(0, clustersIndexes.length)
				.boxed()
				.map(i -> new DiscreteRandomSparseExemplar(10, 1, random))
				.collect(Collectors.toList());

		ClusterArrayToClusters<ExemplarInterface<Integer, Integer>> toClusters = new ClusterArrayToClusters<>(
				new ListObservationProvider<>(exemplars), clustersIndexes);
		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = toClusters.getClusters();
		assertEquals(5, clusters.size());

		validateCluster(exemplars, clusters, new int[]{0, 1}, 0);
		validateCluster(exemplars, clusters, new int[]{8}, 1);
		validateCluster(exemplars, clusters, new int[]{3, 6}, 2);
		validateCluster(exemplars, clusters, new int[]{2, 5}, 3);
		validateCluster(exemplars, clusters, new int[]{4, 7}, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void lengthOfIndexesAndExemplarsDoNotMatch()  {
		new ClusterArrayToClusters<>(new ListObservationProvider<>(
				Collections.singletonList(new DiscreteRandomSparseExemplar(10, 1, random))), new int[]{1,2});
	}

	private void validateCluster(List<ExemplarInterface<Integer, Integer>> exemplars, List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters, int[] clusterMembership, int clusterIndex) {
		List<ExemplarInterface<Integer, Integer>> clusterExemplars = clusters.get(clusterIndex).getObservations();
		assertEquals(clusterMembership.length, clusterExemplars.size());
		for (int index : clusterMembership) {
			assertTrue(clusterExemplars.contains(exemplars.get(index)));
		}
	}


}