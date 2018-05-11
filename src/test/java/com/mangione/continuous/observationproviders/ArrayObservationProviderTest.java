package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

public class ArrayObservationProviderTest {
    private static final Double[][] DATA = new Double[][]{{3d}, {5d}, {6d}, {7d}};

    private ArrayObservationProvider<Double, ObservationInterface<Double>> aop;

    @Before
    public void setUp() throws Exception {
        aop = new ArrayObservationProvider<>(DATA,
                new DoubleObservationFactory());
    }

    @Test
    public void iterator() throws Exception {
        int number = 0;
        for (ObservationInterface<Double> next : aop) {
            validateData(DATA[number], next.getFeatures());
            number++;
        }
        assertEquals(DATA.length, number);
    }


    @Test
    public void forEach() throws Exception {
        final int[] number = {0};
    	aop.forEach(observation -> {
	        validateData(DATA[number[0]], observation.getFeatures());
	        number[0]++;
	    });
        assertEquals(DATA.length, number[0]);
    }


    @Test
    public void fromProvider() throws Exception {
        final int[] number = {0};

        ArrayObservationProvider<Double, ObservationInterface<Double>> wrapped =
                new ArrayObservationProvider<>(aop, new DoubleObservationFactory());
        wrapped.forEach(observation -> {
            validateData(DATA[number[0]], observation.getFeatures());
            number[0]++;
        });
        assertEquals(DATA.length, number[0]);
    }

    @Test
    public void twoIteratorsWorks() throws Exception {
        final int[] number = {0};
        aop.forEach(observation -> {
            validateData(DATA[number[0]], observation.getFeatures());
            number[0]++;

            final int[] innerNumber = {0};
            aop.forEach(innerObservation -> {
                validateData(DATA[innerNumber[0]], innerObservation.getFeatures());
                innerNumber[0]++;
            });
            assertEquals(DATA.length, innerNumber[0]);

        });
        assertEquals(DATA.length, number[0]);
    }

    @Test
    public void remove() throws Exception {
        Iterator<ObservationInterface<Double>> iterator = aop.iterator();
        iterator.next();
        iterator.remove();
        assertArrayEquals(DATA[2], iterator.next().getFeatures());

        iterator = aop.iterator();
        assertArrayEquals(DATA[0], iterator.next().getFeatures());
        assertArrayEquals(DATA[2], iterator.next().getFeatures());
    }

    @Test
    public void forEachRemaining() throws Exception {
        Iterator<ObservationInterface<Double>> iterator = aop.iterator();
        iterator.next();
        final int[] innerNumber = {1};
        iterator.forEachRemaining(innerObservation -> {
            validateData(DATA[innerNumber[0]], innerObservation.getFeatures());
            innerNumber[0]++;
        });
        assertEquals(DATA.length, innerNumber[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void spliteratorNotSupported() throws Exception {
    	aop.spliterator();
    }


    private void validateData(Double[] datum, Double[] features) {
        assertEquals(1, features.length);
        assertEquals(datum[0], features[0], 0);
    }



}