package com.mangione.continuous.zscale;

import java.util.ArrayList;
import java.util.List;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;


public class ZScaleObservationProvider<T extends ObservationInterface<Double>> extends ObservationProvider<Double, T> {
    private final ObservationProvider<Double, T> observationProvider;
    private final List<ColumnZScale> columnZScales = new ArrayList<>();
    private int numberOfColumns;


    public ZScaleObservationProvider(ObservationProvider<Double, T> observationProvider,
            ObservationFactoryInterface<Double, T> observationFactory) {
        super(observationFactory);
        this.observationProvider = observationProvider;

        final List<List<Double>> columnList = cruiseThroughProviderCollectingColumns(observationProvider);
        columnList.forEach(list->columnZScales.add(new ColumnZScale(list)));
    }

    @Override
    public boolean hasNext() {
        return observationProvider.hasNext();
    }

    @Override
    public T next() {
        T nonScaled = observationProvider.next();
        return scale(nonScaled);
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
    public void reset() {
        observationProvider.reset();
    }

    @Override
    public long getNumberOfLines() {
        return observationProvider.getNumberOfLines();
    }

    private List<List<Double>> cruiseThroughProviderCollectingColumns(ObservationProvider<Double, T> observationProvider) {

        ObservationInterface<Double> currentObservation = observationProvider.next();
        this.numberOfColumns = currentObservation.getFeatures().length;

        final List<List<Double>> columnList = allocateColumnListsForNumberOfFeatures(numberOfColumns);
        observationProvider.reset();
        while (observationProvider.hasNext()) {
            currentObservation = observationProvider.next();
            final Double[] features = currentObservation.getFeatures();
            for (int i = 0; i < numberOfColumns; i++) {
                columnList.get(i).add(features[i]);
            }
        }
        observationProvider.reset();
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
