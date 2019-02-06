package com.mangione.continuous.observations;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ObservationInterfaceTest {

    private ObservationInterface<Double> observationInterface;

    @Before
    public void setUp() {
        observationInterface = new ObservationInterface<Double>() {
            @Override
            public List<Double> getFeatures() {
                return Arrays.asList(5d, 9d, 3.2, 0d);
            }

            @Override
            public List<Double> getAllColumns() {
                return Arrays.asList(5d, 9d, 3.2, 0d);
            }
        };
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
    	assertArrayEquals(Arrays.asList(0, 1, 2, 3).toArray(),
                observationInterface.getColumnIndexes().toArray());
    }



}