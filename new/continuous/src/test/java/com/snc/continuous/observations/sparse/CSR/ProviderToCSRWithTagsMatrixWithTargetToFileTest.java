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
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;

public class ProviderToCSRWithTagsMatrixWithTargetToFileTest {
    @Test
    public void withTargetsTest() throws Exception {
        List<SparseExemplar<Integer, Integer>> sparseExemplars = new ArrayList<>();
        SparseExemplarBuilder<Integer, Integer> sparseExemplar = new SparseExemplarBuilder<>(7, 0, 15);
        sparseExemplar.setFeature(0, 1);
        sparseExemplar.setFeature(2, 2);
        sparseExemplars.add(sparseExemplar.build(Integer[]::new));
        sparseExemplar = new SparseExemplarBuilder<>(7, 0, 16);
        sparseExemplar.setFeature(2, 3);
        sparseExemplars.add(sparseExemplar.build(Integer[]::new));
        sparseExemplar = new SparseExemplarBuilder<>(7, 0, 17);
        sparseExemplar.setFeature(0, 4);
        sparseExemplar.setFeature(1, 5);
        sparseExemplar.setFeature(2, 6);
        sparseExemplars.add(sparseExemplar.build(Integer[]::new));

        File valuesFile = File.createTempFile("values", "csv");
        File columnsFile = File.createTempFile("columns", "csv");
        File rowsFile = File.createTempFile("rows", "csv");
        File targetsFile = File.createTempFile("targets", "csv");
        new ProviderToCSRMatrixWithTargetToFile<>(new ListObservationProvider<>(sparseExemplars),
                columnsFile, rowsFile, valuesFile, targetsFile, ExemplarInterface::getLabel).process();

        double[] values = Arrays.stream(new BufferedReader(new FileReader(valuesFile)).readLine().split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
        assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6}, values, 0);
        assertArrayEquals(new int[] {0, 2, 3, 6}, validateIntFile(rowsFile));
        assertArrayEquals(new int[] {0, 2, 2,  0, 1, 2}, validateIntFile(columnsFile));
        assertArrayEquals(new int[] {15, 16, 17}, validateIntFile(targetsFile));
    }

    private int[] validateIntFile(File file) throws Exception {
        return Arrays.stream(new BufferedReader(new FileReader(file)).readLine().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}