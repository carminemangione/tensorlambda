package com.mangione.continuous.classifiers.unsupervised;

import org.ejml.*;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class MCA<S extends Number, T extends ObservationInterface<S>> {

    private ObservationProviderInterface<S, T> provider;
    private int r;
    private int batchSize;
    private int n;

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


    /*
    ##############################################
                        Update
    ##############################################
     */


    private SimpleMatrix burt(SimpleMatrix Z) {
        return Z.transpose().mult(Z);
    }

    private SimpleMatrix pUpdate(SimpleMatrix C, int Q, int n, int nPlus) {
        return C.divide((n + nPlus) + Q * Q);
    }

    private ArrayList sUpdate(SimpleMatrix Dr, SimpleMatrix P, SimpleMatrix r, int update){
        SimpleMatrix DrMod = Dr.elementPower(-1/2);
        SimpleMatrix mid = P.minus(r.transpose().mult(r));
        SimpleMatrix A = DrMod.mult(mid);
        SimpleMatrix S = A.mult(DrMod);
        ArrayList<SimpleMatrix> sabHolder = new ArrayList<>();
        sabHolder.add(S);
        if(update == 1) {
            sabHolder.add(A);
            sabHolder.add(DrMod);
        }
        return sabHolder;
    }

    /*
    ##############################################
                        Update
    ##############################################
     */



}
