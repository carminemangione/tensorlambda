package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

import java.util.ArrayList;

public class ProviderToCSRMatrixWithTarget<S extends Number, T extends ExemplarInterface<S, Integer>,
        U extends ObservationProviderInterface<S, T>> extends ProviderToCSRMatrix<S, T, U>{

    private ArrayList<Integer> targetsList;
    private int[] targets;

    @SuppressWarnings("WeakerAccess")
    public ProviderToCSRMatrixWithTarget(U provider) {
        super(provider);
    }

    public int[] getTargets() {
        return targets;
    }

    @Override
    protected void processNextRow(T exemplar) {
        super.processNextRow(exemplar);
        if(targetsList == null)
            targetsList = new ArrayList<>();
        targetsList.add(exemplar.getTarget());
    }

    @Override
    protected void finishProcessing() {
        this.targets = targetsList.stream().mapToInt(x->x).toArray();
        super.finishProcessing();
    }
}
