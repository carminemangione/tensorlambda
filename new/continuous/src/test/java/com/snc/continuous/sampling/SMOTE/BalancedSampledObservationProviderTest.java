package com.mangione.continuous.sampling.SMOTE;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.classifiers.unsupervised.ClusterFactoryInterface;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;

@Ignore
public class BalancedSampledObservationProviderTest {
	private Random random;

	@Before
	public void setUp() {
		random = new Random(3939);
	}

	@Test
	public void clustersProportionalWhenUpSampledAndRounded() {
		List<ExemplarInterface<Integer, Integer>> majority = ClustersFromProviderTest.generateExemplars(10, 1, random, 10);

		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = Arrays.asList(
				new Cluster<>(0, generateCluster(2, false)),
				new Cluster<>(0, generateCluster(4, true)));

		List<ExemplarInterface<Integer, Integer>> allExemplars = clusters.stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		allExemplars.addAll(majority);

		BalancedSampledObservationProvider<ExemplarInterface<Integer, Integer>> provider = new BalancedSampledObservationProvider<>(
				new ListObservationProvider<>(allExemplars), ExemplarInterface::getLabel, new ClusterFactory(clusters),
				(features, columns, numberOfColumns, original) ->
						new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel()),
				3345);

		assertEquals(20, provider.size());
		assertEquals(10, provider.getStream().filter(majority::contains).count());
		assertEquals(10, provider.getStream().filter(x -> x.getLabel() == 1).count());
		assertEquals(10, provider.getStream().filter(x -> x.getLabel() == 0).count());
		assertEquals(3, provider.getStream().filter(ex -> ex.getLabel() == 0 && inCluster(false, ex)).count(), 0);
		assertEquals(7, provider.getStream().filter(ex -> ex.getLabel() == 0 && inCluster(true, ex)).count(), 0);
	}

	@Test
	public void allMinorityExemplarsConserved() {
		List<ExemplarInterface<Integer, Integer>> majority = ClustersFromProviderTest.generateExemplars(10, 1, random, 10);

		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = Arrays.asList(
				new Cluster<>(0, generateCluster(2, false)),
				new Cluster<>(0, generateCluster(4, true)));

		List<ExemplarInterface<Integer, Integer>> minorityExemplars = clusters.stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		List<ExemplarInterface<Integer, Integer>> allExemplars = new ArrayList<>(minorityExemplars);
		allExemplars.addAll(majority);

		BalancedSampledObservationProvider<ExemplarInterface<Integer, Integer>> provider = new BalancedSampledObservationProvider<>(
				new ListObservationProvider<>(allExemplars), ExemplarInterface::getLabel, new ClusterFactory(clusters),
				(features, columns, numberOfColumns, original) ->
						new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel()),
				3345);

		assertEquals(14, provider.getStream().filter(ex -> !containsReference(minorityExemplars, ex)).count());
	}

	@Test
	public void downSampleMajorityAndUpSampleMinority() {
		List<ExemplarInterface<Integer, Integer>> majority = ClustersFromProviderTest.generateExemplars(1000, 1, random, 10);

		List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters = Arrays.asList(
				new Cluster<>(0, generateCluster(2, false)),
				new Cluster<>(0, generateCluster(4, true)));

		List<ExemplarInterface<Integer, Integer>> allExemplars = clusters.stream()
				.map(Cluster::getObservations)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		allExemplars.addAll(majority);

		BalancedSampledObservationProvider<ExemplarInterface<Integer, Integer>> provider = new BalancedSampledObservationProvider<>(
				new ListObservationProvider<>(allExemplars), ExemplarInterface::getLabel, new ClusterFactory(clusters),
				(features, columns, numberOfColumns, original) ->
						new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel()),
				10,
				3345);

		assertEquals(20, provider.size());
		assertEquals(10, provider.getStream().filter(majority::contains).count());
		assertEquals(10, provider.getStream().filter(x -> x.getLabel() == 1).count());
		assertEquals(10, provider.getStream().filter(x -> x.getLabel() == 0).count());
		assertEquals(3, provider.getStream().filter(ex -> ex.getLabel() == 0 && inCluster(false, ex)).count(), 0);
		assertEquals(7, provider.getStream().filter(ex -> ex.getLabel() == 0 && inCluster(true, ex)).count(), 0);
	}

	List<ExemplarInterface<Integer, Integer>> generateCluster(int numExemplars, boolean high) {
		int startIndex = high ? 15 : 0;
		return IntStream.range(0, numExemplars)
				.boxed()
				.map(i -> getExemplar(startIndex))
				.collect(Collectors.toList());
	}

	private boolean containsReference(List<ExemplarInterface<Integer, Integer>> exemplars, ExemplarInterface<Integer, Integer> exemplar) {
		boolean containsReference = false;
		for (int i = 0; i < exemplars.size() && !containsReference; i++) {
			containsReference = exemplars.get(i) == exemplar;
		}
		return containsReference;
	}

	@Nonnull
	private ExemplarInterface<Integer, Integer> getExemplar(int startIndex) {
		int numToSet = random.nextInt(4);
		SparseExemplarBuilder<Integer, Integer> exemplar = new SparseExemplarBuilder<>(20, 0, 0);
		IntStream.range(startIndex, startIndex + 5).forEach(i -> exemplar.setFeature(i, 1));
		for (int i = 0; i < numToSet; i++)
			exemplar.setFeature(startIndex + random.nextInt(5), 0);
		return exemplar.build(Integer[]::new);
	}

	private boolean inCluster(boolean high, ExemplarInterface<Integer, Integer> exemplar) {
		int startIndex = high ? 15 : 0;
		boolean inCluster = false;
		for (int i = 0; i < 5 && !inCluster; i++) {
			inCluster = exemplar.getFeature(i + startIndex) == 1;
		}
		return inCluster;
	}

	private static class ClusterFactory implements ClusterFactoryInterface<ExemplarInterface<Integer, Integer>,
			ObservationProviderInterface<Integer, ExemplarInterface<Integer, Integer>>> {
		private final List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters;

		private ClusterFactory(List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> clusters) {
			this.clusters = clusters;
		}

		@Override
		public void cluster(ObservationProviderInterface<Integer, ExemplarInterface<Integer, Integer>> provider) {
		}

		@Override
		public List<Cluster<Integer, ExemplarInterface<Integer, Integer>>> getClusters() {
			return clusters;
		}
	}


}