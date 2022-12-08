package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import org.junit.Test;

import com.mangione.continuous.util.coersion.CoerceToIntArray;

public class SparseObservationTest {

	@Test
	public void aFewColumns() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(Integer.valueOf(9), sparseObservation.getFeatures(Integer[]::new)[0]);
		assertEquals(Integer.valueOf(9), sparseObservation.getFeature(0));

		assertEquals(Integer.valueOf(0), sparseObservation.getFeatures(Integer[]::new)[1]);
		assertEquals(Integer.valueOf(0), sparseObservation.getFeature(1));

		assertEquals(Integer.valueOf(8), sparseObservation.getFeatures(Integer[]::new)[2]);
		assertEquals(Integer.valueOf(8), sparseObservation.getFeature(2));

		assertEquals(Integer.valueOf(2), sparseObservation.getFeatures(Integer[]::new)[7]);
		assertEquals(Integer.valueOf(2), sparseObservation.getFeature(7));

		assertEquals(Integer.valueOf(0), sparseObservation.getFeatures(Integer[]::new)[9]);
		assertEquals(Integer.valueOf(0), sparseObservation.getFeature(9));

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
		assertArrayEquals(new Integer[]{9, 0, 8, 0, 0, 0, 0, 2, 0, 0}, sparseObservation.getFeatures(Integer[]::new));
	}

	@Test
	public void getColumnIndexes() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		ArrayList<Integer> columnIndexes = new ArrayList<>(Arrays.asList(CoerceToIntArray.coerce(sparseObservation.getColumnIndexes())));
		Collections.sort(columnIndexes);
		assertEquals(Arrays.asList(0, 2, 7), columnIndexes);
	}

	@Test
	public void getFeatureIndexExists() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(Integer.valueOf(9), sparseObservation.getFeature(0));
		assertEquals(Integer.valueOf(8), sparseObservation.getFeature(2));
		assertEquals(Integer.valueOf(2), sparseObservation.getFeature(7));
	}

	@Test
	public void getFeatureIndexMissing() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 0);
		assertEquals(Integer.valueOf(9), sparseObservation.getFeature(0));
	}

	@Test
	public void getFeatureIndexAfter() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 666);
		assertEquals(Integer.valueOf(666), sparseObservation.getFeature(9));
	}

	@Test
	public void setValueAtIndex() {
		SparseObservationBuilder<Integer> sparseObservationBuilder = new SparseObservationBuilder<>(10, 666);
		SparseObservation<Integer> sparseObservation = sparseObservationBuilder.build(Integer[]::new);
		IntStream.range(0, 10)
				.forEach(index -> assertEquals(Integer.valueOf(666), sparseObservation.getFeature(index)));

		sparseObservationBuilder.setFeature(5, 2);
		SparseObservation<Integer> sparseObservation1 = sparseObservationBuilder.build(Integer[]::new);
		IntStream.range(0, 10)
				.filter(index -> index != 5)
				.forEach(index -> assertEquals(Integer.valueOf(666), sparseObservation1.getFeature(index)));
		assertEquals(Integer.valueOf(2), sparseObservation1.getFeature(5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void valuesAndIndexesNotSameLengthExcepts() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2};

		new SparseObservation<>(values, indexes, 10, 666);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsOutOfRangeHigh() {
		new SparseObservationBuilder<>(10, 666).setFeature(10, 9);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValueAtIndexFailsIfIndexIsNegative() {
		new SparseObservationBuilder
				<>(10, 666).setFeature(-1, 9);
	}

	@Test
	public void missingValueNotZero() {
		Integer[] values = {9, 8, 2};
		int[] indexes = {0, 2, 7};

		SparseObservation<Integer> sparseObservation = new SparseObservation<>(values, indexes, 10, 666);
		assertArrayEquals(new Integer[] {9, 666, 8, 666, 666, 666, 666, 2, 666, 666}, sparseObservation.getFeatures(Integer[]::new));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueTooHighExcepts() {
		new SparseObservationBuilder<>(10, 666).build(Integer[]::new).getFeature(10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getValueNegativeExcepts() {
		new SparseObservationBuilder<>(10, 666).build(Integer[]::new).getFeature(-1);
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

		ArrayList<Integer> columnIndexes = new ArrayList<>(Arrays.asList(CoerceToIntArray.coerce(observation.getColumnIndexes())));
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
			public Integer[] getFeatures(IntFunction<Integer[]> featureBuilder) {
				throw new IllegalStateException("This was not supposed to happen. Should use internal number rather than inflating.");
			}
		};
		assertEquals(30, observation.numberOfFeatures());
	}

	@Test
	public void hashCodeAndEquals() {
		int[] columns = {3, 5, 9, 11};
		Integer[] values = new Integer[4];
		Arrays.fill(values, 1);
		SparseObservation<Integer> original = new SparseObservation<>(values, columns, 30, 0);
		SparseObservation<Integer> same = new SparseObservation<>(values, columns, 30, 0);
		columns = new int[]{4, 5, 9, 11};
		SparseObservation<Integer> differentColumns = new SparseObservation<>(values, columns, 30, 0);
		columns = new int[]{3, 5, 9, 11};
		values = new Integer[] {5, 1, 1, 1};
		SparseObservation<Integer> differentValue = new SparseObservation<>(values, columns, 30, 0);

		columns = new int[]{3, 5, 9, 11};
		values = new Integer[] {1, 1, 1, 1};
		SparseObservation<Integer> differentNumberOfColumns = new SparseObservation<>(values, columns, 31, 0);
		SparseObservation<Integer> differentMissingValue = new SparseObservation<>(values, columns, 30, 1);

		assertEquals(original, same);
		assertEquals(original.hashCode(), same.hashCode());

		assertNotEquals(original, differentColumns);
		assertNotEquals(original.hashCode(), differentColumns.hashCode());

		assertNotEquals(original, differentValue);
		assertNotEquals(original.hashCode(), differentValue.hashCode());

		assertNotEquals(original, differentNumberOfColumns);
		assertNotEquals(original.hashCode(), differentNumberOfColumns.hashCode());

		assertNotEquals(original, differentMissingValue);
		assertNotEquals(original.hashCode(), differentMissingValue.hashCode());

	}

	private static class ExceptsOnGetFeatures extends SparseObservation<Integer> {
		ExceptsOnGetFeatures(Integer[] values, int[] indexes) {
			super(values, indexes, 10, 0);
		}

		@Override
		public Integer[] getFeatures(IntFunction<Integer[]> featureBuilder) {
			throw new RuntimeException("Should use number of columns rather than expanding which, to be frank, " +
					"is not what sparse means");
		}
	}

}