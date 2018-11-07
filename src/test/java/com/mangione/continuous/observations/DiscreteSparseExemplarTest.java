package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiscreteSparseExemplarTest {
	@Test
	public void noValuesNoColumns() throws Exception {
		DiscreteSparseExemplar discreteSparseExemplar = new DiscreteSparseExemplar(new int[]{0}, new int[0], 5);
		assertArrayEquals(new Integer[]{0, 0, 0, 0, 0}, discreteSparseExemplar.getAllColumns().toArray());
		assertArrayEquals(new Integer[]{0, 0, 0, 0}, discreteSparseExemplar.getFeatures().toArray());
		assertEquals(4, discreteSparseExemplar.getTargetIndex());
		assertEquals(0, (int)discreteSparseExemplar.getTarget());
	}


	@Test
	public void someValuesNoColumns() throws Exception {
		DiscreteSparseExemplar discreteSparseExemplar = new DiscreteSparseExemplar(new int[]{3, 5, 10}, new int[]{1, 3}, 5);
		assertArrayEquals(new Integer[]{0, 3, 0, 5, 10}, discreteSparseExemplar.getAllColumns().toArray());
		assertArrayEquals(new Integer[]{0, 3, 0, 5}, discreteSparseExemplar.getFeatures().toArray());
		assertEquals(4, discreteSparseExemplar.getTargetIndex());
		assertEquals(10, (int)discreteSparseExemplar.getTarget());
	}

	@Test(expected = IllegalArgumentException.class)
	public void valuesDoesNotContainTarget() throws Exception {
		new DiscreteSparseExemplar(new int[0], new int[0], 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyValues() throws Exception {
		new DiscreteSparseExemplar(new int[2], new int[0], 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroNumberOfColumnsExcepts() throws Exception {
		new DiscreteSparseExemplar(new int[2], new int[1], 0);
	}
}