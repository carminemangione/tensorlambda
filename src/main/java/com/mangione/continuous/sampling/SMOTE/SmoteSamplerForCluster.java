package com.mangione.continuous.sampling.SMOTE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.classifiers.unsupervised.Cluster;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;
import com.mangione.continuous.util.LoggingTimer;

public class SmoteSamplerForCluster<EXEMPLAR extends ExemplarInterface<Integer, Integer>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmoteSamplerForCluster.class);
	private static final LoggingTimer LOGGING_TIMER = new LoggingTimer(LOGGER, 1000, "Generating synthetic minority samples.");

	private final SparseHammingDistance<EXEMPLAR> hammingDistance = new SparseHammingDistance<>();
	private final Random random;
	private final List<EXEMPLAR> sample;

	private final SyntheticVectorCalculator<EXEMPLAR> syntheticVectorCalculator;

	public SmoteSamplerForCluster(Cluster<Integer, EXEMPLAR> cluster,
			Function<EXEMPLAR, Integer> tagFunction, int targetNumber,
			SparseExemplarFactoryInterface<Integer, EXEMPLAR> factory, Random random) {
		this.random = random;
		this.syntheticVectorCalculator = new SyntheticVectorCalculator<>(tagFunction, factory, random);
		sample = new ArrayList<>(cluster.getObservations());

		if (isThereSpaceBetweenExemplars(cluster))
			sample.addAll(generateSyntheticMinorityExemplars(cluster, targetNumber));
	}

	private boolean isThereSpaceBetweenExemplars(Cluster<Integer, EXEMPLAR> cluster) {
		boolean different = false;
		for (int i = 0; i < cluster.getObservations().size() && !different; i++) {
			for (int j = 0; j < cluster.getObservations().size() && !different; j++) {
				different = hammingDistance.calculateDistance(cluster.getObservations().get(i), cluster.getObservations().get(j)) != 0;
			}
		}
		return different;
	}

	public List<EXEMPLAR> getSample() {
		return sample;
	}

	private List<EXEMPLAR> generateSyntheticMinorityExemplars(Cluster<Integer, EXEMPLAR> cluster, int targetNumber) {
		return cluster.getObservations().size() > 1 ? IntStream.range(0, targetNumber)
				.boxed()
				.map(i -> createSyntheticExemplar(cluster.getObservations()))
				.collect(Collectors.toList()) : new ArrayList<>();
	}

	private EXEMPLAR createSyntheticExemplar(List<EXEMPLAR> observations) {
		int indexOfFirstChoice = random.nextInt(observations.size());
		EXEMPLAR first = observations.get(indexOfFirstChoice);
		EXEMPLAR second;
		do {
			second = observations.get(random.nextInt(observations.size()));
		} while (hammingDistance.calculateDistance(first, second) == 0);
		LOGGING_TIMER.nextStep();
		return syntheticVectorCalculator.generateVector(first, second);
	}

}
