package com.mangione.continuous.calculators.stats;

import org.junit.Test;

import static org.junit.Assert.*;

public class CountTest {
    @Test
    public void creationZerosCount() {
        Count count = new Count("key1");
        assertEquals(0, count.getCount());
        assertEquals("key1", count.getKey());
    }

    @Test
    public void incrementSome() {
        Count count = new Count("key1");
        assertEquals(0, count.getCount());
        count.add();
        assertEquals(1, count.getCount());
        count.add();
        assertEquals(2, count.getCount());
    }

}