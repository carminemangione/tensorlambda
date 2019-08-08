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

/*
    - Usage:
        - instantiate IterSVD class
        - call updateSVD
        - use getters to pull updated versions of our SVD
 */
public class IterSVD {


    private DMatrixRMaj U, Sig, V, A, B;
    private int r, c;


    public IterSVD(int r, int c, DMatrixRMaj U, DMatrixRMaj Sig,
                   DMatrixRMaj V, DMatrixRMaj A, DMatrixRMaj B) {
        this.U = U;
        this.Sig = Sig;
        this.V = V;
        this.A = A;
        this.B = B;
        this.r = r;
        this.c = c;

        svdUpdate();
    }


    public DMatrixRMaj getU() {
        return this.U;
    }

    public DMatrixRMaj getSig() {
        return this.Sig;
    }

    public DMatrixRMaj getV() {
        return this.V;
    }

    /* Equation 2 */
    private HashMap<String, DMatrixRMaj> eqTwo(DMatrixRMaj U, DMatrixRMaj V,
                                                   DMatrixRMaj A, DMatrixRMaj B) {
        HashMap<String, DMatrixRMaj> retMap = new HashMap<>();
        DMatrixRMaj Pa, Ra, Pb, Rb, uTa, vTb, uuTa, vvTb, QRa, QRb;
        QRColPivDecompositionHouseholderColumn_DDRM QR
                = new QRColPivDecompositionHouseholderColumn_DDRM();
        uTa = new DMatrixRMaj(U.numCols, A.numCols);
        vTb = new DMatrixRMaj(V.numCols, B.numCols);
        CommonOps_DDRM.multTransA(U, A, uTa);
        retMap.put("uTa", uTa);
        CommonOps_DDRM.multTransA(V, B, vTb);
        retMap.put("vTb", vTb);
        uuTa = new DMatrixRMaj(U.numRows, uTa.numCols);
        vvTb = new DMatrixRMaj(V.numRows, vTb.numCols);
        CommonOps_DDRM.mult(U, uTa, uuTa);
        CommonOps_DDRM.mult(V, vTb, vvTb);
        QRa = new DMatrixRMaj(U.numRows, uTa.numCols);
        QRb = new DMatrixRMaj(V.numRows, vTb.numCols);
        CommonOps_DDRM.subtract(A, uuTa, QRa);
        CommonOps_DDRM.subtract(B, vvTb, QRb);
        Pa = new DMatrixRMaj(QRa.numRows, QRa.numCols);
        Ra = new DMatrixRMaj(QRa.numCols, QRa.numCols);
        System.out.println(QRa);
        System.out.println(QRb);
        QR.decompose(QRa);
        QR.getQ(Pa,false);
        QR.getR(Ra, false);
        retMap.put("Pa", Pa);
        retMap.put("Ra", Ra);
        Pb = new DMatrixRMaj(QRb.numRows, QRb.numCols);
        Rb = new DMatrixRMaj(QRb.numCols, QRb.numCols);
        QR.decompose(QRb);
        QR.getQ(Pb,false);
        QR.getR(Rb, false);
        retMap.put("Pb", Pb);
        retMap.put("Rb", Rb);
//        System.out.println(Pa);
//        System.out.println(Ra);
//        System.out.println(Pb);
//        System.out.println(Rb);
        return retMap;
    }

    /* Equation 4 */
    private DMatrixRMaj eqFour(DMatrixRMaj Sig, DMatrixRMaj uTa,
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

    /* taking SVD of K */
    private HashMap<String, DMatrixRMaj> kSVD(DMatrixRMaj K) {
        HashMap<String, DMatrixRMaj> svdMap = new HashMap<>();
        DMatrixRMaj U_, Sig_, V_;
        U_ = new DMatrixRMaj();
        Sig_ = new DMatrixRMaj();
        V_ = new DMatrixRMaj();
        SvdImplicitQrDecompose_DDRM svd = new SvdImplicitQrDecompose_DDRM(false, true,
                true, true);
        svd.decompose(K);
        svd.getU(U_, false);
        svdMap.put("U_", U_);
        svd.getW(Sig_);
        svdMap.put("Sig_", Sig_);
        svd.getV(V_, false);
        svdMap.put("V_", V_);
        return svdMap;
    }

    /* taking our Rank(r + c) svd */
    private HashMap<String, DMatrixRMaj> rankRC(HashMap<String, DMatrixRMaj> svdMap, int r, int c) {
        HashMap<String, DMatrixRMaj> rcMap = new HashMap<>();
        DMatrixRMaj Ur, Sigr, Vr;
        int rc = r + c;
        int uRows = svdMap.get("U_").numRows;
        int sigRows = svdMap.get("Sig_").numRows;
        int vRows = svdMap.get("V_").numRows;
        Ur = new DMatrixRMaj(uRows, rc);
        Sigr = new DMatrixRMaj(sigRows, rc);
        Vr = new DMatrixRMaj(vRows, rc);
        CommonOps_DDRM.extract(svdMap.get("U_"), 0, uRows, 0, rc + 1, Ur);
        CommonOps_DDRM.extract(svdMap.get("Sig_"), 0, sigRows, 0, rc + 1, Sigr);
        CommonOps_DDRM.extract(svdMap.get("V_"), 0, vRows, 0, rc + 1, Vr);
        this.Sig = Sigr;
        rcMap.put("U_", Ur);
        rcMap.put("V_", Vr);
        return rcMap;

    }

    /* equation 5 */
    private void eqFive(DMatrixRMaj U, DMatrixRMaj V, DMatrixRMaj U_,
                        DMatrixRMaj V_, DMatrixRMaj Pa, DMatrixRMaj Pb) {
        DMatrixRMaj UPa, U__, VPb, V__;
        UPa = new DMatrixRMaj();
        CommonOps_DDRM.concatColumns(U, Pa, UPa);
        U__ = new DMatrixRMaj();
        CommonOps_DDRM.mult(UPa, U_, U__);
        this.U = U__;
        VPb = new DMatrixRMaj();
        CommonOps_DDRM.concatColumns(V, Pb, VPb);
        V__ = new DMatrixRMaj();
        CommonOps_DDRM.mult(VPb, V_, V__);
        this.V = V__;
    }

    /* wrapper for entire svd update procedure */
    public void svdUpdate() {
        HashMap<String, DMatrixRMaj> eqTwoMap ,kSVDMap, rcMap;
        DMatrixRMaj K;
        eqTwoMap = eqTwo(this.U, this.V, this.A, this.B);
        K = eqFour(this.Sig, eqTwoMap.get("uTa"),
                            eqTwoMap.get("vTb"), eqTwoMap.get("Ra"), eqTwoMap.get("Rb"));
        kSVDMap = kSVD(K);
        rcMap = rankRC(kSVDMap, this.r, this.c);
        eqFive(this.U, this.V, kSVDMap.get("U_"),
                kSVDMap.get("V_"), eqTwoMap.get("Pa"), eqTwoMap.get("Pb"));
    }

    public static void main(String[] args) {
        int i, j, ctr = 0, r = 2, c = 0;
        DMatrixRMaj U = new DMatrixRMaj(3,3);
        DMatrixRMaj Sig = new DMatrixRMaj(3,3);
        DMatrixRMaj V = new DMatrixRMaj(3,3);
        DMatrixRMaj A = new DMatrixRMaj(3,3);
        DMatrixRMaj B = new DMatrixRMaj(3,3);
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                U.set(i, j, ctr);
                Sig.set(i, j, ctr);
                V.set(i, j, ctr);
                A.set(i, j, ctr);
                B.set(i, j, ctr);
                ctr++;
            }
        }

        /* eqTwo with these matrices, all these should be within some
            small epsilon of these digits
(array([[-0.13471586,  0.90287594,  0.40824829],
       [-0.49620342,  0.29515335, -0.81649658],
       [-0.85769097, -0.31256924,  0.40824829]]),
 array([[ 1.33614558e+03,  1.63602982e+03,  1.93591406e+03],
       [ 0.00000000e+00, -6.65469985e-01, -1.33093997e+00],
       [ 0.00000000e+00,  0.00000000e+00, -1.60597618e-14]]))
(array([[-0.13471586,  0.90287594,  0.40824829],
       [-0.49620342,  0.29515335, -0.81649658],
       [-0.85769097, -0.31256924,  0.40824829]]),
 array([[ 1.33614558e+03,  1.63602982e+03,  1.93591406e+03],
       [ 0.00000000e+00, -6.65469985e-01, -1.33093997e+00],
       [ 0.00000000e+00,  0.00000000e+00, -1.60597618e-14]]))

        eqFour with these matrices
        [[ 8.91000000e+03  1.09090000e+04  1.29080000e+04  2.70434746e+05
                -1.19784597e+02 -1.01176499e-12]
        [ 1.09110000e+04  1.33600000e+04  1.58090000e+04  3.31131125e+05
                -1.47734337e+02 -1.25266142e-12]
 [ 1.29120000e+04  1.58110000e+04  1.87100000e+04  3.91827504e+05
                -1.75684076e+02 -1.49355785e-12]
 [ 2.70434746e+05  3.31131125e+05  3.91827504e+05  8.20964179e+06
                -3.66531413e+03 -3.10903186e-11]
 [-1.19784597e+02 -1.47734337e+02 -1.75684076e+02 -3.66531413e+03
        2.21425151e+00  2.13745789e-14]
 [-1.01176499e-12 -1.25266142e-12 -1.49355785e-12 -3.10903186e-11
        2.13745789e-14  2.57915950e-28]]

         */
        IterSVD kapow = new IterSVD(r, c, U, Sig, V, A, B);


    }


}

