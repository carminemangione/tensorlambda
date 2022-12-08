package com.mangione.continuous.sampling.SMOTE;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;

public class SmoteSamplerForClusterTest {
	private Random random;
	private SparseHammingDistance<SparseExemplar<Integer, Integer>> hammingDistance;
	private RandomDiscreteExemplarGenerator fRandomDiscreteExemplarGenerator;
	private SparseExemplarFactoryInterface<Integer, SparseExemplar<Integer, Integer>> exemplarFactory;

	@Before
	public void setUp() {
		random = new Random(383767);
		hammingDistance = new SparseHammingDistance<>();
		fRandomDiscreteExemplarGenerator = new RandomDiscreteExemplarGenerator(random);
		exemplarFactory = (features, columns, numberOfColumns, original) ->
				new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel());
	}

	@Test
	public void balancedSmoteTwoPointsMinority() {
		List<SparseExemplar<Integer, Integer>> clusterPoints = fRandomDiscreteExemplarGenerator.createExemplars(100, 2);

		Cluster<Integer, SparseExemplar<Integer, Integer>> cluster = new Cluster<>(0, clusterPoints);
		SmoteSamplerForCluster<SparseExemplar<Integer, Integer>> sampler = new SmoteSamplerForCluster<>(cluster, SparseExemplar::getLabel, 8,
				(features, columns, numberOfColumns, original) ->
						new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel()), random);
		List<SparseExemplar<Integer, Integer>> sampled = sampler.getSample();

		assertEquals("Correct number of clusterPoints", 10, sampled.size());
		assertEquals("Should all be of the clusterPoints class.",
				10, sampled.stream().filter(ex->ex.getLabel() == 0).count());
		sampled.removeAll(clusterPoints);
		for (SparseExemplar<Integer, Integer> synthetic : sampled) {
			validateSyntheticObs(clusterPoints.get(0), clusterPoints.get(1), synthetic);
		}
	}

	@Test
	public void manyMinorityClassRandomShuffle() {
		int numMajority = 100;
		int numMinority = 37;
		List<SparseExemplar<Integer, Integer>> clusterPoints = fRandomDiscreteExemplarGenerator.createExemplars(50, numMinority);
		Collections.shuffle(clusterPoints, random);

		Cluster<Integer, SparseExemplar<Integer, Integer>> cluster = new Cluster<>(0, clusterPoints);
		SmoteSamplerForCluster<SparseExemplar<Integer, Integer>> sampler = new SmoteSamplerForCluster<>(cluster, SparseExemplar::getLabel, 63,
				exemplarFactory, random);
		List<SparseExemplar<Integer, Integer>> sampled = new ArrayList<>(sampler.getSample());

		assertEquals("Correct number of clusterPoints", numMajority, sampled.size());
		assertEquals("Should all be of the clusterPoints class.",
				numMajority, sampled.stream().filter(ex->ex.getLabel() == 0).count());

		sampled.removeAll(clusterPoints);
		for (SparseExemplar<Integer, Integer> synthetic : sampled) {
			assertTrue(compareDistanceForAllMinorityCombos(synthetic, clusterPoints));
		}
	}

	@Test
	public void clusterOfOneIsNoOp() {
		List<SparseExemplar<Integer, Integer>> clusterPoints = fRandomDiscreteExemplarGenerator.createExemplars(50, 1);
		Cluster<Integer, SparseExemplar<Integer, Integer>> cluster = new Cluster<>(0, clusterPoints);
		SmoteSamplerForCluster<SparseExemplar<Integer, Integer>> sampler = new SmoteSamplerForCluster<>(cluster,
				SparseExemplar::getLabel, 63, exemplarFactory, random);
		assertEquals(1, sampler.getSample().size());
	}

	@Test
	public void clustersOfTheSameAreIgnored() {
		SparseExemplarBuilder<Integer, Integer> builder = new SparseExemplarBuilder<>(1000, 0, 10);
		builder.setFeature(13, 1);
		builder.setFeature(16, 1);
		builder.setFeature(100, 1);
		SparseExemplar<Integer, Integer> obs = builder.build(Integer[]::new);
		Cluster<Integer, SparseExemplar<Integer, Integer>> cluster = new Cluster<>(0, Arrays.asList(obs, obs, obs, obs));
		SmoteSamplerForCluster<SparseExemplar<Integer, Integer>> sampler = new SmoteSamplerForCluster<>(cluster, null, 10, null, new Random());
		assertEquals(4, sampler.getSample().size());
	}

	static boolean compareDistanceForAllMinorityCombos(SparseExemplar<Integer, Integer> synthetic, List<SparseExemplar<Integer, Integer>> minority) {
		SparseHammingDistance<SparseExemplar<Integer, Integer>> hammingDistance = new SparseHammingDistance<>();
		boolean found = false;
		for (int i = 0; i < minority.size() && !found; i++) {
			for (int j = 0; j < minority.size() && !found; j++) {
				if (i != j){
					SparseExemplar<Integer, Integer> first = minority.get(i);
					SparseExemplar<Integer, Integer> second = minority.get(j);
					long distance = hammingDistance.calculateDistance(first, second) - hammingDistance.calculateDistance(first, synthetic) - hammingDistance.calculateDistance(second, synthetic);
					found = distance == 0;
				}
			}

		}
		return found;
	}

	private void validateSyntheticObs(SparseExemplar<Integer, Integer> real1, SparseExemplar<Integer, Integer> real2, SparseExemplar<Integer, Integer> synthetic) {
		long realDistance = hammingDistance.calculateDistance(real1, real2);
		long synth1 = hammingDistance.calculateDistance(real1, synthetic);
		long synth2 = hammingDistance.calculateDistance(real2, synthetic);
		assertEquals(realDistance, synth1 + synth2);
	}
}