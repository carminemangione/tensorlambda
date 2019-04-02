package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SparseExemplarTest {
	@Test
	public void noValuesNoColumns() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{0}, new Integer[]{0}, 5, 0, 0);
		assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, sparseExemplar.getAllColumns().toArray());
		assertArrayEquals(new Integer[]{0, 0, 0, 0}, sparseExemplar.getFeatures().toArray());
		assertEquals(0, sparseExemplar.getTargetIndex());
		assertEquals(0, (int) sparseExemplar.getTarget());
	}

	@Test
	public void targetIndexAndValue() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10}, new Integer[]{1, 3, 4}, 5, 0, 3);
		assertEquals(3, sparseExemplar.getTargetIndex());
		assertEquals(5, (int) sparseExemplar.getTarget());
	}

	@Test
	public void getFeaturesDoesNotIncludeTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10, 11}, new Integer[]{1, 3, 4, 5}, 6, 666, 3);
		assertEquals(Arrays.asList(666, 3, 666, 10, 11), sparseExemplar.getFeatures());
	}

	@Test
	public void targetButNoFeatures() {
		SparseExemplar<Integer> sparseExemplar = new ExceptsOnGetFeatures(0, 0, 11);
		assertEquals(11, sparseExemplar.getTarget().intValue());
		assertEquals(0, sparseExemplar.numberOfFeatures());
	}

	@Test
	public void targetGetNumberOfFeaturesDoesNotCallExpand() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{11}, new Integer[]{0}, 1, 666, 0);
		assertEquals(11, sparseExemplar.getTarget().intValue());
		assertEquals(0, sparseExemplar.getFeatures().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void valuesDoesNotContainTarget() {
		new SparseExemplar<>(new Integer[0], new Integer[0], 5, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyValues() {
		new SparseExemplar<>(new Integer[23], new Integer[]{0}, 5, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroNumberOfColumnsExcepts() {
		new SparseExemplar<>(new Integer[2], new Integer[1], 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnNotInSparseInput() {
		new SparseExemplar<>(new Integer[]{0, 1}, new Integer[] {2, 10}, 3, 0, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnTooHighExcepts() {
		new SparseExemplar<>(new Integer[]{0, 1}, new Integer[] {2, 10}, 2, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnNegativeExcepts() {
		new SparseExemplar<>(new Integer[]{0, 1}, new Integer[] {-1, 10}, 2, 0, 0);
	}

	@Test
	public void getValuesAtIndexLowerThanTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10}, new Integer[]{1, 3, 4}, 5, 666, 3);
		assertEquals(new Integer(666), sparseExemplar.getFeature(0));
		assertEquals(new Integer(3), sparseExemplar.getFeature(1));
		assertEquals(new Integer(666), sparseExemplar.getFeature(2));
		assertEquals(4, sparseExemplar.numberOfFeatures());
	}

	@Test
	public void getFeatureIndexHigherThanTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10}, new Integer[]{1, 3, 4}, 5, 666, 3);
		assertEquals(new Integer(10), sparseExemplar.getFeature(4));
	}


	@Test(expected = IllegalArgumentException.class)
	public void getFeatureTargetExcepts() {
		new SparseExemplar<>(new Integer[]{3, 5, 10, 11}, new Integer[]{1, 3, 4, 5}, 6, 666, 3).getFeature(3);
	}

	@Test
	public void getAllColumnsTargetIndexNotAtEnd() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new Integer[]{3, 5, 10, 11}, new Integer[]{1, 3, 4, 5}, 6, 666, 3);
		assertEquals(Arrays.asList(666, 3, 666, 5, 10, 11), sparseExemplar.getAllColumns());
	}

	@Test
	public void valuesAddedRatherThanArrayInputPlacesTargetAtEnd() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(5, 666, 20);
		sparseExemplar.setFeature(0, 2);
		sparseExemplar.setFeature(2, 10);
		sparseExemplar.setFeature(3, 11);
		assertEquals(Arrays.asList(2, 666, 10, 11, 666, 20), sparseExemplar.getAllColumns());
	}

	private class ExceptsOnGetFeatures extends SparseExemplar<Integer> {
		ExceptsOnGetFeatures(int numberOfColumns, Integer missingValue, Integer target) {
			super(numberOfColumns, missingValue, target);
		}

		@Override
		public List<Integer> getFeatures() {
			throw new RuntimeException("Should use number of columns");
		}
	}

}