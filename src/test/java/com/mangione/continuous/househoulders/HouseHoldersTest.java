package com.mangione.continuous.househoulders;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

public class HouseHoldersTest {
    @Test
    public void testHouseHolders() throws Exception {
        double[][] a = {{2, 2, 4, 2}, {1, 3, -2, 1}, {3, 1, 3, 2}};
        SimpleMatrix X = new SimpleMatrix(a);
        SimpleMatrix hx = houseHoldersOnX(X);
    }

    private SimpleMatrix houseHoldersOnX(SimpleMatrix x) {
        double[][] x1 = {{2}, {1}, {3}};
        SimpleMatrix X1 = new SimpleMatrix(x1);

        double magX1 = Math.sqrt(X1.dot(X1));
        double[][] e1 = {{1}, {0}, {0}};

        SimpleMatrix E1 = new SimpleMatrix(e1);
        SimpleMatrix uminus = X1.minus(E1.scale(magX1));

        SimpleMatrix vt = uminus.transpose().mult(x);

        System.out.println(vt);
        double mag2 = uminus.dot(uminus);
        SimpleMatrix HX = x.minus(uminus.mult(uminus.transpose()).mult(x).scale(2/mag2));

        System.out.println(HX);
        return HX;
    }
}
