package com.mangione.continuous.sampling.SMOTE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.datagenerators.categoric.DiscreteRandomSparseExemplar;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

@Ignore
public class ClustersFromProviderTest {
	private Random random;

	@Before
	public void setUp() {
		this.random = new Random(3597);
	}

	@Test
	public void minorityOnCountRatherThanValueRatherThanTag() {
		List<ExemplarInterface<Integer, Integer>> majority = generateExemplars(100, 0, random, 100);

		int[] pointCount = {10, 20};
		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = IntStream.range(0, pointCount.length)
				.boxed()
				.map(i -> generateExemplars(pointCount[i], 1, random, 100))
				.map(x -> new Cluster<>(0, x))
				.collect(Collectors.toList());
		ClusterFactory factory = new ClusterFactory(clusters);

		List<ExemplarInterface<Integer, Integer>> allExemplars = clusters.stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		allExemplars.addAll(majority);

		ClustersFromProvider<ExemplarInterface<Integer, Integer>> balancedClusters = new ClustersFromProvider<>(
				new ListObservationProvider<>(allExemplars),
				ExemplarInterface::getLabel, factory);

		assertEquals(0, (long)balancedClusters.getMajorityObservations().get(0).getLabel());
	}


	@Test
	public void createBalancedClusters() {
		List<ExemplarInterface<Integer, Integer>> majority = generateExemplars(100, 1, random, 100);

		int[] pointCount = {10, 20};
		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = IntStream.range(0, pointCount.length)
				.boxed()
				.map(i -> generateExemplars(pointCount[i], 0, random, 100))
				.map(x -> new Cluster<>(0, x))
				.collect(Collectors.toList());
		ClusterFactory factory = new ClusterFactory(clusters);

		List<ExemplarInterface<Integer, Integer>> allExemplars = clusters.stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		allExemplars.addAll(majority);

		ClustersFromProvider<ExemplarInterface<Integer, Integer>> balancedClusters = new ClustersFromProvider<>(
				new ListObservationProvider<>(allExemplars), ExemplarInterface::getLabel, factory);

		assertEquals(10, balancedClusters.getClusters().get(0).getObservations().size());
		assertEquals(20, balancedClusters.getClusters().get(1).getObservations().size());
		assertEquals(100, balancedClusters.getMajorityObservations().size());
	}

	static List<ExemplarInterface<Integer, Integer>> generateExemplars(int num, int tag, Random random, int numDimensions) {
		return IntStream.range(0, num)
				.boxed()
				.map(x->new DiscreteRandomSparseExemplar(numDimensions, tag, random))
				.collect(Collectors.toList());
	}


	private static class ClusterFactory implements ClusterFactoryInterface<ExemplarInterface<Integer, Integer>, ObservationProviderInterface<Integer,ExemplarInterface<Integer, Integer>>> {
		private final List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters;
		private boolean clusterCalled;

		private ClusterFactory(List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters) {
			this.clusters = clusters;
		}

		@Override
		public void cluster(ObservationProviderInterface<Integer, ExemplarInterface<Integer, Integer>> provider) {
			clusterCalled = true;
		}

		@Override
		public List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> getClusters() {
			assertTrue(clusterCalled);
			return clusters;
		}
	}


}