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
import java.util.Iterator;
import java.util.stream.IntStream;

/* implement using provider at some point, make sure to get the file implementation first */
public class MCA<S extends Number, T extends ObservationInterface<S>> {
    private ObservationProviderInterface<S,T> provider;
    private ProxyValuesMultiColumn pv;
    private int rank, batchSize, numRows, numCols;
    private DMatrixRMaj U, Sig, V, Dr;
    private Boolean firstIteration = true;



    public MCA(File file, int rank, int batchSize, ObservationProviderInterface provider) throws IOException{
        /* declare U, S, V for first update */
        this.provider = provider;
        this.batchSize = batchSize;
        this.numCols = provider.getNumberOfColumns();
        this.numRows = (int) provider.getNumberOfLines();
        assert batchSize < this.numRows : "Bro your batchSize cannot be greater than the length of the dataset lol";
        int numBatches = this.numRows / this.batchSize;
        int leftover = this.numRows % this.batchSize;
        initVars(file, rank, provider);
        Iterator<T> iter = provider.iterator();
        int i = 0, ctr = 0;
//        while (i <= numBatches) {
//
//        }
    }

    private void initVars(File file, int rank, ObservationProviderInterface<S, T> provider) throws IOException {
        this.rank = rank;
        this.provider = provider;
        this.numRows = provider.getNumberOfColumns();
        this.pv = new ProxyValuesMultiColumn(file);
        this.U = new DMatrixRMaj(pv.getNumLevels(), pv.getNumLevels());
        this.Sig = new DMatrixRMaj(pv.getNumLevels(), pv.getNumLevels());
        this.V = new DMatrixRMaj(pv.getNumLevels(), pv.getNumLevels());

    }

    private void createZ(ProxyValuesMultiColumn pv, Iterator<T> iter) throws IOException {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(this.batchSize, pv.getNumLevels());
//        IntStream.range(0, this.batchSize)
//                .forEach(row -> IntStream.range(0, this.numCols)
//                        .forEach(col -> Z.set(row, pv.getIndex(col, iter.next().getFeature(col)))));


    }
}
