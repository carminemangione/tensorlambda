package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ObservationToExemplarProvider extends ObservationProvider<Double,DiscreteExemplar<Double>> {


	private final ObservationProviderInterface<Double, ObservationInterface<Double>> provider;

	protected ObservationToExemplarProvider(ObservationProviderInterface<Double, ObservationInterface<Double>> provider,
			ObservationFactoryInterface<Double, ? extends DiscreteExemplar<Double>> factory) {
		super(factory);
		this.provider = provider;
	}

	@Override
	public long getNumberOfLines() {
		return provider.getNumberOfLines();
	}

	@Nonnull
	@Override
	public Iterator<DiscreteExemplar<Double>> iterator() {
		return new Iterator<DiscreteExemplar<Double>>() {

			private final Iterator<ObservationInterface<Double>> iterator = provider.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public DiscreteExemplar<Double> next() {
				return new DiscreteExemplar<>(iterator.next().getFeatures());
			}
		};
	}

	@Override
	public void forEach(Consumer<? super DiscreteExemplar<Double>> action) {
		provider.iterator().forEachRemaining(observation -> action.accept(new DiscreteExemplar<>(observation.getFeatures())));
	}

	@Override
	public Spliterator<DiscreteExemplar<Double>> spliterator() {
		throw new UnsupportedOperationException();
	}
}
