package com.mangione.continuous.classifiers.unsupervised;

import static org.junit.Assert.*;

import org.ejml.data.DMatrixRMaj;
import org.junit.Test;

public class IterSVDTest {
    @Test
    public void t1() {
        int i, j, ctr = 1, r = 3, c = 0;
        int[] b = new int[]{1,2,2,3,4,5,23,7,8};
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
                B.set(i, j, b[ctr - 1]);
                ctr++;
            }
        }

        IterSVD kapow = new IterSVD(r, c, U, Sig, V, A, B);
        System.out.println(kapow.getU());
        System.out.println(kapow.getSig());
        System.out.println(kapow.getV());

        /*compare to other dmatrixrmaj's, all you have to do is make a double array and insert it into the constructore */
        /* its fine if within some sort of epsilon of the value */
    }
}
