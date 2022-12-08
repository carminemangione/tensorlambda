package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import org.apache.commons.lang3.time.StopWatch;

@SuppressWarnings("WeakerAccess")
public class ProviderToChiSquareForFeature {
	private final ChiSquare fChiSquare;
	public ProviderToChiSquareForFeature(ObservationProviderInterface<Integer, ? extends ExemplarInterface<Integer, Integer>> provider, int column) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ContingencyTable.Builder builder = new ContingencyTable.Builder(3, 3);
		for (ExemplarInterface<Integer, Integer> next : provider)
			builder.addObservation(next.getFeature(column), next.getLabel());

		fChiSquare = new ChiSquare(builder.build());
	}

	public double getChiSquare() {
		return fChiSquare.getChiSquare();
	}
}
