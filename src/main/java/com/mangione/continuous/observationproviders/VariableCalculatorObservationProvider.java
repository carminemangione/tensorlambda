package com.mangione.continuous.observationproviders;

import clojure.lang.PersistentVector;
import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.calculators.VariableCalculator;
import com.mangione.continuous.observations.NamedColumns;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class VariableCalculatorObservationProvider<R, S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider;
	private final ObservationFactoryInterface<S, ? extends T> observationFactory;
	private final VariableCalculations<R, S> variableCalculations;
	private final NamedColumns namedColumns = new NamedColumns();
	private PersistentVector featureMap;



	@SuppressWarnings("WeakerAccess")
	public VariableCalculatorObservationProvider(ObservationProviderInterface<R, ? extends ObservationInterface<R>> provider,
			VariableCalculations<R, S> variableCalculations,
			ObservationFactoryInterface<S, ? extends T> observationFactory) {

		this.provider = provider;
		this.observationFactory = observationFactory;
		this.variableCalculations = variableCalculations;

	}

	public void fillNamedColumns(Map<String, Integer> mapOfTracks) {
		for (Map.Entry<String, Integer> entry : mapOfTracks.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			namedColumns.addColumn(value, key);
		}
		System.out.println(namedColumns);
	}

	public void setFeatureMap(PersistentVector map){
		featureMap = map;
		for(Object val : map){
			System.out.println(val);
		}
		
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

		private int counter = 0;

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

