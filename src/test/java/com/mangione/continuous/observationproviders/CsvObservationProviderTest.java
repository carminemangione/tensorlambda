package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class CsvObservationProviderTest {

    private File file;
    private static final String FILE_NAME = "CsvObservationProviderTest.csv";

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("CsvObservationProviderTest", "csv");
        file.deleteOnExit();
    }

    @Test
    public void countLinesInInput() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 3; i++) {
            bufferedWriter.write("1");
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        CsvObservationProvider<Observation> op = new CsvObservationProvider<>(file, new ObservationFactory());
        assertEquals(3, op.getNumberOfLines());
        ensureFileIsClosed();
    }

    @Test
    public void readLinesInInput() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 3; i++) {
            bufferedWriter.write(String.format("%d,%d,%d", i, i + 1, i + 2));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        CsvObservationProvider<Observation> op = new CsvObservationProvider<>(file, new ObservationFactory());
        int i = 0;
        while (op.hasNext()) {
            final double[] next = op.next().getFeatures();
            assertEquals(3, next.length);
            for (int j = 0; j < next.length; j++) {
                assertEquals(i + j, next[j], 0);
            }
            i++;
        }
    }

    @Test
    public void variableCalculator() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        String[] categories = {"a", "b", "c"};
        for (int i = 0; i < 3; i++) {
            bufferedWriter.write(categories[i]);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        Map<Integer, VariableCalculator> calculators = new HashMap<>();
        calculators.put(0, feature -> {
            double[] out = new double[3];
            switch (feature) {
                case "a":
                    out[0] = 1;
                    break;
                case "b":
                    out[1] = 1;
                    break;
                case "c":
                    out[2] = 1;
                    break;
            }
            return out;
        });
        CsvObservationProvider<Observation> op = new CsvObservationProvider<>(file, new ObservationFactory(), calculators);
        assertTrue(Arrays.equals(new double[]{1, 0, 0}, op.next().getFeatures()));
        assertTrue(Arrays.equals(new double[]{0, 1, 0}, op.next().getFeatures()));
        assertTrue(Arrays.equals(new double[]{0, 0, 1}, op.next().getFeatures()));

    }

    private void ensureFileIsClosed() throws IOException {
        try {
            FileUtils.touch(file);

        } catch (IOException e) {
            fail();
        }
    }


}