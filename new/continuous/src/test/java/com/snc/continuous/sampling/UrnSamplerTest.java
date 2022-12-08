package com.mangione.continuous.sampling;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class UrnSamplerTest {

    @Test
    public void pick() {
    }

    @Test
    public void hasNext() {
        UrnSampler urnSampler = new UrnSampler(false, new Random(), 1000, 5000, 7000);
        int i = 0;
        while (urnSampler.hasNext()) {
            i++;
            urnSampler.pick();
        }
        assertEquals(13000, i);

    }

    @Test
    public void getCounts() {
    }
}