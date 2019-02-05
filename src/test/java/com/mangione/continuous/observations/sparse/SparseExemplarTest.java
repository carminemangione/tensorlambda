package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SparseExemplarTest {
	@Test
	public void noValuesNoColumns() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{0}, new Integer[]{0}, 5, 0, 0);
		assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, sparseExemplar.getAllColumns().toArray());
		assertArrayEquals(new Integer[]{0, 0, 0, 0}, sparseExemplar.getFeatures().toArray());
		assertEquals(0, sparseExemplar.getTargetIndex());
		assertEquals(0, (int) sparseExemplar.getTarget());
	}


	@Test
	public void targetIndexAndValue() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{1, 3, 4}, new Integer[]{3, 5, 10}, 5, 0, 3);
		assertEquals(3, sparseExemplar.getTargetIndex());
		assertEquals(5, (int) sparseExemplar.getTarget());
	}


	@Test
	public void getFeaturesDoesNotIncludeTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{1, 3, 4, 5}, new Integer[]{3, 5, 10, 11}, 6, 666, 3);
		assertEquals(Arrays.asList(666, 3, 666, 10, 11), sparseExemplar.getFeatures());
	}


	@Test(expected = IllegalArgumentException.class)
	public void valuesDoesNotContainTarget() {
		new SparseExemplar<>(new int[0], new Integer[0], 5, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyValues() {
		new SparseExemplar<>(new int[]{0}, new Integer[23], 5, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroNumberOfColumnsExcepts() {
		new SparseExemplar<>(new int[1], new Integer[2], 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnNotInSparseInput() {
		new SparseExemplar<>(new int[] {2, 10}, new Integer[]{0, 1}, 3, 0, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnTooHighExcepts() {
		new SparseExemplar<>(new int[] {2, 10}, new Integer[]{0, 1}, 2, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnNegativeExcepts() {
		new SparseExemplar<>(new int[] {-1, 10}, new Integer[]{0, 1}, 2, 0, 0);
	}

	@Test
	public void getValuesAtIndexLowerThanTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{1, 3, 4}, new Integer[]{3, 5, 10}, 5, 666, 3);
		assertEquals(new Integer(666), sparseExemplar.getFeature(0));
		assertEquals(new Integer(3), sparseExemplar.getFeature(1));
		assertEquals(new Integer(666), sparseExemplar.getFeature(2));
	}

	@Test
	public void getFeatureIndexHigherThanTarget() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{1, 3, 4}, new Integer[]{3, 5, 10}, 5, 666, 3);
		assertEquals(new Integer(10), sparseExemplar.getFeature(4));
	}


	@Test(expected = IllegalArgumentException.class)
	public void getFeatureTargetExcepts() {
		new SparseExemplar<>(new int[]{1, 3, 4, 5}, new Integer[]{3, 5, 10, 11}, 6, 666, 3).getFeature(3);
	}

	@Test
	public void getAllColumnsTargetIndexNotAtEnd() {
		SparseExemplar<Integer> sparseExemplar = new SparseExemplar<>(new int[]{1, 3, 4, 5}, new Integer[]{3, 5, 10, 11}, 6, 666, 3);
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
}