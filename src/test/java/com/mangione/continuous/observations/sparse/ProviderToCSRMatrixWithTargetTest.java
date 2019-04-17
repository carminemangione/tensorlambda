package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.ObservationToExemplarProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;

public class ProviderToCSRMatrixWithTargetTest {
    @Test
    public void withTargetsTest() {
        List<SparseObservation<Integer>> sparseObservations = new ArrayList<>();
        SparseObservation<Integer> sparseObservation = new SparseObservation<>(7, 0);
        sparseObservation.setFeature(0, 1);
        sparseObservation.setFeature(2, 2);
        sparseObservation.setFeature(6, 15);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(7, 0);
        sparseObservation.setFeature(2, 3);
        sparseObservation.setFeature(6, 16);
        sparseObservations.add(sparseObservation);
        sparseObservation = new SparseObservation<>(7, 0);
        sparseObservation.setFeature(0, 4);
        sparseObservation.setFeature(1, 5);
        sparseObservation.setFeature(2, 6);
        sparseObservation.setFeature(6, 17);
        sparseObservations.add(sparseObservation);

        ListObservationProvider<Integer, SparseObservation<Integer>> observations =
                new ListObservationProvider<>(sparseObservations);
        ObservationToExemplarProvider<Integer, SparseExemplar<Integer>> exemplars =
                new ObservationToExemplarProvider<>(observations,
                        obs -> {
                            List<Integer> columns = new ArrayList<>(obs.getColumnIndexes());
                            Collections.sort(columns);
                            List<Integer> features = columns.stream()
                                    .map(obs::getFeature)
                                    .collect(Collectors.toList());
                            return new SparseExemplar<>(features, columns, obs.numberOfFeatures(), 0, 6);
                        });


        ProviderToCSRMatrixWithTarget<Integer, SparseExemplar<Integer>,
                ObservationProviderInterface<Integer, SparseExemplar<Integer>>> providerToCSRMatrix =
                new ProviderToCSRMatrixWithTarget<>(exemplars);

        double[] values = providerToCSRMatrix.getValues();
        int[] columnIndexes = providerToCSRMatrix.getColumnIndexes();
        int[] rowIndexes = providerToCSRMatrix.getRowIndexes();
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6}, values, 0);
        assertArrayEquals(new int[] {0, 2, 3, 6}, rowIndexes);
        assertArrayEquals(new int[] {0, 2, 2, 0, 1, 2}, columnIndexes);
        assertArrayEquals(new int[] {15, 16, 17}, providerToCSRMatrix.getTargets());
    }

}