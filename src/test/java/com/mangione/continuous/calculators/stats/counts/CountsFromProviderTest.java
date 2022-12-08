package com.mangione.continuous.calculators.stats.counts;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class CountsFromProviderTest {

    @Test
    public void countsWithTwoColumns() {
    	Integer[][] values  = {{0, 0}, {0, 0}, {1, 1}, {1, 0},  {1, 0},  {1, 0}};
        ArrayObservationProvider<Integer, ObservationInterface<Integer>> provider =
                new ArrayObservationProvider<>(values, Observation::new);

        CountsFromProvider<Integer, ObservationInterface<Integer>, ListObservationProvider<Integer,
                ObservationInterface<Integer>>, String> countsFromProvider = new CountsFromProvider<>(provider, this::generateKey);

        List<Count<String>> counts = countsFromProvider.getCounts();
        assertEquals(3, counts.size());
        assertEquals("10", counts.get(0).getKey());
        assertEquals(3, counts.get(0).getCount());
        assertEquals("00", counts.get(1).getKey());
        assertEquals(2, counts.get(1).getCount());
        assertEquals("11", counts.get(2).getKey());
        assertEquals(1, counts.get(2).getCount());
    }

    private String generateKey(ObservationInterface<Integer> observation) {
        return "" + observation.getFeature(0) + "" + observation.getFeature(1);
    }

}