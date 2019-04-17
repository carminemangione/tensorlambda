package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class CsvObservationProviderTest {

    private File file;
	private Function<String[], ObservationInterface<String>> stringFactory;

	@Before
    public void setUp() throws Exception {
        file = File.createTempFile("CsvObservationProviderTest", "csv");
        file.deleteOnExit();
		stringFactory = strings -> new Observation<>(Arrays.asList(strings));
	}

    @Test
    public void countLinesInInput() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 3; i++) {
            bufferedWriter.write("1");
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>(file,
				stringFactory, false);

        assertEquals(3, op.getNumberOfLines());
        ensureFileIsClosed();
    }

    @Test
    public void readLinesInInput() throws Exception {
	    fillFileWithNumbers();

        CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>(file, stringFactory, false);
		AtomicInteger index = new AtomicInteger();
        op.forEach(observation -> validateFeatures(index.getAndIncrement(), observation));
	   	assertEquals(3, index.get());
    }

    @Test
    public void iterator() throws Exception {
	    fillFileWithNumbers();

	    CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>(file, stringFactory, false);

	    int i = 0;
	    for (ObservationInterface<String> observation : op) {
		    validateFeatures(i++, observation);
	    }
    }

	@Test
	public void forEach() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>( file, stringFactory, false);
		final int[] i = {0};
		op.forEach(stringObservationInterface ->
				validateFeatures(i[0]++, stringObservationInterface));
		assertEquals(3, i[0]);
	}

	@Test
	public void multipleIterators() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>( file, stringFactory, false);

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
	public void iteratorRemoveNotSupported() {
		new CsvObservationProvider<>( file, stringFactory, false).iterator().remove();
	}

	@Test
	public void forRemaining() throws Exception {
		fillFileWithNumbers();
		Iterator<ObservationInterface<String>> iterator = new CsvObservationProvider<>( file, stringFactory, false).iterator();
		iterator.next();
		final int[] i = {1};
		iterator.forEachRemaining(stringObservationInterface ->
				 validateFeatures(i[0]++, stringObservationInterface
				));
		assertEquals(3, i[0]);
	}

	@Test
	public void getNamedColumnsFromHeader() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write("one,two,three");
		writer.newLine();
		writer.write("1,2,3");
		writer.close();

		CsvObservationProvider<String, ObservationInterface<String>> provider = new CsvObservationProvider<>( file, stringFactory, true);

		ProxyValues namedColumns = provider.getNamedColumns();

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


	private void ensureFileIsClosed() {
        try {
            FileUtils.touch(file);

        } catch (IOException e) {
            fail();
        }
    }


}