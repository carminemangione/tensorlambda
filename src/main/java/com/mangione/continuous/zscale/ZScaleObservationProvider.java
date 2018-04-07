package com.mangione.continuous.zscale;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ZScaleObservationProvider<T extends Observation> extends ObservationProvider<T> {
    private final ArrayObservationProvider observationProvider;
    private final List<ColumnZScale> columnZScales = new ArrayList<>();
    private int numberOfColumns;


    public ZScaleObservationProvider(ArrayObservationProvider observationProvider,
            ObservationFactoryInterface<T> observationFactory, boolean lastColumnIsLabel) throws IOException {
        super(observationFactory);
        this.observationProvider = observationProvider;

        final List<List<Double>> columnList = cruiseThroughProviderCollectingColumns(observationProvider, lastColumnIsLabel);
        columnList.forEach(list->columnZScales.add(new ColumnZScale(list)));
    }

    @Override
    public boolean hasNext() throws IOException {
        return observationProvider.hasNext();
    }

    @Override
    public T next() throws IOException {

        Observation nonScaled = observationProvider.next();
        return scale(nonScaled);
    }

    public T scale(Observation nonScaled) {
        double[] scaled = new double[numberOfColumns];
        for (int i = 0; i < columnZScales.size(); i++) {
            scaled[i] = columnZScales.get(i).zscale(nonScaled.getFeatures()[i]);
        }
        if (columnZScales.size() < numberOfColumns)
            scaled[numberOfColumns - 1] = nonScaled.getFeatures()[numberOfColumns-2];
        return create(scaled);
    }


    @Override
    public void reset() throws IOException {
        observationProvider.reset();
    }

    @Override
    public long getNumberOfLines() throws IOException {
        return observationProvider.getNumberOfLines();
    }

    private List<List<Double>> cruiseThroughProviderCollectingColumns(ArrayObservationProvider observationProvider,
            boolean lastColumnIsLabel) throws IOException {
        Observation currentObservation = observationProvider.next();
        this.numberOfColumns = currentObservation.getFeatures().length;
        int numberOfColumns = this.numberOfColumns - (lastColumnIsLabel ? 1 : 0);

        final List<List<Double>> columnList = allocateColumnListsForNumberOfFeatures(numberOfColumns);
        observationProvider.reset();
        while (observationProvider.hasNext()) {
            currentObservation = observationProvider.next();
            final double[] features = currentObservation.getFeatures();
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
