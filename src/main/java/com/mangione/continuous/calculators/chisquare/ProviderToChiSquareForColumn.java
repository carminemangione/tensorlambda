package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;

@SuppressWarnings("WeakerAccess")
public class ProviderToChiSquareForColumn {
	private final ChiSquare fChiSquare;

	public ProviderToChiSquareForColumn(ObservationProvider<Integer, ? extends ExemplarInterface<Integer, Integer>> provider, int column) {

		ContingencyTable.Builder builder = new ContingencyTable.Builder(3, 3);
		for (ExemplarInterface<Integer, Integer> next : provider)
			builder.addObservation(next.getFeature(column), next.getTarget());

		fChiSquare = new ChiSquare(builder.build());
	}

	public double getChiSquare() {
		return fChiSquare.getChiSquare();
	}
}
