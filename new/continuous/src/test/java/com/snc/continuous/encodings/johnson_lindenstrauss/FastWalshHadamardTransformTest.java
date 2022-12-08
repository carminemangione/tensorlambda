package com.mangione.continuous.encodings.johnson_lindenstrauss;

import com.mangione.continuous.encodings.johnson_lindenstrauss.FastWalshHadamardTransform;
import org.apache.commons.math3.transform.FastHadamardTransformer;
import org.ejml.data.DMatrixRMaj;

import org.ejml.dense.row.CommonOps_DDRM;
import org.junit.Test;

import static org.junit.Assert.*;

public class FastWalshHadamardTransformTest {
    @Test
    public void TestSumBits() {
        int p = 3;
        assertEquals(2, FastWalshHadamardTransform.sum_bits(3));
    }

    @Test
    public void TestIsPowerOfTwo() {
        assertFalse(FastWalshHadamardTransform.is_power_of_two(0));
        assertTrue(FastWalshHadamardTransform.is_power_of_two(1));
        assertFalse(FastWalshHadamardTransform.is_power_of_two(3));
    }

    @Test
    public void TestHadamardCoefficient2X2() {
        assertEquals(1.0, FastWalshHadamardTransform.hadamard_coefficient(0, 0), 0.0);
        assertEquals(1.0, FastWalshHadamardTransform.hadamard_coefficient(0, 1), 0.0);
        assertEquals(1.0, FastWalshHadamardTransform.hadamard_coefficient(1, 0), 0.0);
        assertEquals(-1.0, FastWalshHadamardTransform.hadamard_coefficient(1, 1), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestFWHT0x0() {
        int p = 0;
        FastWalshHadamardTransform fastWalshHadamardTransform = new FastWalshHadamardTransform(p);
    }

    @Test
    public void TestFWHT1x1() {
        int p = 1;
        DMatrixRMaj input = new DMatrixRMaj(p, 1);
        FastWalshHadamardTransform fastWalshHadamardTransform = new FastWalshHadamardTransform(p);
        for (int column = 0; column < p; column++) {
            input.zero();
            input.set(column, 0, 1.0);
            DMatrixRMaj output = fastWalshHadamardTransform.apply(input);
            for (int row = 0; row < p; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, column), output.get(row, 0), 0.0);
            }
        }
    }

    @Test
    public void TestFWHT2x2() {
        int p = 2;
        DMatrixRMaj input = new DMatrixRMaj(p, 1);
        FastWalshHadamardTransform fastWalshHadamardTransform = new FastWalshHadamardTransform(p);
        for (int column = 0; column < p; column++) {
            input.zero();
            input.set(column, 0, 1.0);
            DMatrixRMaj output = fastWalshHadamardTransform.apply(input);
            for (int row = 0; row < p; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, column), output.get(row, 0), 0.0);
            }
        }
    }
    
    @Test
    public void TestFWHT4x4() {
        int p = 4;
        DMatrixRMaj input = new DMatrixRMaj(p, 1);
        FastWalshHadamardTransform fastWalshHadamardTransform = new FastWalshHadamardTransform(p);
        for (int column = 0; column < p; column++) {
            input.zero();
            input.set(column, 0, 1.0);
            DMatrixRMaj output = fastWalshHadamardTransform.apply(input);
            for (int row = 0; row < p; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, column), output.get(row, 0), 0.0);
            }
        }
    }

    @Test
    public void TestFWHT8x8() {
        int p = 8;
        DMatrixRMaj input = new DMatrixRMaj(p, 1);
        FastWalshHadamardTransform fastWalshHadamardTransform = new FastWalshHadamardTransform(p);
        for (int column = 0; column < p; column++) {
            input.zero();
            input.set(column, 0, 1.0);
            DMatrixRMaj output = fastWalshHadamardTransform.apply(input);
            for (int row = 0; row < p; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, column), output.get(row, 0), 0.0);
            }
        }
    }

    @Test
    public void TestLeanFWHT8x8() {
        int numRows = 8;
        DMatrixRMaj output = new DMatrixRMaj(numRows, 1);
        for (int e = 0; e < numRows; e++) {
            output.zero();
            output.set(e, 0, 1.0);
            FastWalshHadamardTransform.apply_in_place(output, e + 1);
            for (int row = 0; row < numRows; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, e), output.get(row, 0), 0.0);
            }
        }
    }

    @Test
    public void TestPopulateWHColumn8X8(){
        int numRows = 8;
        DMatrixRMaj output = new DMatrixRMaj(numRows, 1);
        for (int e = 0; e < numRows; e++) {
            output.zero();
            output.set(e, 0, 1.0);
            FastWalshHadamardTransform.populateWHColumn(output.data,output.getNumRows(),e);
            for (int row = 0; row < numRows; row++) {
                assertEquals(FastWalshHadamardTransform.hadamard_coefficient(row, e), output.get(row, 0), 0.0);
            }
        }
    }

    @Test
    public void TestInnerProduct(){
        int columns = 16;
        int rows = 4;
        DMatrixRMaj TestMatrix = new DMatrixRMaj(rows,columns);
        TestMatrix.zero();
        for(int i=0;i<rows;i++){
            TestMatrix.set(i,i,1.0);
        }
        DMatrixRMaj TransformedMatrix = TestMatrix.createLike();
        TransformedMatrix.zero();
        DMatrixRMaj MatrixOfInnerProducts = new DMatrixRMaj(rows,rows);
        TransformedMatrix.set(TestMatrix);
        for(int r=0;r<rows;r++)
            FastWalshHadamardTransform.apply_in_place(TransformedMatrix.data,columns, r+1, r * columns);
        CommonOps_DDRM.multTransB(TransformedMatrix,TransformedMatrix,MatrixOfInnerProducts);
        CommonOps_DDRM.scale(1.0/16.0,MatrixOfInnerProducts);
        for(int r=0;r<rows;r++){
            for(int c=0;c<rows;c++) assertEquals(r == c ? 1.0 : 0.0, MatrixOfInnerProducts.get(r, c), 1.0e-12);
        }
    }

    @Test
    public void TestInnerProductFaster(){
        int columns = 16;
        int rows = 4;
        DMatrixRMaj TestMatrix = new DMatrixRMaj(rows,columns);
        TestMatrix.zero();
        for(int i=0;i<rows;i++){
            TestMatrix.set(i,i,1.0);
        }
        DMatrixRMaj TransformedMatrix = TestMatrix.createLike();
        TransformedMatrix.set(TestMatrix);
        for(int r=0;r<rows;r++)
            FastWalshHadamardTransform.populateWHColumn(TransformedMatrix.data,columns,r,r*columns);
        DMatrixRMaj MatrixOfInnerProducts = new DMatrixRMaj(rows,rows);
        CommonOps_DDRM.multTransB(TransformedMatrix,TransformedMatrix,MatrixOfInnerProducts);
        CommonOps_DDRM.scale(1.0/16.0,MatrixOfInnerProducts);
        for(int r=0;r<rows;r++){
            for(int c=0;c<rows;c++) assertEquals(r == c ? 1.0 : 0.0, MatrixOfInnerProducts.get(r, c), 1.0e-12);
        }
    }
}
