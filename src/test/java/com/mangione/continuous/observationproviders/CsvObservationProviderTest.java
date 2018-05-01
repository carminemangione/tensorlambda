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

        CsvObservationProvider op =
                new CsvObservationProvider(file);

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

        CsvObservationProvider op = new CsvObservationProvider(file);
        int i = 0;
        while (op.hasNext()) {
            final String[] next = op.next().getFeatures();
            assertEquals(3, next.length);
            for (int j = 0; j < next.length; j++) {
                assertEquals(i + j, Double.parseDouble(next[j]), 0);
            }
            i++;
        }
    }


    private void ensureFileIsClosed() throws IOException {
        try {
            FileUtils.touch(file);

        } catch (IOException e) {
            fail();
        }
    }


}