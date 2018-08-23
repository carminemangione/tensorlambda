package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ArrayObservationProvider<S, T extends ObservationInterface<S>>
		extends ObservationProvider<S, T> implements ObservationProviderInterface<S, T> {
	private final List<T> observations = new ArrayList<>();


	public ArrayObservationProvider(S[][] data, ObservationFactoryInterface<S, T> observationFactoryInterface) {
		super(observationFactoryInterface);
		for (S[] doubles : data) {
			observations.add(create(Arrays.asList(doubles)));
		}
	}

	public ArrayObservationProvider(List<? extends T> data, ObservationFactoryInterface<S, ? extends T> observationFactoryInterface) {
		super(observationFactoryInterface);
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

	@SuppressWarnings("WeakerAccess")
	public ArrayObservationProvider(ObservationProviderInterface<S, ? extends T> observationProvider,
			ObservationFactoryInterface<S, ? extends T> observationFactoryInterface) {
		super(observationFactoryInterface);

		for (T anObservationProvider : observationProvider) {
			observations.add(create(anObservationProvider.getAllColumns()));
		}
	}



	public ArrayObservationProvider(ObservationProviderInterface<S, ? extends T> observationProvider,
	                                ObservationFactoryInterface<S, ? extends T> observationFactoryInterface, Comparator<ObservationInterface<S>> comp) {
		super(observationFactoryInterface);

		int counter = 0;
		System.out.println("Reading CSV");
		for (T anObservationProvider : observationProvider) {
			counter++;
			if(counter % 100000 == 0)
				System.out.println(counter);
			if(anObservationProvider != null)
				observations.add(create(anObservationProvider.getAllColumns()));
		}
		System.out.println("About to Sort");
		observations.sort(comp);
		System.out.println("Sorted!");
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

		public ArrayObservationProviderIterator(int curr) {
			current = curr;
		}

		public ArrayObservationProviderIterator() {
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
				action.accept(next());
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
