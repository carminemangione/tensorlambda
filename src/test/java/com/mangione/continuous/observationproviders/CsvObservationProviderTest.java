package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;
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

        CsvObservationProvider<Double, ObservationInterface<Double>> op =
                new CsvObservationProvider<>(file, new DoubleObservationFactory<>(), new DoubleVariableCalculator(), new DoubleArraySupplier());
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

        CsvObservationProvider<Double, ObservationInterface<Double>> op = new CsvObservationProvider<>(file, new DoubleObservationFactory<>(),
                new DoubleVariableCalculator(), new DoubleArraySupplier());
        int i = 0;
        while (op.hasNext()) {
            final Double[] next = op.next().getFeatures();
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

        Map<Integer, VariableCalculator<Double>> calculators = new HashMap<>();
        calculators.put(0, feature -> {
            Double[] out = new Double[]{0d,0d,0d};
            switch (feature) {
                case "a":
                    out[0] = 1d;
                    break;
                case "b":
                    out[1] = 1d;
                    break;
                case "c":
                    out[2] = 1d;
                    break;
            }
            return Arrays.asList(out);
        });
        CsvObservationProvider<Double, ObservationInterface<Double>> op = new CsvObservationProvider<>(file,
                new DoubleObservationFactory<>(), calculators, new DoubleVariableCalculator(), new DoubleArraySupplier());



        assertEquals(1d, op.next().getFeatures()[0], 0);
        assertEquals(1d, op.next().getFeatures()[1], 0);
        assertEquals(1d, op.next().getFeatures()[2], 0);

    }

    private void ensureFileIsClosed() throws IOException {
        try {
            FileUtils.touch(file);

        } catch (IOException e) {
            fail();
        }
    }


}