package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValuesMultiColumn;
import com.mangione.continuous.observations.dense.Observation;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/* implement using provider at some point, make sure to get the file implementation first */
public class MCA<S extends Number, T extends ObservationInterface<S>>  {
    private ObservationProviderInterface<S,T> provider;
    private ProxyValuesMultiColumn pv;
    int rank, batchSize;
    DMatrixRMaj U, Sig, V, Dr;
    public MCA(File file, int rank, int batchSize) throws IOException {
        /* declare U, S, V for first update */
        this.pv = new ProxyValuesMultiColumn(file);
        /* now proxy to sparse */
    }

    private void createZ(ProxyValuesMultiColumn pv, ObservationProviderInterface<S, T> provider) throws IOException {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(this.batchSize, pv.getNumLevels());
    }
}
