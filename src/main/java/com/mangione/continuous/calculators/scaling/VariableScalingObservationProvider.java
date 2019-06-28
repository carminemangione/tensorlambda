package com.mangione.continuous.calculators.scaling;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class VariableScalingObservationProvider<R extends Number, S extends Number,
		T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final Function<List<S>, T> observationFactory;
	private final VariableScalings<R, S, ObservationProviderInterface<R, ? extends ObservationInterface<R>>> variableScalings;

	public VariableScalingObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableScalingAbstractFactory<R, S> variableScalingFactory, Function<List<S>, T> observationFactory) {

		this.provider = provider;
		variableScalings = new VariableScalings<>(variableScalingFactory, provider);
		this.observationFactory = observationFactory;
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
			return observationFactory.apply(variableScalings.apply(iterator.next().getFeatures()));
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

