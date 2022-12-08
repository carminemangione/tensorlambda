package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.function.IntFunction;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SparseExemplarTest {
	@Test
	public void noValuesNoColumns() {
		SparseExemplar<Integer, Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{0}, new int[]{0}, 5, 0, 666);
		assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, sparseExemplar.getFeatures(Integer[]::new));
		assertEquals(666, (int) sparseExemplar.getLabel());
	}

	@Test
	public void targetIndexAndValue() {
		SparseExemplar<Integer, Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10}, new int[]{1, 3, 4}, 5, 0, 3);
		assertEquals(3, (int) sparseExemplar.getLabel());
	}

	@Test(expected = IllegalStateException.class)
	public void numberOfColumnsDoesNotNumberOfValuesExcepts() {
		new SparseExemplar<>(new Integer[]{3, 4, 5, 10, 11}, new int[]{1, 3, 4, 5}, 6, 666, 3);
	}

	@Test
	public void getFeaturesDoesNotIncludeTarget() {
		SparseExemplar<Integer, Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10, 11}, new int[]{1, 3, 4, 5}, 6, 666, 3);
		assertArrayEquals(new Integer[]{666, 3, 666, 5, 10, 11}, sparseExemplar.getFeatures(Integer[]::new));
	}

	@Test
	public void targetButNoFeatures() {
		SparseExemplar<Integer, Integer> sparseExemplar = new ExceptsOnGetFeatures(0, 0, 11);
		assertEquals(11, sparseExemplar.getLabel().intValue());
		assertEquals(0, sparseExemplar.numberOfFeatures());
	}

	@Test
	public void targetGetNumberOfFeaturesDoesNotCallExpand() {
		SparseExemplar<Integer, Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{11}, new int[]{0}, 1, 666, 0);
		assertEquals(0, sparseExemplar.getLabel().intValue());
		assertEquals(1, sparseExemplar.getFeatures(Integer[]::new).length);
	}

	@Test(expected = IllegalStateException.class)
	public void tooManyValues() {
		new SparseExemplar<>(new Integer[23], new int[]{0}, 5, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroNumberOfColumnsExcepts() {
		new SparseExemplar<>(new Integer[2], new int[1], 0, 0, 0);
	}


	@Test(expected = IllegalStateException.class)
	public void targetColumnNegativeExcepts() {
		new SparseExemplar<>(new Integer[]{0, 1}, new int[] {-1, 10}, 2, 0, 0);
	}

	@Test
	public void getValuesAtIndexLowerThanTarget() {
		SparseExemplar<Integer, Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10}, new int[]{1, 3, 4}, 5, 666, 3);
		assertEquals(Integer.valueOf(666), sparseExemplar.getFeature(0));
		assertEquals(Integer.valueOf(3), sparseExemplar.getFeature(1));
		assertEquals(Integer.valueOf(666), sparseExemplar.getFeature(2));
		assertEquals(5, sparseExemplar.numberOfFeatures());
	}

	@Test
	public void valuesAddedRatherThanArrayInputPlacesTargetAtEnd() {
		SparseExemplarBuilder<Integer, Integer> sparseExemplar = new SparseExemplarBuilder<>(5, 666, 20);
		sparseExemplar.setFeature(0, 2);
		sparseExemplar.setFeature(2, 10);
		sparseExemplar.setFeature(3, 11);
	}


	private static class ExceptsOnGetFeatures extends SparseExemplar<Integer, Integer> {
		ExceptsOnGetFeatures(int numberOfColumns, Integer missingValue, Integer target) {
			super(new Integer[numberOfColumns], new int[numberOfColumns], numberOfColumns, missingValue, target);
		}

		@Override
		public Integer[] getFeatures(IntFunction<Integer[]> featureBuilder) {
			throw new RuntimeException("Should use number of columns");
		}
	}

}