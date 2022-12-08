package com.mangione.continuous.observations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.Observation;

public class ObservationInterfaceTest {

    private ObservationInterface<Double> observationInterface;

    @Before
    public void setUp() {
        observationInterface = new Observation<>(new Double[] {5d, 9d, 3.2, 0d});
    }

    @Test
    public void getFeatureDefaultImplementation() {
        assertEquals(5d, observationInterface.getFeature(0), 0);
        assertEquals(9d, observationInterface.getFeature(1), 0);
        assertEquals(3.2d, observationInterface.getFeature(2), 0);
        assertEquals(0d, observationInterface.getFeature(3), 0);
    }

    @Test
    public void getAllColumnIndexesDefaultImplementation() {
    	assertArrayEquals(new int[]{0, 1, 2, 3}, observationInterface.getColumnIndexes());
    }

    @Test
    public void numberOfFeatures() {
    	assertEquals(4, observationInterface.numberOfFeatures());
    }

}