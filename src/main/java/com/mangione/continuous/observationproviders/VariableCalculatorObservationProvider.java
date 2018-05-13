package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class VariableCalculatorObservationProvider<R, S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final VariableCalculator<R, S> defaultCalculator;
	private final Map<Integer, VariableCalculator<R, S>> indexToCalculator;
	private final ObservationFactoryInterface<S, T> observationFactory;

	@SuppressWarnings("unused")
	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculator<R, S> defaultVariableCalculator,
			ObservationFactoryInterface<S, T> observationFactory) {
		this(provider, defaultVariableCalculator, new HashMap<>(), observationFactory);
	}


	@SuppressWarnings("WeakerAccess")
	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculator<R, S> defaultVariableCalculator, Map<Integer, VariableCalculator<R, S>> indexToCalculator,
			ObservationFactoryInterface<S, T> observationFactory) {

		this.provider = provider;
		this.defaultCalculator = defaultVariableCalculator;
		this.indexToCalculator = indexToCalculator;
		this.observationFactory = observationFactory;
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

	private List<S> translateAllVariables(List<R> features) {
		AtomicInteger i = new AtomicInteger();

		final List<S> translatedVariables = new ArrayList<>();


		features.stream()
				.map(x -> calculateVariableWithIndexedCalculatorOrDefault(x, i.getAndIncrement()))
				.forEach(translatedVariables::addAll);

		return translatedVariables;
	}

	private List<S> calculateVariableWithIndexedCalculatorOrDefault(R variable, int index) {
		return indexToCalculator.get(index) != null ?
				indexToCalculator.get(index).apply(variable) :
				defaultCalculator.apply(variable);
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
			List<S> translatedVariables = translateAllVariables(iterator.next().getFeatures());
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

