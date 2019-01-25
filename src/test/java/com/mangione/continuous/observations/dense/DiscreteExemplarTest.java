package com.mangione.continuous.observations.dense;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observations.dense.DiscreteExemplar;

public class DiscreteExemplarTest {
	@Test
	public void targetLastColumn() {
		DiscreteExemplar<Integer> exemplarTargetLastColumn = DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 2, 3, 4));
		assertEquals(new Integer(4), exemplarTargetLastColumn.getTarget());
		assertEquals(Arrays.asList(1, 2, 3), exemplarTargetLastColumn.getFeatures());
		assertEquals(Arrays.asList(1, 2, 3, 4), exemplarTargetLastColumn.getAllColumns());
		assertEquals(3, exemplarTargetLastColumn.getTargetIndex());
	}

	@Test
	public void targetMiddleColumn() {
		DiscreteExemplar<Integer> exemplarTargetLastColumn =
				DiscreteExemplar.getExemplarTargetWithColumn(Arrays.asList(1, 2, 3, 4), 2);
		assertEquals(new Integer(3), exemplarTargetLastColumn.getTarget());
		assertEquals(Arrays.asList(1, 2, 4), exemplarTargetLastColumn.getFeatures());
		assertEquals(Arrays.asList(1, 2, 3, 4), exemplarTargetLastColumn.getAllColumns());
		assertEquals(2, exemplarTargetLastColumn.getTargetIndex());
	}

	@Test
	public void exemplarKeepsCopyOfList() {
		List<Integer> original = Arrays.asList(1, 2, 3, 4);
		DiscreteExemplar.getExemplarTargetLastColumn(original).getFeatures().remove(1);
		assertEquals(Arrays.asList(1, 2, 3, 4), original);

		DiscreteExemplar.getExemplarTargetWithColumn(original, 2).getFeatures().remove(2);
		assertEquals(Arrays.asList(1, 2, 3, 4), original);
	}

	@Test(expected = IllegalArgumentException.class)
	public void targetColumnOnEmptyExcepts()  {
		DiscreteExemplar.getExemplarTargetLastColumn(Collections.emptyList());
	}


	@Test(expected = IllegalArgumentException.class)
	public void targetColumnTooBigExcepts()  {
		DiscreteExemplar.getExemplarTargetWithColumn(Collections.singletonList(1), 20);
	}

	@Test
	public void getColumnsDoesNotIncludeTargetWhenNotAtEnd() {
		DiscreteExemplar<Integer> exemplarTargetLastColumn = DiscreteExemplar.getExemplarTargetWithColumn(Arrays.asList(1, 2, 3, 4),2);
		assertEquals(Arrays.asList(1, 2, 4), exemplarTargetLastColumn.getFeatures());
	}

}