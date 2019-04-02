package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.ObservationToExemplarProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ProviderToCSRMatrixWithTargetTest {
    @Test
    public void withTargetsTest() {
        List<SparseObservation<Integer>> sparseObservations = new ArrayList<>();
        SparseObservation<Integer> sparseObservation = new SparseObservation<>(3, 0);
        sparseObservation.setFeature(0, 1);
        sparseObservation.setFeature(2, 2);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(3, 0);
        sparseObservation.setFeature(2, 3);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(3, 0);
        sparseObservation.setFeature(0, 4);
        sparseObservation.setFeature(1, 5);
        sparseObservation.setFeature(2, 6);
        sparseObservations.add(sparseObservation);

        ListObservationProvider<Integer, SparseObservation<Integer>> observations =
                new ListObservationProvider<>(sparseObservations);
        ObservationToExemplarProvider<Integer, SparseExemplar<Integer>> exemplars =
                new ObservationToExemplarProvider<>(observations,
                        obs -> new SparseExemplar<>(obs.getFeatures(), obs.getColumnIndexes(), 3, 0, 1));


        ProviderToCSRMatrixWithTarget<Integer, SparseExemplar<Integer>,
                ObservationProviderInterface<Integer, SparseExemplar<Integer>>> providerToCSRMatrix =
                new ProviderToCSRMatrixWithTarget<>(exemplars);

        double[] values = providerToCSRMatrix.getValues();
        int[] columnIndexes = providerToCSRMatrix.getColumnIndexes();
        int[] rowIndexes = providerToCSRMatrix.getRowIndexes();
        assertArrayEquals(new double[]{1, 2, 3, 4, 5}, values, 0);
        assertArrayEquals(new int[] {0, 2, 3}, rowIndexes);
        assertArrayEquals(new int[] {0, 2, 3, 4, 5}, columnIndexes);
        assertArrayEquals(new int[] {2, 3, 6}, providerToCSRMatrix.getTargets());
    }

}