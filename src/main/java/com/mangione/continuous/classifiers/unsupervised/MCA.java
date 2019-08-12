package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.ejml.data.DMatrixRMaj;

public class MCA<S extends Number, T extends ObservationInterface<S>> {
    private ObservationProviderInterface<S, T> provider;
    int rank, batchSize;
    DMatrixRMaj U, S, V;
    public MCA(ObservationProviderInterface<S, T> provider, int rank, int batchSize) {
        /* declare U, S, V for first update */
        /* need to create setter for U, S, V basically so that if we remove columns, we still have
        *  zeros in place of the removed columns as opposed to literally lopping them off */
    }
}
