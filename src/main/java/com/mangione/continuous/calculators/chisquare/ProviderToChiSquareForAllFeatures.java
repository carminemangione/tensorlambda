package com.mangione.continuous.calculators.chisquare;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ProxyValues;

@SuppressWarnings("WeakerAccess")
public class ProviderToChiSquareForAllFeatures {


	private final List<ChiSquare> chiSquares;

	public ProviderToChiSquareForAllFeatures(ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider,
											 ProxyValues observationStates, ProxyValues targetStates) {

        int numberOfFeatures = getNumberOfFeaturesFromFirstExemplar(provider);
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

		for (ExemplarInterface<Integer, Integer> exemplar : provider) {
			exemplar.getColumnIndexes()
					.forEach(index -> contingencyTableBuilders.get(index).addObservation(exemplar.getFeature(index),
							exemplar.getTarget()));
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
