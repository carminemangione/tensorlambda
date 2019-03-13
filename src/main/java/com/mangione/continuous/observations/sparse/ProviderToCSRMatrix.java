package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class ProviderToCSRMatrix<S extends Number, T extends SparseObservationInterface<S>,
        U extends ObservationProviderInterface<S, T>> {
    private final double[] values;
    private final int[] columnIndexes;
    private final int[] rowIndexes;
	private final double[] targets;

	public ProviderToCSRMatrix(U provider) {
        List<Integer> columnIndexes = new ArrayList<>();
        List<Integer> rowIndexes = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<Integer> targets = new ArrayList<>();

        rowIndexes.add(0);
        int i = 0;
        for (T sObservation : provider) {
        	try {
		        targets.add(((FailedTestExemplar) sObservation).getTarget());
	        } catch(Exception e) {
        		e.printStackTrace();
	        }
            CSRRow<T> csrRow = new CSRRow<>(sObservation);
            rowIndexes.add(rowIndexes.get(rowIndexes.size() - 1) + csrRow.getNumValues());
            values.addAll(csrRow.getValues());
            columnIndexes.addAll(csrRow.getIndexes());

        }

        this.values = values.stream().mapToDouble(x->x).toArray();
        this.columnIndexes = columnIndexes.stream().mapToInt(x->x).toArray();
        this.rowIndexes = rowIndexes.stream().mapToInt(x->x).toArray();
        this.targets = targets.stream().mapToDouble(x -> x).toArray();
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

    public double[] getTargets() {
		return targets;
    }
}
