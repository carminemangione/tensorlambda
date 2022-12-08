package com.mangione.continuous.classifiers.unsupervised;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.sparse.csc.CommonOps_DSCC;


//import sun.java2d.marlin.DMarlinRenderingEngine;


/* gotta run this stuff iteratively - calling sabUpdate then itersvd until our batches have run out */
public class MCAIter {

    private int n, nPlus, Q;

    private DMatrixRMaj A, B, Dr;

    /* Args:
        r - the rank of our svd
        batchSize - the size of the batches we use to iteratively update SVD

        What it does:
        Conducts an MCA transform on the data
     */

    public MCAIter(int nPlus, int Q) {
        this.nPlus = nPlus;
        this.Q = Q;
        this.n = 0;

    }
    public void findAB(DMatrixSparseCSC Z, Boolean firstIteration) {
        modSuite(Z, firstIteration);
    }

    public DMatrixRMaj getA() {
        return A;
    }

    public DMatrixRMaj getB() {
        return B;
    }

    /* returns single column vector of row sums */

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
    private DMatrixRMaj sparseToDense(DMatrixSparseCSC A) {
        int cols = A.numCols;
        DMatrixRMaj I = CommonOps_DDRM.identity(cols);
        DMatrixRMaj C = new DMatrixRMaj(cols, cols);
        CommonOps_DSCC.mult(A, I, C);
        return C;
    }

    /* makes burt matrix */
    private DMatrixRMaj burt(DMatrixSparseCSC Z) {
        DMatrixSparseCSC lowerC = new DMatrixSparseCSC(Z.numRows, Z.numRows);
        CommonOps_DSCC.innerProductLower(Z, lowerC, null, null);

        DMatrixSparseCSC C = new DMatrixSparseCSC(Z.numCols, Z.numCols);
        CommonOps_DSCC.symmLowerToFull(lowerC, C, null);
        return sparseToDense(C);
    }

    /* update for p */
    private DMatrixRMaj pUpdate(DMatrixRMaj C, int Q, int n, int nPlus) {
        DMatrixRMaj P = new DMatrixRMaj(C.numRows, C.numCols);
        double gTotal = (n + nPlus) + Q * Q;
        this.n += this.nPlus;
        CommonOps_DDRM.divide(C, gTotal, P);
        return P;
    }

    /* update for s */
    private void abUpdate(DMatrixRMaj Dr, DMatrixRMaj P, DMatrixRMaj r) {
        DMatrixRMaj B = new DMatrixRMaj(Dr.numRows, Dr.numCols);
        CommonOps_DDRM.elementPower(-1./2, Dr, B);
        this.B = B;
        DMatrixRMaj rrT = new DMatrixRMaj(r.numRows, r.numRows);
        CommonOps_DDRM.multTransB(r, r, rrT);
        DMatrixRMaj mid = new DMatrixRMaj(P.numRows, P.numCols);
        CommonOps_DDRM.subtract(P, rrT, mid);
        DMatrixRMaj A = new DMatrixRMaj(B.numRows, mid.numCols);
        CommonOps_DDRM.mult(B, mid, A);
        this.A = A;
    }

    /* creates our modification matrices, updates S, P, A, B */
    private void modSuite(DMatrixSparseCSC Z, Boolean firstIteration) {
        DMatrixRMaj C = burt(Z);
        DMatrixRMaj P = pUpdate(C, this.Q, this.n, this.nPlus);
        DMatrixRMaj r = makeR(P);
        if(firstIteration) {
            this.Dr = makeDr(r);
        }
        abUpdate(this.Dr, P, r);



    }

    public MCAIter() {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(5, 3);
        int i, j, ctr = 0;
        for(i = 0; i < 2; i++) {
            for(j = 0; j < 3; j++) {
                Z.set(i, j, ctr);
                ctr ++;
            }
        }
        DMatrixRMaj b = new DMatrixRMaj(3,3);
        DMatrixRMaj c = new DMatrixRMaj(4,3);
        DMatrixRMaj e = new DMatrixRMaj(7, 3);

//        this.Q = 3;
//        this.n = 2;
//        this.nPlus = 0;
//        this.Sig = new DMatrixRMaj(this.r, this.r);
//        modSuite((Z));
    }

    



}
