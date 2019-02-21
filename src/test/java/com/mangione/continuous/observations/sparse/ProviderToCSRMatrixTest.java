package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ProviderToCSRMatrixTest {
    @Test
    public void csr4x6() {
        /* Uses the following matrix:
        {10, 20, 0, 0, 0, 0}
        {0, 30, 0, 40, 0, 0}
        {0, 0, 50, 60, 70, 0}
        {0, 0, 0, 0, 0, 80}
        */

        List<SparseObservation<Integer>> sparseObservations = new ArrayList<>();
        SparseObservation<Integer> sparseObservation = new SparseObservation<>(6, 0);
        sparseObservation.setFeature(0, 10);
        sparseObservation.setFeature(1, 20);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(6, 0);
        sparseObservation.setFeature(1, 30);
        sparseObservation.setFeature(3, 40);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(6, 0);
        sparseObservation.setFeature(2, 50);
        sparseObservation.setFeature(3, 60);
        sparseObservation.setFeature(4, 70);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(6, 0);
        sparseObservation.setFeature(5, 80);
        sparseObservations.add(sparseObservation);

        ArrayObservationProvider<Integer, SparseObservation<Integer>> observations =
                new ArrayObservationProvider<Integer, SparseObservation<Integer>>(sparseObservations,
                        (data, columns) -> new SparseObservation<>(data.size(), 0));

        ProviderToCSRMatrix<Integer, SparseObservation<Integer>,
                ArrayObservationProvider<Integer, SparseObservation<Integer>>> providerToCSRMatrix = new ProviderToCSRMatrix<>(observations);


        double[] values = providerToCSRMatrix.getValues();
        int[] columnIndexes = providerToCSRMatrix.getColumnIndexes();
        int[] rowIndexes = providerToCSRMatrix.getRowIndexes();
        assertArrayEquals(new double[]{10, 20, 30, 40, 50, 60, 70, 80}, values, 0);
        assertArrayEquals(new int[] {0, 2, 4, 7, 8}, rowIndexes);
        assertArrayEquals(new int[] {0, 1, 1, 3, 2, 3, 4, 5}, columnIndexes);
    }


}