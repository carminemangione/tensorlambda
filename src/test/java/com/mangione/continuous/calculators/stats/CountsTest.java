package com.mangione.continuous.calculators.stats;

import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class CountsTest {

    @Test
    public void oneCount() {
        Counts counts = new Counts();
        assertEquals(0, counts.getCounts().size());
        counts.add("key1");
        assertEquals(1, counts.getCounts().size());
        Count count = counts.getCounts().get(0);
        assertEquals("key1", count.getKey());
        assertEquals(1, count.getCount());
        counts.add("key1");
        assertEquals(2, count.getCount());
    }

    @Test
    public void multipleReturnedInInverseOrderOfCount() {
        Counts counts = new Counts();
        incrementCount("key0", 5, counts);
        incrementCount("key1", 10, counts);
        incrementCount("key2", 1, counts);

        List<Count> countList = counts.getCounts();
        assertEquals(10, countList.get(0).getCount());
        assertEquals(5, countList.get(1).getCount());
        assertEquals(1, countList.get(2).getCount());

    }

    private void incrementCount(String key, int numHits, Counts counts) {
        IntStream.range(0, numHits).forEach(value -> counts.add(key));
    }
}