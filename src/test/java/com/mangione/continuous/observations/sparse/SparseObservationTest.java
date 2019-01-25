package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Test;

public class SparseObservationTest {

	@Test
	public void aFewColumns() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(new Integer(9), sparseObservation.getFeatures().get(0));
		assertEquals(new Integer(9), sparseObservation.getFeature(0));

		assertEquals(new Integer(0), sparseObservation.getFeatures().get(1));
		assertEquals(new Integer(0), sparseObservation.getFeature(1));

		assertEquals(new Integer(8), sparseObservation.getFeatures().get(2));
		assertEquals(new Integer(8), sparseObservation.getFeature(2));

		assertEquals(new Integer(2), sparseObservation.getFeatures().get(7));
		assertEquals(new Integer(2), sparseObservation.getFeature(7));

		assertEquals(new Integer(0), sparseObservation.getFeatures().get(9));
		assertEquals(new Integer(0), sparseObservation.getFeature(9));

	}

	@Test
	public void missingValuesAreZeros() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(Arrays.asList(9, 0, 8, 0, 0, 0, 0, 2, 0, 0), sparseObservation.getAllColumns());
	}

	@Test
	public void getColumnIndexes() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(Arrays.asList(0, 2, 7), sparseObservation.getColumnIndexes());
	}

	@Test
	public void getValueAtIndexExists() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(new Integer(9), sparseObservation.getValueAt(0));
		assertEquals(new Integer(8), sparseObservation.getValueAt(2));
		assertEquals(new Integer(2), sparseObservation.getValueAt(7));
	}

	@Test
	public void getValueAtIndexMissing() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(new Integer(9), sparseObservation.getValueAt(0));	}

	@Test
	public void getValueAtIndexAfter() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 666);
		assertEquals(new Integer(666), sparseObservation.getValueAt(9));
	}

	@Test
	public void setValueAtIndex() {
		SparseObservation<Integer> sparseObservation = new SparseObservation<>(10, 666);
		IntStream.range(0, 10)
				.forEach(index -> assertEquals(new Integer(666), sparseObservation.getValueAt(index)));

		sparseObservation.setValueAt(5, 2);
		IntStream.range(0, 10)
				.filter(index -> index != 5)
				.forEach(index -> assertEquals(new Integer(666), sparseObservation.getValueAt(index)));
		assertEquals(new Integer(2), sparseObservation.getValueAt(5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void valuesAndIndexesNotSameLengthExcepts()  {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2};

		new SparseObservation<>(values, indexes, 10, 666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsOutOfRangeHigh()  {
		new SparseObservation<>(10, 666).setValueAt(10, 9);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsNegative()  {
		new SparseObservation<>(10, 666).setValueAt(-1, 9);
	}

	@Test
	public void missingValueNotZero() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 666);
		assertEquals(Arrays.asList(9, 666, 8, 666, 666, 666, 666, 2, 666, 666), sparseObservation.getAllColumns());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueTooHighExcepts()  {
		new SparseObservation<>(10, 666).getValueAt(10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueNegativeExcepts()  {
		new SparseObservation<>(10, 666).getValueAt(-1);
	}


	@Test(expected = UnsupportedOperationException.class)
	public void getColumnsReturnsImmutableList()  {
		new SparseObservation<>(10, 666).getAllColumns().add(1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getColumnIndexesReturnsImmutableList() {
		new SparseObservation<>(10, 666).getColumnIndexes().add(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyValues() {
		new SparseObservation<>(new Integer[23], new int[]{0}, 5, 0);
	}
}