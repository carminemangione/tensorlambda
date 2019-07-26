package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

import java.util.ArrayList;
import java.util.List;

public class MCA<S extends Number, T extends ObservationInterface<S>> {

    private ObservationProviderInterface<S, T> provider;
    private int r;
    private int batchSize;


    /* Args:
        r - the rank of our svd
        batchSize - the size of the batches we use to iteratively update SVD
        provider - the dataprovider

        What it does:
        Conducts an MCA transform on the data

     */
    public MCA(int r, int batchSize, ObservationProviderInterface<S, T> provider){
        this.provider = provider;
        this.r = r;
        this.batchSize = batchSize;

    }

//    private double[][] burt(double[][] Z) {
//        return;
//    }

}
