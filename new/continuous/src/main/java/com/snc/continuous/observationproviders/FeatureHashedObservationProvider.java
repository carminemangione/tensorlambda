package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.encodings.hashing.FeatureHash;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;
import com.mangione.continuous.util.LoggingTimer;
import com.mangione.continuous.util.coersion.CoerceToIntArray;

public class FeatureHashedObservationProvider<EXEMPLAR extends SparseExemplar<Integer, Integer>> implements ObservationProviderInterface<Integer, EXEMPLAR> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureHashedObservationProvider.class);
	private static final LoggingTimer LOGGING_TIMER = new LoggingTimer(LOGGER, 1000, "Feature hashing provider.");
	private final ObservationProviderInterface<Integer, EXEMPLAR> provider;
	private final FeatureHash featureHash;
	private final SparseExemplarFactoryInterface<Integer, EXEMPLAR> exemplarFactory;

	public FeatureHashedObservationProvider(int dimensionOfOutputSpace, int indexSeed, int etaSeed,
			ObservationProviderInterface<Integer, EXEMPLAR> provider,
			SparseExemplarFactoryInterface<Integer, EXEMPLAR> exemplarFactory) {
		Iterator<EXEMPLAR> iterator = provider.iterator();
		if (!iterator.hasNext())
			throw new IllegalStateException("empty provider");
		int numberOfFeatures = iterator.next().numberOfFeatures();

		featureHash = new FeatureHash(numberOfFeatures, dimensionOfOutputSpace, indexSeed, etaSeed);
		this.provider = provider;
		this.exemplarFactory = exemplarFactory;
	}

	@Nonnull
	@Override
	public Iterator<EXEMPLAR> iterator() {
		return new Iterator<>() {
			private final Iterator<EXEMPLAR> iterator = provider.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public EXEMPLAR next() {
				EXEMPLAR next = iterator.next();
				int[] hash = featureHash.hash(next);
				LOGGING_TIMER.nextStep();
				return exemplarFactory.create(CoerceToIntArray.coerce(hash),
						IntStream.range(0, hash.length).toArray(), hash.length,
						next);
			}
		};
	}
}
