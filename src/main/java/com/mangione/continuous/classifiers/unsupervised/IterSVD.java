package com.mangione.continuous.classifiers.unsupervised;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.decomposition.qr.QRColPivDecompositionHouseholderColumn_DDRM;
import org.ejml.dense.row.decomposition.svd.SvdImplicitQrDecompose_DDRM;

import java.util.HashMap;


/*
  - Based on implementation from Brand's Fast low-rank modifications of the thin singular value
        decomposition (2006), section 2
  - Should be called in loop by another class
 */
public class IterSVD {
    private DMatrixRMaj U;
    private DMatrixRMaj Sig;
    private DMatrixRMaj Vt;


    /*
    ##############################################
                        Update
    ##############################################
     */

    public IterSVD(DMatrixRMaj U, DMatrixRMaj Sig,
                   DMatrixRMaj Vt, DMatrixRMaj A, DMatrixRMaj B) {
        HashMap<String, DMatrixRMaj> holder;
        holder = updateSVD(U, Vt, A, B);

    }

    public DMatrixRMaj getU() {
        return this.U;
    }

    public DMatrixRMaj getSig() {
        return this.Sig;
    }

    public DMatrixRMaj getVt() {
        return this.Vt;
    }

    /* Equation 2 */
    private HashMap<String, DMatrixRMaj> updateSVD(DMatrixRMaj U, DMatrixRMaj Vt,
                                                   DMatrixRMaj A, DMatrixRMaj B) {
        HashMap<String, DMatrixRMaj> retMap = new HashMap<>();
        DMatrixRMaj Pa, Ra, Pb, Rb, uTa, vTb, uuTa, vvTb, QRa, QRb;
        QRColPivDecompositionHouseholderColumn_DDRM QR
                = new QRColPivDecompositionHouseholderColumn_DDRM();
        uTa = new DMatrixRMaj();
        vTb = new DMatrixRMaj();
        CommonOps_DDRM.multTransA(U, A, uTa);
        retMap.put("uTa", uTa);
        CommonOps_DDRM.multTransA(Vt, B, vTb);
        retMap.put("vTb", vTb);
        uuTa = new DMatrixRMaj();
        vvTb = new DMatrixRMaj();
        CommonOps_DDRM.mult(U, uTa, uuTa);
        CommonOps_DDRM.multTransA(Vt, vTb, vvTb);
        QRa = new DMatrixRMaj();
        QRb = new DMatrixRMaj();
        CommonOps_DDRM.subtract(A, uuTa, QRa);
        CommonOps_DDRM.subtract(B, vvTb, QRb);
        Pa = new DMatrixRMaj(QRa.numRows, QRa.numCols);
        Ra = new DMatrixRMaj(QRa.numCols, QRa.numCols);
        QR.decompose(QRa);
        QR.getQ(Pa,false);
        QR.getR(Ra, false);
        retMap.put("Pa", Pa);
        retMap.put("Ra", Ra);
        Pb = new DMatrixRMaj(QRb.numRows, QRb.numCols);
        Rb = new DMatrixRMaj(QRb.numCols, QRb.numCols);
        QR.decompose(QRb);
        QR.getQ(Pa,false);
        QR.getR(Ra, false);
        retMap.put("Pb", Pb);
        retMap.put("Rb", Rb);
        return retMap;
    }

    /* Equation 4 */
    private DMatrixRMaj makeK(DMatrixRMaj Sig, DMatrixRMaj uTa,
                              DMatrixRMaj uTb, DMatrixRMaj Ra, DMatrixRMaj Rb) {
        int aRows = uTa.numRows + Ra.numRows;
        int bRows = uTb.numRows + Rb.numRows;
        DMatrixRMaj uTaRa = new DMatrixRMaj(aRows, uTa.numCols);
        DMatrixRMaj uTbRb = new DMatrixRMaj(bRows, uTb.numCols);
        CommonOps_DDRM.concatRows(uTa, Ra, uTaRa);
        CommonOps_DDRM.concatRows(uTb, Rb, uTbRb);
        DMatrixRMaj aTimesBt = new DMatrixRMaj(aRows, bRows);
        CommonOps_DDRM.multTransB(uTaRa, uTbRb, aTimesBt);
        DMatrixRMaj K = new DMatrixRMaj(aRows, bRows);
        int i, j;
        for(i = 0; i < Sig.numRows; i++){
            for(j = 0; j < Sig.numCols; j++) {
                K.set(i, j, Sig.get(i,j));
            }
        }
        CommonOps_DDRM.addEquals(K, aTimesBt);
        return K;
    }

//    /* taking SVD of K */
//    private DMatrixRMaj kSVD(DMatrixRMaj K) {
//        SvdImplicitQrDecompose_DDRM svd = new SvdImplicitQrDecompose_DDRM(false, true,
//                true, true);
//        svd.decompose(K);
//        svd.getU()
//
//    }
}

