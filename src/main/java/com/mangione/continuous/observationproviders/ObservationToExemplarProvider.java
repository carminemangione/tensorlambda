package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.ObservationInterface;

public class ObservationToExemplarProvider<T extends Number> implements ObservationProviderInterface<T, DiscreteExemplar<T>> {
	private final ObservationProviderInterface<T, ? extends ObservationInterface<T>> provider;
	private final int targetColumnIndex;

	@SuppressWarnings("WeakerAccess")
	public ObservationToExemplarProvider(ObservationProviderInterface<T, ? extends ObservationInterface<T>> provider,
			int targetColumnIndex) {
		this.provider = provider;
		this.targetColumnIndex = targetColumnIndex;
	}

	@Nonnull
	@Override
	public Iterator<DiscreteExemplar<T>> iterator() {
		return new Iterator<DiscreteExemplar<T>>() {

			private final Iterator<? extends ObservationInterface<T>> iterator = provider.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public DiscreteExemplar<T> next() {
				return DiscreteExemplar.getExemplarTargetWithColumn(iterator.next().getFeatures(), targetColumnIndex);
			}
		};
	}

	@Override
	public void forEach(Consumer<? super DiscreteExemplar<T>> action) {
		provider.iterator().forEachRemaining(observation -> action.accept(
				DiscreteExemplar.getExemplarTargetWithColumn(observation.getFeatures(), targetColumnIndex)));
	}

	@Override
	public Spliterator<DiscreteExemplar<T>> spliterator() {
		throw new UnsupportedOperationException();
	}
}
