package com.mangione.continuous.classifiers.unsupervised.nearestneighbor;

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


    public IterSVD(int pvNumCols) {
        this.U = new DMatrixRMaj(pvNumCols, pvNumCols);
        this.Sig = new DMatrixRMaj(pvNumCols, pvNumCols);
        this.V = new DMatrixRMaj(pvNumCols, pvNumCols);

    }
    public void svdUpdate(int r, int c, DMatrixRMaj A, DMatrixRMaj B) {
        this.A = A;
        this.B = B;
        this.r = r;
        this.c = c;

        svdUpdateSuite();
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
        U_ = new DMatrixRMaj(K.numRows, K.numCols);
        Sig_ = new DMatrixRMaj(K.numCols, K.numCols);
        V_ = new DMatrixRMaj(K.numCols, K.numCols);
        SvdImplicitQrDecompose_DDRM svd = new SvdImplicitQrDecompose_DDRM(false, true,
                true, false);
//        SafeSvd_DDRM svd = new SafeSvd_DDRM();
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
    private HashMap<String, DMatrixRMaj> rankRC(DMatrixRMaj U_, DMatrixRMaj Sig_, DMatrixRMaj V_, int r, int c) {
        int i, j, rc = r + c;
        assert(rc < V_.numRows);
        HashMap<String, DMatrixRMaj> rcMap = new HashMap<>();
        for(i = Sig_.numCols - 1; i > rc - 1; i--) {
            for(j = 0; j < U_.numRows; j++) {
                U_.set(i,j,0);
                if(i == j) {
                    Sig_.set(i,j,0);
                }
                V_.set(j, i, 0);
            }
        }
        rcMap.put("Ur", U_);
        this.Sig = Sig_;
        rcMap.put("Vr", V_);
        return rcMap;
    }

    /* equation 5 */
    private void eqFive(DMatrixRMaj U, DMatrixRMaj V, DMatrixRMaj Ur,
                        DMatrixRMaj Vr, DMatrixRMaj Pa, DMatrixRMaj Pb) {
        DMatrixRMaj UPa, U__, VPb, V__;
        UPa = new DMatrixRMaj(U.numRows, U.numCols + Pa.numCols);
        CommonOps_DDRM.concatColumns(U, Pa, UPa);
        U__ = new DMatrixRMaj(UPa.numRows, Ur.numCols);
        CommonOps_DDRM.mult(UPa, Ur, U__);
        this.U = U__;
        VPb = new DMatrixRMaj(V.numRows, V.numCols + Pb.numCols);
        CommonOps_DDRM.concatColumns(V, Pb, VPb);
        V__ = new DMatrixRMaj(VPb.numRows, Vr.numCols);
        CommonOps_DDRM.mult(VPb, Vr, V__);
        this.V = V__;
    }

    /* wrapper for entire svd update procedure */
    private void svdUpdateSuite() {
        HashMap<String, DMatrixRMaj> eqTwoMap ,kSVDMap, rcMap;
        DMatrixRMaj K;
        eqTwoMap = eqTwo(this.U, this.V, this.A, this.B);
        K = eqFour(this.Sig, eqTwoMap.get("uTa"), eqTwoMap.get("vTb"), eqTwoMap.get("Ra"), eqTwoMap.get("Rb"));
        kSVDMap = kSVD(K);
        rcMap = rankRC(kSVDMap.get("U_"), kSVDMap.get("Sig_"), kSVDMap.get("V_"), this.r, this.c);
        eqFive(this.U, this.V, rcMap.get("Ur"), rcMap.get("Vr"), eqTwoMap.get("Pa"), eqTwoMap.get("Pb"));
    }

}

