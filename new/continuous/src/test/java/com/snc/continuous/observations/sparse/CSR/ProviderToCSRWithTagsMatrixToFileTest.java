package com.mangione.continuous.observations.sparse.CSR;

import static org.junit.Assert.assertArrayEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.sparse.CSR.ProviderToCSRMatrixToFile;
import com.mangione.continuous.observations.sparse.SparseObservation;
import com.mangione.continuous.observations.sparse.SparseObservationBuilder;

public class ProviderToCSRWithTagsMatrixToFileTest {
    @Test
    public void csr4x6() throws Exception {
        /* Uses the following matrix:
        {10, 20, 0, 0, 0, 0}
        {0, 30, 0, 40, 0, 0}
        {0, 0, 50, 60, 70, 0}
        {0, 0, 0, 0, 0, 80}
        */

        List<SparseObservation<Integer>> sparseObservations = new ArrayList<>();
        SparseObservationBuilder<Integer> sparseObservationBuilder = new SparseObservationBuilder<>(6, 0);
        sparseObservationBuilder.setFeature(0, 10);
        sparseObservationBuilder.setFeature(1, 20);
        sparseObservations.add(sparseObservationBuilder.build(Integer[]::new));
        sparseObservationBuilder = new SparseObservationBuilder<>(6, 0);
        sparseObservationBuilder.setFeature(1, 30);
        sparseObservationBuilder.setFeature(3, 40);
        sparseObservations.add(sparseObservationBuilder.build(Integer[]::new));
        sparseObservationBuilder = new SparseObservationBuilder<>(6, 0);
        sparseObservationBuilder.setFeature(2, 50);
        sparseObservationBuilder.setFeature(3, 60);
        sparseObservationBuilder.setFeature(4, 70);
        sparseObservations.add(sparseObservationBuilder.build(Integer[]::new));
        sparseObservationBuilder = new SparseObservationBuilder<>(6, 0);
        sparseObservationBuilder.setFeature(5, 80);
        sparseObservations.add(sparseObservationBuilder.build(Integer[]::new));

        ListObservationProvider<Integer, SparseObservation<Integer>> observations =
                new ListObservationProvider<>(sparseObservations);

        File valuesFile = File.createTempFile("values", "csv");
        File columnsFile = File.createTempFile("columns", "csv");
        File rowsFile = File.createTempFile("rows", "csv");
       new ProviderToCSRMatrixToFile<>(observations, columnsFile, rowsFile, valuesFile).process();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(valuesFile));
        double[] values = Arrays.stream(bufferedReader.readLine().split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        bufferedReader.close();
        assertArrayEquals(new double[]{10, 20, 30, 40, 50, 60, 70, 80}, values, 0);
        assertArrayEquals(new int[] {0, 2, 4, 7, 8}, validateIntFile(rowsFile));
        assertArrayEquals(new int[] {0, 1, 1, 3, 2, 3, 4, 5}, validateIntFile(columnsFile));
    }

    private int[] validateIntFile(File file) throws Exception {
        String s = new BufferedReader(new FileReader(file)).readLine();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}