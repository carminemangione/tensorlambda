package com.mangione.continuous.calculators;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class VariableCalculatorObservationProvider<R, S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final VariableCalculations<R, S> variableCalculations;
	private Function<List<S>, T> calculatedListToObservation;

	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
												 VariableCalculations<R, S> variableCalculations, Function<List<S>, T> calculatedListToObservation) {

		this.provider = provider;
		this.variableCalculations = variableCalculations;

		this.calculatedListToObservation = calculatedListToObservation;
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return new VariableCalculatorObservationProviderIterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		for (T t : this) action.accept(t);
	}

	@Override
	public Spliterator<T> spliterator() {
		return null;
	}


	private class VariableCalculatorObservationProviderIterator implements Iterator<T> {
		private Iterator<? extends ObservationInterface<R>> iterator;

		private VariableCalculatorObservationProviderIterator() {
			iterator = provider.iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			List<S> translatedVariables = variableCalculations.translateAllVariables(iterator.next().getAllColumns());
			return calculatedListToObservation.apply(translatedVariables);
		}

		@Override
		public void remove() {
			iterator.remove();
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			while (hasNext())
				action.accept(next());
		}
	}

}

