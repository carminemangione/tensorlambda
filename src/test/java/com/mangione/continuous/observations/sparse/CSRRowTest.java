package com.mangione.continuous.observations.sparse;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CSRRowTest {

    @Test
    public void createRow() {
    	int[] indexes = {0, 5, 8};
    	double[] values = {2, 9, 8};
        CSRRow csRrow = new CSRRow(indexes, values);
        assertArrayEquals(indexes, csRrow.getIndexes());
        assertArrayEquals(values, csRrow.getValues(), 0);
        assertEquals(3, csRrow.getNumValues());
    }

    @Test
    public void fromObservation() {
        SparseObservation<Integer> observation = new SparseObservation<>(10, 0);
        observation.setFeature(2, 100);
        observation.setFeature(7, 88);
        CSRRow csrRow = new CSRRow(observation);
        assertEquals(2, csrRow.getNumValues());
        assertArrayEquals(new double[]{100, 88}, csrRow.getValues(), 0);
        assertArrayEquals(new int[]{2, 7}, csrRow.getIndexes());
    }


    @Test(expected = IllegalArgumentException.class)
    public void indexesMayNotBeNull()  {
        new CSRRow(null, new double[]{0, 9});
    }

    @Test(expected = IllegalArgumentException.class)
    public void valuesMayNotBeNull()  {
        new CSRRow(new int[]{1, 3}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexesAndValuesMustHaveSameLength()  {
        new CSRRow(new int[]{1, 3}, new double[]{0, 9, 0});
    }
}