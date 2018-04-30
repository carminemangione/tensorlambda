package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class VariableCalculatorObservationProvider<R, S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final VariableCalculator<R, S> defaultCalculator;
	private final Map<Integer, VariableCalculator<R, S>> indexToCalculator;
	private final ArraySupplier<S> arraySupplier;
	private final ObservationFactoryInterface<S, T> observationFactory;

	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculator<R, S> defaultVariableCalculator, ArraySupplier<S> arraySupplier,
			ObservationFactoryInterface<S, T> observationFactory) {
		this(provider, defaultVariableCalculator, new HashMap<>(), arraySupplier, observationFactory);
	}

	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculator<R, S> defaultVariableCalculator, Map<Integer, VariableCalculator<R, S>> indexToCalculator,
			ArraySupplier<S> arraySupplier, ObservationFactoryInterface<S, T> observationFactory) {

		this.provider = provider;
		this.defaultCalculator = defaultVariableCalculator;
		this.indexToCalculator = indexToCalculator;
		this.arraySupplier = arraySupplier;
		this.observationFactory = observationFactory;
	}

	@Override
	public boolean hasNext() {
		return provider.hasNext();
	}

	@Override
	public T next() {
		S[] translatedVariables = translateAllVariables(provider.next().getFeatures());
		return observationFactory.create(translatedVariables);
	}

	@Override
	public void reset() {

	}

	@Override
	public long getNumberOfLines() {
		return 0;
	}

	@Override
	public T create(S[] data) {
		return null;
	}

	private S[] translateAllVariables(R[] features) {
		List<S> translatedVariables = new ArrayList<>();
		for (int i = 0; i < features.length; i++) {
			translatedVariables.addAll(calculateVariableWithIndexedCalcuatorOrDefault(features[i], i));
		}
		return translatedVariables.toArray(arraySupplier.get(translatedVariables.size()));
	}

	private List<S> calculateVariableWithIndexedCalcuatorOrDefault(R variable, int index) {
		return indexToCalculator.get(index) != null ?
				indexToCalculator.get(index).calculateVariable(variable) :
				defaultCalculator.calculateVariable(variable);
	}

}

