package com.mangione.continuous.calculators;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class VariableCalculatorObservationProvider<ORIGINAL, CALCULATED, PROVIDER extends ObservationInterface<CALCULATED>> implements ObservationProviderInterface<CALCULATED, PROVIDER> {

	private final ObservationProviderInterface<ORIGINAL, ? extends ObservationInterface<ORIGINAL>> provider;
	private final VariableCalculations<ORIGINAL, CALCULATED> variableCalculations;
	private final Function<List<CALCULATED>, PROVIDER> calculatedListToObservation;
	private final IntFunction<ORIGINAL[]> featureBuilder;

	public VariableCalculatorObservationProvider(ObservationProviderInterface<ORIGINAL, ? extends ObservationInterface<ORIGINAL>> provider,
			VariableCalculations<ORIGINAL, CALCULATED> variableCalculations, Function<List<CALCULATED>, PROVIDER> calculatedListToObservation,
			IntFunction<ORIGINAL[]> featureBuilder) {

		this.provider = provider;
		this.variableCalculations = variableCalculations;
		this.calculatedListToObservation = calculatedListToObservation;
		this.featureBuilder = featureBuilder;
	}

	@Override
	@Nonnull
	public Iterator<PROVIDER> iterator() {
		return new VariableCalculatorObservationProviderIterator();
	}

	@Override
	public void forEach(Consumer<? super PROVIDER> action) {
		for (PROVIDER t : this) action.accept(t);
	}

	@Override
	public Spliterator<PROVIDER> spliterator() {
		return null;
	}


	private class VariableCalculatorObservationProviderIterator implements Iterator<PROVIDER> {
		private final Iterator<? extends ObservationInterface<ORIGINAL>> iterator;

		private VariableCalculatorObservationProviderIterator() {
			iterator = provider.iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public PROVIDER next() {
			List<CALCULATED> translatedVariables = variableCalculations.translateAllVariables(iterator.next().getFeatures(featureBuilder));
			return calculatedListToObservation.apply(translatedVariables);
		}

		@Override
		public void remove() {
			iterator.remove();
		}

		@Override
		public void forEachRemaining(Consumer<? super PROVIDER> action) {
			while (hasNext())
				action.accept(next());
		}
	}

}

