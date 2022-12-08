package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.encodings.hashing.FeatureHash;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;
import com.mangione.continuous.util.coersion.CoerceToIntArray;

public class FeatureHashedObservationProviderTest {

	private SparseExemplarFactoryInterface<Integer, SparseExemplar<Integer, Integer>> exemplarFactory;
	private int[] columns;

	@Before
	public void setUp() {
		exemplarFactory = (features, columnIndexes, numberOfColumns, original) ->
				new SparseExemplar<>(features, columnIndexes, numberOfColumns, 0, original.getLabel());
		columns = IntStream.range(0, 3).toArray();
	}

	@Test
	public void providerWorks() {
		Integer[][] observations = {{1, 2, 3}};

		ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>> aop =
				new ArrayObservationProvider<>(observations,
						(features) -> new SparseExemplar<>(features, columns, 3, 0, 1));

		FeatureHashedObservationProvider<SparseExemplar<Integer, Integer>> provider =
				new FeatureHashedObservationProvider<>(2, 2452, 3245, aop,
						exemplarFactory);

		FeatureHash featureHash = new FeatureHash(3, 2, 2452, 3245);
		Iterator<SparseExemplar<Integer, Integer>> iterator = provider.iterator();
		assertTrue(iterator.hasNext());
		ObservationInterface<Integer> next = iterator.next();
		assertArrayEquals(featureHash.hash(new int[]{0, 1, 2}), CoerceToIntArray.coerce(next.getFeatures(Integer[]::new)));
		assertFalse(iterator.hasNext());
	}


	@Test(expected = IllegalStateException.class)
	public void emptyProviderExcepts() {
		ObservationProviderInterface<Integer, SparseExemplar<Integer, Integer>> aop =
				new ArrayObservationProvider<>(new Integer[][]{},
						(features) -> new SparseExemplar<>(features, columns, 3, 0, 1));
		new FeatureHashedObservationProvider<>(2, 2452, 3245, aop,
				exemplarFactory);

	}
}