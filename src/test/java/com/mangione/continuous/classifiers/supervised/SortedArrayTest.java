package com.mangione.continuous.classifiers.supervised;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mangione.continuous.classifiers.supervised.SortedArray;

public class SortedArrayTest {
    @Test
    public void insert3InOrder() throws Exception {
        SortedArray<Double> sortedArray = new SortedArray<>(3);
        sortedArray.add(1.0);
        sortedArray.add(2.0);
        sortedArray.add(3.0);
        assertEquals(3, sortedArray.get().size());
        assertEquals(1, sortedArray.get().get(0), 0);
        assertEquals(2, sortedArray.get().get(1), 0);
        assertEquals(3, sortedArray.get().get(2), 0);
    }

    @Test
    public void insert4InOrderReturnsLowesThree() throws Exception {
        SortedArray<Double> sortedArray = new SortedArray<>(4);
        sortedArray.add(1.);
        sortedArray.add(2.);
        sortedArray.add(3.);
        sortedArray.add(4.);

        assertEquals(4, sortedArray.get().size());
        assertEquals(1, sortedArray.get().get(0), 0);
        assertEquals(2, sortedArray.get().get(1), 0);
        assertEquals(3, sortedArray.get().get(2), 0);
        assertEquals(4, sortedArray.get().get(3), 0);
    }

    @Test
    public void insertOneAtBeginning() throws Exception {
        SortedArray<Double> sortedArray = new SortedArray<>(2);
        sortedArray.add(10.);
        sortedArray.add(20.);
        sortedArray.add(0.);

        assertEquals(2, sortedArray.get().size());
        assertEquals(0, sortedArray.get().get(0), 0);
        assertEquals(10, sortedArray.get().get(1), 0);
    }

    @Test
    public void insertOneAtEnd() throws Exception {
        SortedArray<Double> sortedArray = new SortedArray<>(2);
        sortedArray.add(10.);
        sortedArray.add(25.);
        sortedArray.add(20.);

        assertEquals(10, sortedArray.get().get(0), 0);
        assertEquals(20, sortedArray.get().get(1), 0);
    }

    @Test
    public void insertOneInMiddle() throws Exception {
        SortedArray<Double> sortedArray = new SortedArray<>(2);
        sortedArray.add(10.);
        sortedArray.add(20.);
        sortedArray.add(15.);

        assertEquals(10, sortedArray.get().get(0), 0);
        assertEquals(15, sortedArray.get().get(1), 0);
    }


}