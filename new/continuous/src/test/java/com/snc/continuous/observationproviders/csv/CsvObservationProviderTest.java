package com.mangione.continuous.observationproviders.csv;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.encodings.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class CsvObservationProviderTest {

    private File file;
	private Function<String[], ObservationInterface<String>> stringFactory;

	@Before
    public void setUp() throws Exception {
        file = File.createTempFile("CsvObservationProviderTest", "csv");
        file.deleteOnExit();
		stringFactory = Observation::new;
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
				stringFactory, false, CsvObservationProviderTest::openFile);

        assertEquals(3, op.size());
        ensureFileIsClosed();
    }

    @Test
    public void readLinesInInput() throws Exception {
	    fillFileWithNumbers();

        CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>(file, stringFactory, false, CsvObservationProviderTest::openFile);
		AtomicInteger index = new AtomicInteger();
        op.forEach(observation -> validateFeatures(index.getAndIncrement(), observation));
	   	assertEquals(3, index.get());
    }

    @Test
    public void iterator() throws Exception {
	    fillFileWithNumbers();

	    CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>(file, stringFactory, false, CsvObservationProviderTest::openFile);

	    int i = 0;
	    for (ObservationInterface<String> observation : op) {
		    validateFeatures(i++, observation);
	    }
    }

	@Test
	public void forEach() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>( file, stringFactory, false, CsvObservationProviderTest::openFile);
		final int[] i = {0};
		op.forEach(stringObservationInterface ->
				validateFeatures(i[0]++, stringObservationInterface));
		assertEquals(3, i[0]);
	}

	@Test
	public void multipleIterators() throws Exception {
		fillFileWithNumbers();

		CsvObservationProvider<String, ObservationInterface<String>> op = new CsvObservationProvider<>( file, stringFactory, false, CsvObservationProviderTest::openFile);

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
		new CsvObservationProvider<>( file, stringFactory, false, CsvObservationProviderTest::openFile).iterator().remove();
	}

	@Test
	public void forRemaining() throws Exception {
		fillFileWithNumbers();
		Iterator<ObservationInterface<String>> iterator = new CsvObservationProvider<>( file, stringFactory, false, CsvObservationProviderTest::openFile).iterator();
		assertTrue(iterator.hasNext());
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

		CsvObservationProvider<String, ObservationInterface<String>> provider = new CsvObservationProvider<>( file, stringFactory, true, CsvObservationProviderTest::openFile);

		ProxyValues namedColumns = provider.getNamedColumns();

		assertEquals("one", namedColumns.getName(0));
		assertEquals("two", namedColumns.getName(1));
		assertEquals("three", namedColumns.getName(2));
	}


	@Test
	public void testCountNumberOfLinesForGxip() throws IOException {
		File temp = File.createTempFile("temp", ".csv.gz");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(temp))));
		for (int i = 0; i < 10; i++) {
			bw.write("poo" + i);
			bw.newLine();
		}
		bw.close();

		CsvObservationProvider<String, ObservationInterface<String>> failedTestExemplars =
				new CsvObservationProvider<>(temp,
						Observation::new,
						false,
						CsvObservationProviderTest::openGzippedFile);

		Iterator<ObservationInterface<String>> iterator = failedTestExemplars.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			iterator.next();
			if (++i % 100000 == 0)
				System.out.println("Processed: " + NumberFormat.getIntegerInstance().format(i));
		}
		System.out.println(i);
		Assert.assertEquals(10, i);
	}
	
	private static BufferedReader openGzippedFile(File file) {
		try {
			return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
		} catch (IOException e) {
			fail("Could not find file");
			throw new RuntimeException(e);
		}
	}

	private static BufferedReader openFile(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			fail("Could not find file");
			throw new RuntimeException(e);
		}
	}
	
	private void validateFeatures(int i, ObservationInterface<String> next) {
		final String[] features = next.getFeatures(String[]::new);
		assertEquals(3, features.length);

		for (int j = 0; j < features.length; j++) {
			assertEquals(i + j, Double.parseDouble(features[j]), 0);
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