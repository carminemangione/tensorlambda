package com.mangione.continuous.calculators.chisquare;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ProxyValues;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class ProviderToChiSquareForAllFeatures {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderToChiSquareForAllFeatures.class);
	private final List<ChiSquare> chiSquares;

	public ProviderToChiSquareForAllFeatures(ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider,
											 ProxyValues observationStates, ProxyValues targetStates) {

        int numberOfFeatures = getNumberOfFeaturesFromFirstExemplar(provider);
        LOGGER.info(String.format("Calculating Chi-Square for %d features", numberOfFeatures));
        List<ContingencyTable.Builder> contingencyTableBuilders = createBuilderForEachFeature(numberOfFeatures, observationStates, targetStates);
		loopThroughExemplarsAddingToAppropriateBuilder(provider, contingencyTableBuilders);

		chiSquares = contingencyTableBuilders.stream()
				.map(ContingencyTable.Builder::build)
				.map(ChiSquare::new)
				.collect(Collectors.toList());
	}

    private int getNumberOfFeaturesFromFirstExemplar(ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider) {
        ExemplarInterface<Integer, Integer> firstExemplar = provider.iterator().next();
        if (firstExemplar == null)
            throw new IllegalArgumentException("Empty providers not allowed.");

        return firstExemplar.numberOfFeatures();
    }

    private void loopThroughExemplarsAddingToAppropriateBuilder(ObservationProvider<Integer,
			ExemplarInterface<Integer, Integer>> provider, List<ContingencyTable.Builder> contingencyTableBuilders) {

		int numObservations = 0;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (ExemplarInterface<Integer, Integer> exemplar : provider) {
			exemplar.getColumnIndexes()
					.forEach(index -> contingencyTableBuilders.get(index).addObservation(exemplar.getFeature(index),
							exemplar.getTarget()));

			if (numObservations++ % 1000 == 0) {
				LOGGER.info(String.format("Processed %d observations at %d per second", numObservations,
						numObservations / (stopWatch.getSplitTime() / 1000)));
			}
		}
	}

	private List<ContingencyTable.Builder> createBuilderForEachFeature(
			int numberOfFeatures, ProxyValues observationStates, ProxyValues targetStates) {
		return IntStream.range(0, numberOfFeatures)
					.mapToObj(i -> new ContingencyTable.Builder(observationStates.size(), targetStates.size()))
					.collect(Collectors.toList());
	}

	public List<ChiSquare> getChiSquares() {
		return chiSquares;
	}
}
