package com.mangione.continuous.observations.sparse;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CSRRowTest {

    @Test
    public void fromObservation() {
        SparseObservation<Integer> observation = new SparseObservation<>(10, 0);
        observation.setFeature(2, 100);
        observation.setFeature(7, 88);
        CSRRow<SparseObservation<Integer>> csrRow = new CSRRow<>(observation);

        assertEquals(2, csrRow.getNumValues());
        assertEquals(Arrays.asList(100.0, 88.0), csrRow.getValues());
        assertEquals(Arrays.asList(2, 7), csrRow.getIndexes());
    }

}