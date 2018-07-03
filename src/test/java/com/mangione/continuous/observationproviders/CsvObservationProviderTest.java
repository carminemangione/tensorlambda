package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.NamedColumns;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.StringObservationFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

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

        CsvObservationProvider op = new CsvObservationProvider(file, new StringObservationFactory(), false);

        assertEquals(3, op.getNumberOfLines());
        ensureFileIsClosed();
    }

    @Test
    public void readLinesInInput() throws Exception {
	    fillFileWithNumbers();

        CsvObservationProvider op = new CsvObservationProvider(file, new StringObservationFactory(), false);
		AtomicInteger index = new AtomicInteger();
        op.forEach(observation -> validateFeatures(index.getAndIncrement(), observation));
	   	assertEquals(3, index.get());
    }

    @Test
    public void iterator() throws Exception {
	    fillFileWithNumbers();

	    CsvObservationProvider op = new CsvObservationProvider(file, new StringObservationFactory(), false);

	    int i = 0;
	    for (ObservationInterface<String> observation : op) {
		    validateFeatures(i++, observation);
	    }
    }

	@Test
	public void forEach() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider op = new CsvObservationProvider(file, new StringObservationFactory(), false);
		final int[] i = {0};
		op.forEach(stringObservationInterface ->
				validateFeatures(i[0]++, stringObservationInterface));
		assertEquals(3, i[0]);
	}

	@Test
	public void multipleIterators() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider op = new CsvObservationProvider(file, new StringObservationFactory(), false);

		int i = 0;
		for (ObservationInterface<String> observation : op) {
			validateFeatures(i++, observation);
			int j = 0;
			for (ObservationInterface<String> innerObs : op) {
				validateFeatures(j++, innerObs);
			}
			assertEquals(3, j);
		}
		assertEquals(3, i);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void iteratorRemoveNotSupported() throws Exception {
		new CsvObservationProvider(file, new StringObservationFactory(), false).iterator().remove();
	}

	@Test
	public void forRemaining() throws Exception {
		fillFileWithNumbers();
		Iterator<ObservationInterface<String>> iterator = new CsvObservationProvider(file, new StringObservationFactory(), false).iterator();
		iterator.next();
		final int[] i = {1};
		iterator.forEachRemaining(stringObservationInterface ->
				 validateFeatures(i[0]++, stringObservationInterface
				));
		assertEquals(3, i[0]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void spliteratorNotSupported() throws Exception {
		new CsvObservationProvider(file, new StringObservationFactory(), false).spliterator();
	}

	@Test
	public void getNamedColumnsFromHeader() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("one,two,three");
		writer.newLine();
		writer.write("1,2,3");
		writer.close();

		CsvObservationProvider provider = new CsvObservationProvider(file, new StringObservationFactory(), true);

		NamedColumns namedColumns = provider.getNamedColumns();

		assertEquals("one", namedColumns.getName(0));
		assertEquals("two", namedColumns.getName(1));
		assertEquals("three", namedColumns.getName(2));


	}

	private void validateFeatures(int i, ObservationInterface<String> next) {
		final List<String> features = next.getFeatures();
		assertEquals(3, features.size());

		for (int j = 0; j < features.size(); j++) {
			assertEquals(i + j, Double.parseDouble(features.get(j)), 0);
		}

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