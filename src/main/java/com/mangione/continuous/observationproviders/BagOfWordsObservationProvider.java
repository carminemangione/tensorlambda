package com.mangione.continuous.observationproviders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.mangione.continuous.encodings.ProxyValues;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class BagOfWordsObservationProvider implements ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>> {
	private final ProxyValues featureProxyValues = new ProxyValues();
	private final ProxyValues targetProxyValues = new ProxyValues();
	private final ObservationProviderInterface<String, Observation<String>> csvObservationProvider;
	private final int contentColumn;
	private final int targetColumn;

	public BagOfWordsObservationProvider(ObservationProviderInterface<String, Observation<String>> provider, int contentColumn,
			int targetColumn) {
		int numberOfFeatures = provider.getNumberOfFeatures();
		if (contentColumn >= numberOfFeatures || targetColumn >= numberOfFeatures)
			throw new IllegalArgumentException("Content or target column is too large");

		csvObservationProvider = provider;
		this.contentColumn = contentColumn;
		this.targetColumn = targetColumn;

		for (Observation<String> observation : provider) {
			addWordsToFeatureProxy(observation.getFeature(contentColumn));
			targetProxyValues.getIndexOrAdd(observation.getFeature(targetColumn));
		}
	}

	public ProxyValues getFeatureProxyValues() {
		return featureProxyValues;
	}

	public ProxyValues getTargetProxyValues() {
		return targetProxyValues;
	}

	@Nonnull
	@Override
	public Iterator<SparseExemplar<Integer, Integer>> iterator() {
		return new BoWIterator();
	}

	private void addWordsToFeatureProxy(String words) {
		Arrays.stream(words.split(" "))
				.filter(word->!word.isEmpty())
				.map(String::toLowerCase)
				.forEach(featureProxyValues::getIndexOrAdd);
	}

	private class BoWIterator implements Iterator<SparseExemplar<Integer, Integer>> {
		private final Iterator<Observation<String>> csvIterator = csvObservationProvider.iterator();

		@Override
		public boolean hasNext() {
			return csvIterator.hasNext();
		}

		@Override
		public SparseExemplar<Integer, Integer> next() {
			Observation<String> next = csvIterator.next();
			int target = targetProxyValues.getIndexOrAdd(next.getFeature(targetColumn));

			String[] words = next.getFeature(contentColumn).split(" ");

			int[] features = IntStream.range(0, words.length)
					.boxed()
					.map(i-> words[i].toLowerCase())
					.filter(word->!word.isEmpty())
					.mapToInt(featureProxyValues::getIndex)
					.toArray();

			return SparseExemplar.createBinaryExemplar(features, featureProxyValues.size(), target);
		}
	}
}
