package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.sparse.SparseExemplarInterface;

@SuppressWarnings("WeakerAccess")
public class ProviderToChiSquareForColumn {
	private final ChiSquare fChiSquare;

	public ProviderToChiSquareForColumn(ObservationProvider<Integer, ? extends SparseExemplarInterface<Integer, Integer>> provider, int column) {

		ContingencyTable.Builder builder = new ContingencyTable.Builder(3, 3);
		for (SparseExemplarInterface<Integer, Integer> next : provider)
			builder.addObservation(next.getFeature(column), next.getTarget());

		fChiSquare = new ChiSquare(builder.build());
	}

	public double getChiSquare() {
		return fChiSquare.getChiSquare();
	}
}
