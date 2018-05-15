package com.mangione.continuous.observationproviders;

import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.calculators.VariableCalculator;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public class VariableCalculatorObservationProvider<R, S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final ObservationFactoryInterface<S, ? extends T> observationFactory;
	private final VariableCalculations<R, S> variableCalculations;



	@SuppressWarnings("WeakerAccess")
	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculations<R, S> variableCalculations,
			ObservationFactoryInterface<S, ? extends T> observationFactory) {

		this.provider = provider;
		this.observationFactory = observationFactory;
		this.variableCalculations = variableCalculations;
	}


	@Override
	public long getNumberOfLines() {
		return 0;
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
			return observationFactory.create(translatedVariables);
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

