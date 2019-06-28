package com.mangione.continuous.observationproviders;

import java.util.List;
import java.util.function.Function;

import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.observations.ObservationInterface;

public class AllColumnCalculatingObserationProvider<R, S, T  extends ObservationInterface<S>> extends VariableCalculatorObservationProvider<R, S, T> {
	public AllColumnCalculatingObserationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculations<R, S> variableCalculations, Function<List<S>, T> calculatedListToObservation) {
		super(provider, variableCalculations, calculatedListToObservation);
	}
}
