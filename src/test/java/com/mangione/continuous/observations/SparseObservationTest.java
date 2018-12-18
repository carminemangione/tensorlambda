package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class SparseObservationTest {

	@Test
	public void aFewColumns() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10);
		assertEquals(new Integer(9), sparseObservation.getFeatures().get(0));
		assertEquals(new Integer(9), sparseObservation.getFeature(0));

		assertNull(sparseObservation.getFeatures().get(1));
		assertNull(sparseObservation.getFeature(1));

		assertEquals(new Integer(8), sparseObservation.getFeatures().get(2));
		assertEquals(new Integer(8), sparseObservation.getFeature(2));

		assertEquals(new Integer(2), sparseObservation.getFeatures().get(7));
		assertEquals(new Integer(2), sparseObservation.getFeature(7));

		assertNull(sparseObservation.getFeatures().get(9));
		assertNull(sparseObservation.getFeature(9));

	}

}