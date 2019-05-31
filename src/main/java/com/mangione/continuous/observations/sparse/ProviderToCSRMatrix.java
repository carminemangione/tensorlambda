package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@SuppressWarnings("WeakerAccess")
public class ProviderToCSRMatrix<S extends Number, T extends ObservationInterface<S>,
        U extends ObservationProviderInterface<S, T>> {
    private double[] values;
    private int[] columnIndexes;
    private int[] rowIndexes;
    private final List<Integer> columnIndexesList;
    private final List<Integer> rowIndexesList;
    private final List<S> valueList;

    public ProviderToCSRMatrix(U provider) {
        columnIndexesList = new ArrayList<>();
        rowIndexesList = new ArrayList<>();
        valueList = new ArrayList<>();

        int i = 0;

        rowIndexesList.add(0);
        for (T sObservation : provider) {
            processNextRow(sObservation);
            if(++i > 100000)
            	break;
        }

        finishProcessing();
    }

    public double[] getValues() {
        return values;
    }

    public int[] getColumnIndexes() {
        return columnIndexes;
    }

    public int[] getRowIndexes() {
        return rowIndexes;
    }

    protected void processNextRow(T observation) {
        Set<Integer> columnIndexes = observation.getColumnIndexes();
        columnIndexesList.addAll(columnIndexes);
        columnIndexes.forEach(
                index -> valueList.add(observation.getFeature(index)));
        rowIndexesList.add(rowIndexesList.get(rowIndexesList.size() - 1) + columnIndexes.size());
    }


    protected void finishProcessing() {
        this.values = valueList.stream()
                .map(Number::doubleValue)
                .mapToDouble(x -> x).toArray();
        this.columnIndexes = columnIndexesList.stream().mapToInt(x -> x).toArray();
        this.rowIndexes = rowIndexesList.stream().mapToInt(x -> x).toArray();
    }
}
