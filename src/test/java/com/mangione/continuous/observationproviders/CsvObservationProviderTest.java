package com.mangione.continuous.observationproviders;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.ObservationInterface;

public class CsvObservationProviderTest {

    private File file;

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
	    fillFileWithNumbers();

        CsvObservationProvider op = new CsvObservationProvider(file);
        int i = 0;
	    for  (ObservationInterface<String> observation : op) {
            final String[] next = observation.getFeatures();
		    i = validateFeatures(i, next);
        }
    }

    @Test
    public void iterator() throws Exception {
	    fillFileWithNumbers();

	    CsvObservationProvider op = new CsvObservationProvider(file);

	    int i = 0;
	    for (ObservationInterface<String> observation : op) {
		    i = validateFeatures(i, observation.getFeatures());
	    }
    }

	@Test
	public void forEach() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider op = new CsvObservationProvider(file);
		final int[] i = {0};
		op.forEach(stringObservationInterface ->
				i[0] = validateFeatures(i[0], stringObservationInterface.getFeatures()));
		assertEquals(3, i[0]);
	}

	@Test
	public void multipleIterators() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider op = new CsvObservationProvider(file);

		int i = 0;
		for (ObservationInterface<String> observation : op) {
			i = validateFeatures(i, observation.getFeatures());
			int j = 0;
			for (ObservationInterface<String> innerObs : op) {
				j = validateFeatures(j, innerObs.getFeatures());
			}
			assertEquals(3, j);
		}
		assertEquals(3, i);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void iteratorRemoveNotSupported() throws Exception {
		new CsvObservationProvider(file).iterator().remove();
	}

	@Test
	public void forRemaining() throws Exception {
		fillFileWithNumbers();
		Iterator<ObservationInterface<String>> iterator = new CsvObservationProvider(file).iterator();
		iterator.next();
		final int[] i = {1};
		iterator.forEachRemaining(stringObservationInterface ->
				i[0] = validateFeatures(i[0], stringObservationInterface.getFeatures()));
		assertEquals(3, i[0]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void spliteratorNotSupported() throws Exception {
		new CsvObservationProvider(file).spliterator();
	}

	private int validateFeatures(int i, String[] next) {
		assertEquals(3, next.length);
		for (int j = 0; j < next.length; j++) {
			assertEquals(i + j, Double.parseDouble(next[j]), 0);
		}
		i++;
		return i;
	}


	private void fillFileWithNumbers() throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < 3; i++) {
		    bufferedWriter.write(String.format("%d,%d,%d", i, i + 1, i + 2));
		    bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}


	private void ensureFileIsClosed() throws IOException {
        try {
            FileUtils.touch(file);

        } catch (IOException e) {
            fail();
        }
    }


}