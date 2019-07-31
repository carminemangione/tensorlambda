package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.apache.commons.lang3.ObjectUtils;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.sparse.csc.CommonOps_DSCC;
import org.ejml.dense.row.decomposition.qr.QRColPivDecompositionHouseholderColumn_DDRM;

//import sun.java2d.marlin.DMarlinRenderingEngine;

import java.util.ArrayList;
import java.util.List;

public class MCA<S extends Number, T extends ObservationInterface<S>> {

    private ObservationProviderInterface<S, T> provider;
    private int r; /* the rank of our SVD */
    private int batchSize;
    private int n;
    private int nPlus;
    private int Q;

    private DMatrixRMaj C;
    private DMatrixRMaj P;
    private DMatrixRMaj U;
    private DMatrixRMaj S;
    private DMatrixRMaj Vt;
    private DMatrixRMaj A;
    private DMatrixRMaj B;

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
        this.nPlus = 0;

    }

    public MCA() {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(5, 3);
        int i, j, ctr = 0;
        for(i = 0; i < 2; i++) {
            for(j = 0; j < 3; j++) {
                Z.set(i, j, ctr);
                ctr ++;
            }
        }
        this.Q = 3;
        this.n = 2;
        this.nPlus = 0;
        this.S = new DMatrixRMaj(this.r, this.r);
        modSuite((Z));
    }


    /*
    ##############################################
           Finding out Modification Matrices
    ##############################################
     */

    /* returns single column vector of row sums */
    /* YE */
    private DMatrixRMaj makeR(DMatrixRMaj P) {
        DMatrixRMaj r = new DMatrixRMaj(P.numRows, 1);
        CommonOps_DDRM.sumRows(P, r);
        return r;
    }

    /* returns diagonalized row sums */

    private DMatrixRMaj makeDr(DMatrixRMaj r) {
        double[] rArray = new double[r.numRows];
        int i;
        for(i = 0; i < r.numRows; i++) {
            rArray[i] = r.get(i, 0);
        }
        return CommonOps_DDRM.diag(rArray);
    }

    /* square sparse matrix to dense matrix */
    /* YE */
    private DMatrixRMaj sparseToDense(DMatrixSparseCSC A) {
        int cols = A.numCols;
        DMatrixRMaj I = CommonOps_DDRM.identity(cols);
        DMatrixRMaj C = new DMatrixRMaj(cols, cols);
        CommonOps_DSCC.mult(A, I, C);
        return C;
    }

    /* makes burt matrix */
    /* YE */
    private DMatrixRMaj burt(DMatrixSparseCSC Z) {
        DMatrixSparseCSC lowerC = new DMatrixSparseCSC(Z.numRows, Z.numRows);
        CommonOps_DSCC.innerProductLower(Z, lowerC, null, null);

        DMatrixSparseCSC C = new DMatrixSparseCSC(Z.numCols, Z.numCols);
        CommonOps_DSCC.symmLowerToFull(lowerC, C, null);
        return sparseToDense(C);
    }

    /* update for p */
    /* YE */
    private DMatrixRMaj pUpdate(DMatrixRMaj C, int Q, int n, int nPlus) {
        DMatrixRMaj P = new DMatrixRMaj(C.numRows, C.numCols);
        double gTotal = (n + nPlus) + Q * Q;
        this.n += this.nPlus;
        CommonOps_DDRM.divide(C, gTotal, P);
        return P;
    }

    /* update for s */
    /* YE */
    private void sUpdate(DMatrixRMaj Dr, DMatrixRMaj P, DMatrixRMaj r) {
        DMatrixRMaj B = new DMatrixRMaj(Dr.numRows, Dr.numCols);
        CommonOps_DDRM.elementPower(-1/2, Dr, B);
        this.B = B;
        DMatrixRMaj rrT = new DMatrixRMaj(r.numRows, r.numRows);
        CommonOps_DDRM.multTransB(r, r, rrT);
        DMatrixRMaj mid = new DMatrixRMaj(P.numRows, P.numCols);
        CommonOps_DDRM.subtract(P, rrT, mid);
        DMatrixRMaj A = new DMatrixRMaj(B.numRows, mid.numCols);
        CommonOps_DDRM.mult(B, mid, A);
        this.A = A;
        DMatrixRMaj S = new DMatrixRMaj();
        CommonOps_DDRM.mult(A, B, S);
        CommonOps_DDRM.addEquals(this.S, S);
    }

    /* creates our modification matrices, updates S, P, A, B */
    private void modSuite(DMatrixSparseCSC Z) {
        DMatrixRMaj C = burt(Z);
        DMatrixRMaj P = pUpdate(C, this.Q, this.n, this.nPlus);
        DMatrixRMaj r = makeR(P);
        DMatrixRMaj Dr = makeDr(r);
        sUpdate(Dr, P, r);



    }

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

    private void updateSVD(DMatrixRMaj U, DMatrixRMaj S,
                           DMatrixRMaj Vt, DMatrixRMaj A, DMatrixRMaj B) {
        DMatrixRMaj Pa;
        DMatrixRMaj Ra;
        DMatrixRMaj Pb;
        DMatrixRMaj Rb;
        QRColPivDecompositionHouseholderColumn_DDRM QR
                = new QRColPivDecompositionHouseholderColumn_DDRM();
        DMatrixRMaj uTa = new DMatrixRMaj();
        DMatrixRMaj vTb = new DMatrixRMaj();
        CommonOps_DDRM.multTransA(U, A, uTa);
        CommonOps_DDRM.multTransA(Vt, B, vTb);
        DMatrixRMaj uuTa = new DMatrixRMaj();
        DMatrixRMaj vvTb = new DMatrixRMaj();
        CommonOps_DDRM.mult(U, uTa, uuTa);
        CommonOps_DDRM.multTransA(Vt, vTb, vvTb);
        DMatrixRMaj QRa = new DMatrixRMaj();
        DMatrixRMaj QRb = new DMatrixRMaj();
        CommonOps_DDRM.subtract(A, uuTa, QRa);
        CommonOps_DDRM.subtract(B, vvTb, QRb);
        QR.decompose(QRa);
    }

    public static void main(String[] args) {
        MCA fork = new MCA();


    }




    /*
    ##############################################
                        Update
    ##############################################
     */



}
