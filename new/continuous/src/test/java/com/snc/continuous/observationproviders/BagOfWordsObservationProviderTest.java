package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.mangione.continuous.encodings.ProxyValues;
import com.mangione.continuous.observationproviders.csv.CsvObservationProvider;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class BagOfWordsObservationProviderTest {

	@Test
	public void singleLineSingleValue() throws IOException {
		File tempFile = File.createTempFile("BagOfWordsObservationProviderTest", "csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
		bw.write("poop,19");
		bw.close();
		CsvObservationProvider<String, Observation<String>> provider = new CsvObservationProvider<>(tempFile,
				Observation::new, false, getFileBufferedReaderFunction());
		BagOfWordsObservationProvider bowProvider = new BagOfWordsObservationProvider(provider, 0, 1);
		assertEquals(1, bowProvider.getNumberOfFeatures());
		assertEquals(1, bowProvider.getFeatureProxyValues().size());
		assertEquals("poop", bowProvider.getFeatureProxyValues().getName(0));

		Iterator<SparseExemplar<Integer, Integer>> iterator = bowProvider.iterator();
		assertTrue(iterator.hasNext());
		SparseExemplar<Integer, Integer> next = iterator.next();
		assertEquals(1, next.getFeatures(Integer[]::new).length);
		assertEquals(0, (int) next.getLabel());
		assertEquals(0, next.getColumnIndexes()[0]);
	}

	@Test
	public void multipleWordsMultipleTargets() {
		String contentObs1 = "a boy and his dog";
		String contentObs2 = "boy oh boy";
		String[][] words = {{"poop1", "tag1", contentObs1}, {"poop1", "tag2", contentObs2}};

		ObservationProviderInterface<String, Observation<String>> provider = new ArrayObservationProvider<>(words, Observation::new);
		BagOfWordsObservationProvider bowProvider = new BagOfWordsObservationProvider(provider, 2, 1);
		assertEquals(2, bowProvider.getTargetProxyValues().size());
		assertEquals(6, bowProvider.getFeatureProxyValues().size());

		ProxyValues featureProxyValues = bowProvider.getFeatureProxyValues();
		String[] wordsObs1 = contentObs1.split(" ");
		Arrays.stream(wordsObs1)
				.forEach(word -> assertNotNull("Validate all content in proxy", featureProxyValues.getIndex(word)));
		String[] wordsObs2 = contentObs2.split(" ");
		Arrays.stream(wordsObs2)
				.forEach(word -> assertNotNull("Validate all content in proxy", featureProxyValues.getIndex(word)));

		assertNotNull("Validate all target in proxy", bowProvider.getTargetProxyValues().getIndex("tag1"));
		assertNotNull("Validate all target in proxy", bowProvider.getTargetProxyValues().getIndex("tag2"));

		validateEncoding(bowProvider, featureProxyValues, wordsObs1, wordsObs2);
	}

	@Test
	public void wordsAreTrimmed() {
		String contentObs1 = "a boy and his        dog";
		String contentObs2 = "boy oh boy";
		String[][] words = {{"poop1", "tag1", contentObs1}, {"poop1", "tag2", contentObs2}};

		ObservationProviderInterface<String, Observation<String>> provider = new ArrayObservationProvider<>(words, Observation::new);
		BagOfWordsObservationProvider bowProvider = new BagOfWordsObservationProvider(provider, 2, 1);
		assertEquals(2, bowProvider.getTargetProxyValues().size());
		assertEquals(6, bowProvider.getFeatureProxyValues().size());
		validateEncoding(bowProvider, bowProvider.getFeatureProxyValues(), new String[]{"a", "boy", "and", "his", "dog"},
				new String[]{"boy", "oh", "boy"});

	}

	@Test
	public void wordsAreLowercase() {
		String contentObs1 = "a boy and his        DOG";
		String[][] words = {{"poop1", "tag1", contentObs1}};

		ObservationProviderInterface<String, Observation<String>> provider = new ArrayObservationProvider<>(words, Observation::new);
		BagOfWordsObservationProvider bowProvider = new BagOfWordsObservationProvider(provider, 2, 1);
		assertNotNull(bowProvider.getFeatureProxyValues().getIndex("dog"));
	}

	private void validateEncoding(BagOfWordsObservationProvider bowProvider, ProxyValues featureProxyValues, String[] wordsObs1, String[] wordsObs2) {
		Iterator<SparseExemplar<Integer, Integer>> iterator = bowProvider.iterator();
		assertTrue(iterator.hasNext());
		SparseExemplar<Integer, Integer> next = iterator.next();
		IntStream.range(0, wordsObs1.length)
				.boxed()
				.forEach(i -> assertEquals("Properly encoded", wordsObs1[i],
						featureProxyValues.getName(next.getColumnIndexes()[i])));

		SparseExemplar<Integer, Integer> next1 = iterator.next();
		IntStream.range(0, wordsObs2.length)
				.boxed()
				.forEach(i -> assertEquals("Properly encoded", wordsObs2[i],
						featureProxyValues.getName(next1.getColumnIndexes()[i])));
		assertFalse(iterator.hasNext());
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnInvalid() {
		String contentObs1 = "a boy and his        DOG";
		String[][] words = {{"poop1", "tag1", contentObs1}};

		ObservationProviderInterface<String, Observation<String>> provider = new ArrayObservationProvider<>(words, Observation::new);
		new BagOfWordsObservationProvider(provider, 3, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void contentColumnInvalid() {
		String contentObs1 = "a boy and his        DOG";
		String[][] words = {{"poop1", "tag1", contentObs1}};

		ObservationProviderInterface<String, Observation<String>> provider = new ArrayObservationProvider<>(words, Observation::new);
		new BagOfWordsObservationProvider(provider, 1, 4);
	}

	@Nonnull
	private Function<File, BufferedReader> getFileBufferedReaderFunction() {
		return file ->
		{
			try {
				return new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		};
	}
}