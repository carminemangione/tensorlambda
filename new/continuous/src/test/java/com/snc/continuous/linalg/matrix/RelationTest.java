package com.mangione.continuous.linalg.matrix;


import org.ejml.data.DMatrixRMaj;
import org.junit.Test;

public class RelationTest {
    @Test
    public void testSomething() {
        MatrixRelation relation = new MatrixRelation(8, 8);
        for (int i = 0; i < 3; i++) {
            relation.add(i * 2, i * 3, 1.0);
        }
        MatrixRelation.createGatherRelation(relation.getColumnCount(),relation.columnSet()).convert((DMatrixRMaj)null).print();
        MatrixRelation.createGatherRelation(relation.getRowCount(),relation.rowSet()).transpose().convert((DMatrixRMaj)null).print();
    }
}
