package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;

public class ListObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {
	private final List<T> observations;

	public ListObservationProvider(ObservationProviderInterface<S, T> provider) {
		observations = provider.getStream().collect(Collectors.toList());
	}

	public ListObservationProvider(List<? extends T> data) {
		observations = new ArrayList<>(data);
	}

	public T getByIndex(int index) {
		if (index >= observations.size())
			throw new IllegalArgumentException(String.format("Index %d is out of bounds, size of list is %d", index, observations.size()));
		return observations.get(index);
	}

	@Override
	public int size() {
		return observations.size();
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return new ArrayObservationProviderIterator();
	}

	public Iterator<T> iterator(int curr) {
		return new ArrayObservationProviderIterator(curr);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		for (T t : this) {
			action.accept(t);
		}
	}

	private class ArrayObservationProviderIterator implements Iterator<T> {
		private int current;

		ArrayObservationProviderIterator(int curr) {
			current = curr;
		}

		ArrayObservationProviderIterator() {
		}

		@Override
		public boolean hasNext() {
			return current < observations.size();
		}

		@Override
		public T next() {
			return observations.get(current++);
		}

		@Override
		public void remove() {
			observations.remove(current);
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			while (hasNext()) {
				T next = next();
				action.accept(next);
			}
		}

		@Override
		public String toString() {
			return "ArrayObservationProviderIterator{" +
					"current=" + current +
					'}';
		}
	}
}
