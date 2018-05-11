package com.mangione.continuous.zscale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;


public class ZScaleObservationProvider<T extends ObservationInterface<Double>> extends ObservationProvider<Double, T> {
    private final ObservationProviderInterface<Double, T> observationProvider;
    private final List<ColumnZScale> columnZScales = new ArrayList<>();
    private Integer numberOfColumns;


    @SuppressWarnings("WeakerAccess")
    public ZScaleObservationProvider(ObservationProviderInterface<Double, T> observationProvider,
            ObservationFactoryInterface<Double, T> observationFactory) {
        super(observationFactory);
        this.observationProvider = observationProvider;
        if (!observationProvider.iterator().hasNext())
            return;

        final List<List<Double>> columnList = cruiseThroughProviderCollectingColumns(observationProvider);
        columnList.forEach(list->columnZScales.add(new ColumnZScale(list)));
    }

    public T scale(T nonScaled) {
        Double[] scaled = new Double[numberOfColumns];
        for (int i = 0; i < columnZScales.size(); i++) {
            scaled[i] = columnZScales.get(i).zscale(nonScaled.getFeatures()[i]);
        }
        if (columnZScales.size() < numberOfColumns)
            scaled[numberOfColumns - 1] = nonScaled.getFeatures()[numberOfColumns-2];
        return create(scaled);
    }

    @Override
    public long getNumberOfLines() {
        return observationProvider.getNumberOfLines();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new ZScaleObservationProviderIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        iterator().forEachRemaining(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("spliterator not supported.");
    }

    private class ZScaleObservationProviderIterator implements Iterator<T> {

        private final Iterator<T> iterator;

        ZScaleObservationProviderIterator() {
            iterator = observationProvider.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            T nonScaled = iterator.next();
            return scale(nonScaled);
        }

        @Override
        public void remove() {
            iterator.remove();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            while(hasNext())
                action.accept(next());
        }
    }

    private List<List<Double>> cruiseThroughProviderCollectingColumns(ObservationProviderInterface<Double, T> observationProvider) {

        List<List<Double>> columnList = null;

        for (T currentObservation : observationProvider) {
            final Double[] features = currentObservation.getFeatures();
            if (columnList == null) {
                this.numberOfColumns = features.length;
                columnList = allocateColumnListsForNumberOfFeatures(numberOfColumns);
            }

            for (int i = 0; i < numberOfColumns; i++) {
                columnList.get(i).add(features[i]);
            }
        }
        return columnList;
    }

    private List<List<Double>> allocateColumnListsForNumberOfFeatures(int numberOfColumns) {
        List<List<Double>> columnList = new ArrayList<>();
        for (int i = 0; i < numberOfColumns; i++) {
            columnList.add(new ArrayList<>());
        }
        return columnList;
    }
}
