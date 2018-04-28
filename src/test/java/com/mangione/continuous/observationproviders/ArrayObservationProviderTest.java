package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayObservationProviderTest {
    @Test
    public void oneColumn() throws Exception {
        Double[][] data = {{3d},{5d},{6d}, {7d}};
        ArrayObservationProvider<Double, ObservationInterface<Double>> aop = new ArrayObservationProvider<>(data,
                new DoubleObservationFactory<>());
        int number = 0;
        while (aop.hasNext()) {
            final ObservationInterface<Double> next = aop.next();
            Double[] features = next.getFeatures();
            assertEquals(1, features.length);
            assertEquals(data[number][0], features[0], 0);
            number++;
        }
        assertEquals(data.length, number);
    }

}