package com.mangione.continuous.observationproviders.csv;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

public class CsvObservationIteratorTest {
	private static final int NUM_LINES = 1001;
	private File csvFile;
	private Function<File, BufferedReader> bufferedFunction;

	@Before
	public void setUp() throws Exception {
		csvFile = File.createTempFile("CsvObservationSpliteratorTest", "csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));
		for (int i = 0; i < NUM_LINES; i++) {
			bw.write("" + i);
			bw.newLine();
		}
		bw.close();
		bufferedFunction = x -> {
			try {
				return new BufferedReader(new FileReader(x));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		};
	}

	@Test
	public void readLines() {
		int expectedNumber = 0;
		CsvObservationIterator<Integer> iterator = new CsvObservationIterator<>(csvFile, " ", s -> Integer.parseInt(s[0]), false, bufferedFunction);
		while (iterator.hasNext())
			assertEquals(expectedNumber++, (int)iterator.next());
		assertEquals(NUM_LINES, expectedNumber);
	}

	@Test
	public void readHeader() {
		int expectedNumber = 1;
		CsvObservationIterator<Integer> iterator = new CsvObservationIterator<>(csvFile, " ", s -> Integer.parseInt(s[0]), true, bufferedFunction);
		while (iterator.hasNext())
			assertEquals(expectedNumber++, (int)iterator.next());
		assertEquals(NUM_LINES, expectedNumber);
	}

	@Test
	public void hasNextPastEndDoesNotExcept() {
		int expectedNumber = 0;
		CsvObservationIterator<Integer> iterator = new CsvObservationIterator<>(csvFile, " ",
				s -> Integer.parseInt(s[0]), false, bufferedFunction);
		for (int i = 0; i < NUM_LINES * 10; i++)
			if (iterator.hasNext())
			assertEquals(expectedNumber++, (int)iterator.next());
		assertEquals(NUM_LINES, expectedNumber);
	}
}