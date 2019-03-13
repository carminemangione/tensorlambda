package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class ProviderToCSRMatrix<S extends Number, T extends ObservationInterface<S>,
        U extends ObservationProviderInterface<S, T>> {
    private final double[] values;
    private final int[] columnIndexes;
    private final int[] rowIndexes;

    public ProviderToCSRMatrix(U provider) {
        List<Integer> columnIndexes = new ArrayList<>();
        List<Integer> rowIndexes = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        rowIndexes.add(0);
        int i = 0;
        for (T sObservation : provider) {
            CSRRow<T> csrRow = new CSRRow<>(sObservation);
            rowIndexes.add(rowIndexes.get(rowIndexes.size() - 1) + csrRow.getNumValues());
            values.addAll(csrRow.getValues());
            columnIndexes.addAll(csrRow.getIndexes());
            if(i++ % 500 == 0) {
                break;
            }
            if(i % 100 == 0)
                System.out.println("GOTTEMM" + i);
        }

        this.values = values.stream().mapToDouble(x->x).toArray();
        this.columnIndexes = columnIndexes.stream().mapToInt(x->x).toArray();
        this.rowIndexes = rowIndexes.stream().mapToInt(x->x).toArray();
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
}
