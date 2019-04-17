package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
	public void numberOfFeaturesDoesNotExpand() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};
		SparseObservation<Integer> sparseObservation = new ExceptsOnGetFeatures(values, indexes);
		assertEquals(10, sparseObservation.numberOfFeatures());
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
		ArrayList<Integer> columnIndexes = new ArrayList<>(sparseObservation.getColumnIndexes());
		Collections.sort(columnIndexes);
		assertEquals(Arrays.asList(0, 2, 7), columnIndexes);
	}

	@Test
	public void getFeatureIndexExists() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(new Integer(9), sparseObservation.getFeature(0));
		assertEquals(new Integer(8), sparseObservation.getFeature(2));
		assertEquals(new Integer(2), sparseObservation.getFeature(7));
	}

	@Test
	public void getFeatureIndexMissing() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(new Integer(9), sparseObservation.getFeature(0));	}

	@Test
	public void getFeatureIndexAfter() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 666);
		assertEquals(new Integer(666), sparseObservation.getFeature(9));
	}

	@Test
	public void setValueAtIndex() {
		SparseObservation<Integer> sparseObservation = new SparseObservation<>(10, 666);
		IntStream.range(0, 10)
				.forEach(index -> assertEquals(new Integer(666), sparseObservation.getFeature(index)));

		sparseObservation.setFeature(5, 2);
		IntStream.range(0, 10)
				.filter(index -> index != 5)
				.forEach(index -> assertEquals(new Integer(666), sparseObservation.getFeature(index)));
		assertEquals(new Integer(2), sparseObservation.getFeature(5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void valuesAndIndexesNotSameLengthExcepts()  {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2};

		new SparseObservation<>(values, indexes, 10, 666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsOutOfRangeHigh()  {
		new SparseObservation<>(10, 666).setFeature(10, 9);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsNegative()  {
		new SparseObservation<>(10, 666).setFeature(-1, 9);
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
		new SparseObservation<>(10, 666).getFeature(10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueNegativeExcepts()  {
		new SparseObservation<>(10, 666).getFeature(-1);
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

	@Test
	public void fourColumns() {
		int[] columns = {3, 5, 9, 11};
		Integer[] values = new Integer[4];
		Arrays.fill(values, 1);
		SparseObservation<Integer> observation = new SparseObservation<>(values, columns, 30, 0);

		ArrayList<Integer> columnIndexes = new ArrayList<>(observation.getColumnIndexes());
		Collections.sort(columnIndexes);
		assertEquals(Arrays.asList(3, 5, 9, 11), columnIndexes);
	}

	@Test
	public void numberOfFeaturesDoesNotInflateObservationByCallingGetFeatures() {
		int[] columns = {3, 5, 9, 11};
		Integer[] values = new Integer[4];
		Arrays.fill(values, 1);
		SparseObservation<Integer> observation = new SparseObservation<Integer>(values, columns, 30, 0) {
			@Override
			public List<Integer> getFeatures() {
				throw new IllegalStateException("This was not supposed to happen. Should use internal number rather than inflating.");
			}
		};
		assertEquals(30, observation.numberOfFeatures());
	}


	private static class ExceptsOnGetFeatures extends SparseObservation<Integer> {
		ExceptsOnGetFeatures(Integer[] values, int[] indexes) {
			super(values, indexes, 10, 0);
		}

		@Override
		public List<Integer> getFeatures() {
			throw new RuntimeException("Should use number of columns rather than expanding which, to be frank, " +
					"is not what sparse means");
		}
	}

}