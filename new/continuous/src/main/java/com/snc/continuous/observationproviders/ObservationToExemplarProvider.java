package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObservationToExemplarProvider<S extends Number, T extends ExemplarInterface<S, Integer>>
        implements ObservationProviderInterface<S, T> {
    private final ObservationProviderInterface<S, ? extends ObservationInterface<S>> provider;
    private final Function<ObservationInterface<S>, T> factory;

    public ObservationToExemplarProvider(ObservationProviderInterface<S, ? extends ObservationInterface<S>> provider,
                                         Function<ObservationInterface<S>, T> observationToExemplarFactory) {
        this.provider = provider;
        this.factory = observationToExemplarFactory;
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final Iterator<? extends ObservationInterface<S>> iterator = provider.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return factory.apply(iterator.next());
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        provider.iterator().forEachRemaining(observation -> action.accept(
                factory.apply(observation)));
    }
}
