package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.apache.commons.lang3.ObjectUtils;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;
import sun.java2d.marlin.DMarlinRenderingEngine;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MCA<S extends Number, T extends ObservationInterface<S>> {

    private ObservationProviderInterface<S, T> provider;
    private int r; /* the rank of our SVD */
    private int batchSize;
    private int n;
    private SimpleMatrix U;
    private SimpleMatrix S;
    private SimpleMatrix V;

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
           Finding out Modification Matrices
    ##############################################
     */

    private DMatrixRMaj sparseToDense(DMatrixSparseCSC A) {
        int cols = A.numCols;
        double[] ones = new double[cols];
        int i;
        for(i = 0; i < cols; i++) {
            ones[i] = 1;
        }
        DMatrixRMaj I = CommonOps_DDRM.diag(ones);
        DMatrixRMaj C = new DMatrixRMaj();
        CommonOps_DSCC.mult(A, I, C);
        return C;
    }

    private DMatrixRMaj burt(DMatrixSparseCSC Z) {
        DMatrixSparseCSC lowerC = new DMatrixSparseCSC(Z.numRows, Z.numCols);
        CommonOps_DSCC.innerProductLower(Z, lowerC, null, null);
        DMatrixSparseCSC C = new DMatrixSparseCSC(Z.numRows, Z.numCols);
        CommonOps_DSCC.symmLowerToFull(lowerC, C, null);
        return sparseToDense(C);
    }

    private DMatrixRMaj pUpdate(DMatrixRMaj C, int Q, int n, int nPlus) {
        double a = (n + nPlus) + Q * Q;
        CommonOps_DDRM.divide(a, C);
        return C;
    }

//    private ArrayList sUpdate(DMatrixRMaj Dr, DMatrixRMaj P, DMatrixRMaj r, int update){
//        DMatrixRMaj DrMod = Dr.elementPower(-1/2);
//        DMatrixRMaj mid = P.minus(r.transpose().mult(r));
//        DMatrixRMaj A = DrMod.mult(mid);
//        DMatrixRMaj S = A.mult(DrMod);
//        ArrayList<SimpleMatrix> sabHolder = new ArrayList<>();
//        sabHolder.add(S);
//        if(update == 1) {
//            sabHolder.add(A);
//            sabHolder.add(DrMod);
//        }
//        return sabHolder;
//    }

    /*
    ##############################################
                  Init and Modification
    ##############################################
     */


    /*
    ##############################################
                        Update
    ##############################################
     */

//    private SimpleMatrix updateSVD(SimpleMatrix U, SimpleMatrix S,
//                                   SimpleMatrix V, SimpleMatrix A, SimpleMatrix B) {
//
//    }






    /*
    ##############################################
                        Update
    ##############################################
     */



}
