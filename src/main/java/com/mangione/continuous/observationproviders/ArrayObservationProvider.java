package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ArrayObservationProvider<S, T extends ObservationInterface<S>> extends ObservationProvider<S, T> {
    private final List<T> observations = new ArrayList<>();


    public ArrayObservationProvider(S[][] data, ObservationFactoryInterface<S, T> observationFactoryInterface) {
        super(observationFactoryInterface);
        for (S[] doubles : data) {
            observations.add(create(doubles));
        }
    }

    @SuppressWarnings("WeakerAccess")
    public ArrayObservationProvider(ObservationProvider<S, T> observationProvider,
            ObservationFactoryInterface<S, T> observationFactoryInterface) {
        super(observationFactoryInterface);

        for (T anObservationProvider : observationProvider) {
            observations.add(create(anObservationProvider.getFeatures()));
        }
    }

    @Override
    public long getNumberOfLines()  {
        return observations.size();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new ArrayObservationProviderIterator();
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
