package com.mangione.continuous.classifiers.supervised.smile;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;

public class ProviderToSmileOneEncodedSparseTest {

	private int numberOfFeatures;

	@Before
	public void setUp() {
		numberOfFeatures = 100;
	}

	@Test
	public void encodeProvider() {
		List<SparseExemplar<Integer, Integer>> exemplars = new ArrayList<>(Arrays.asList(
				buildExemplar(new int[]{5, 40, 66, 92}, 1),
				buildExemplar(new int[]{7, 9, 11}, 2)));
		ListObservationProvider<Integer, SparseExemplar<Integer, Integer>> provider = new ListObservationProvider<>(exemplars);
		ProviderToSmileOneEncodedSparse<SparseExemplar<Integer, Integer>, ListObservationProvider<Integer, SparseExemplar<Integer, Integer>>> oneEncoded = new ProviderToSmileOneEncodedSparse<>(provider,
				SparseExemplar::getLabel);
		int[][] colEncoded = oneEncoded.getColEndoced();

		assertEquals(2, colEncoded.length);
		assertArrayEquals(new int[]{5, 40, 66, 92}, colEncoded[0]);
		assertArrayEquals(new int[]{7, 9, 11}, colEncoded[1]);
		int[] tags = oneEncoded.getTags();
		assertEquals(2, tags.length);
		assertArrayEquals(new int[]{1, 2}, tags);

		assertEquals(numberOfFeatures, oneEncoded.getNumberOfFeatures());
	}


	private SparseExemplar<Integer, Integer> buildExemplar(int[] columns, int target) {
		SparseExemplarBuilder<Integer, Integer> builder = new SparseExemplarBuilder<>(numberOfFeatures, 0, target);
		Arrays.stream(columns)
				.boxed()
				.forEach(column->builder.setFeature(column, 1));
		return builder.build(Integer[]::new);
	}


}