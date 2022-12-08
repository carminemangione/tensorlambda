package com.mangione.continuous.sampling.SMOTE;

import org.opencompare.hac.experiment.DissimilarityMeasure;
import org.opencompare.hac.experiment.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.util.LoggingTimer;

public class HammingDistanceDissimilarityMeasure implements DissimilarityMeasure {
	private static final Logger LOGGER = LoggerFactory.getLogger(HammingDistanceDissimilarityMeasure.class);
	private static final LoggingTimer LOGGING_TIMER = new LoggingTimer(LOGGER, 100, "Distances calculated");
	private final SparseHammingDistance<ObservationInterface<Integer>> hammingDistance = new SparseHammingDistance<>();
	@Override
	public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {
		if (!(experiment instanceof DiscreteExperiment))
			throw new IllegalArgumentException("Experiment must be of type DiscreteExperiment");

		@SuppressWarnings("rawtypes") ListObservationProvider<Integer, ? extends ObservationInterface<Integer>> provider =
				cast(((DiscreteExperiment) experiment).getProvider());

		ObservationInterface<Integer> obs1 = provider.getByIndex(observation1);
		ObservationInterface<Integer> obs2 = provider.getByIndex(observation2);
		LOGGING_TIMER.nextStep();
		return hammingDistance.calculateDistance(obs1, obs2);
	}

	private static <T> T cast(Object o) {
		//noinspection unchecked
		return (T)o;
	}
}
