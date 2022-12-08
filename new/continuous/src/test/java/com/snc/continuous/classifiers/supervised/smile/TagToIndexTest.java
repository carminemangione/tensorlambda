package com.mangione.continuous.classifiers.supervised.smile;

import static org.junit.Assert.*;
import org.junit.Test;

public class TagToIndexTest {
	@Test
	public void labelsOutOfOrder() {
		int[] labels = {9, 7, 0, 9, 2};
		TagToIndex tagToIndex = new TagToIndex(labels);
		assertEquals(4, tagToIndex.size());
		assertEquals(0, (int) tagToIndex.getIndex(0));
		assertEquals(1, (int) tagToIndex.getIndex(2));
		assertEquals(2, (int) tagToIndex.getIndex(7));
		assertEquals(3, (int) tagToIndex.getIndex(9));
	}

	@Test
	public void labelsOutOfOrderGetTag() {
		int[] labels = {9, 7, 0, 9, 2};
		TagToIndex tagToIndex = new TagToIndex(labels);
		assertEquals(4, tagToIndex.size());
		assertEquals(0, (int) tagToIndex.getTag(0));
		assertEquals(2, (int) tagToIndex.getTag(1));
		assertEquals(7, (int) tagToIndex.getTag(2));
		assertEquals(9, (int) tagToIndex.getTag(3));
	}
}