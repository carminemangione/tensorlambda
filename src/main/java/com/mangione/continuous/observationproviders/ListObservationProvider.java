package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ListObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {
	private final List<T> observations = new ArrayList<>();


	public ListObservationProvider(List<? extends T> data) {
		observations.addAll(data);
	}

	public T getByIndex(int index) {
		try {
			return observations.get(index);
		} catch (Throwable e) {
			System.out.println("Index: " + index);
			return null;
		}
	}

	@Override
	public long getNumberOfLines() {
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

	@Override
	public Spliterator<T> spliterator() {
		throw new UnsupportedOperationException("spliterator not supported.");
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
