package com.mangione.continuous.classifiers;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NNearestNeighborTest {

    @Test
    public void threePointsOneClass() throws Exception {
        double[][] training = {{1, 1, 1}, {2, 2, 1}, {1, 1.5, 1}};

        ObservationProvider<Observation> provider = new ArrayObservationProvider<>(training, new ObservationFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar(new double[]{2, 3}, 1.0, 1)));
    }

    @Test
    public void sixPointsTwoClassesSeparable() throws Exception {
        double[][] training = {{1, 1, 1}, {2, 2, 1}, {1, 1.5, 1}, {-4, -3, 0}, {-6, -7, 0}, {-5, -6, 0}};

        ObservationProvider<Observation> provider = new ArrayObservationProvider<>(training, new ObservationFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar(new double[]{2, 3}, 1.0, 1)));
    }

    @Test
    public void sixPointsOneMisclassified() throws Exception {
        double[][] training = {{1, 1, 1}, {2, 2, 1}, {1, 1.5, 1}, {-4, -3, 1}, {-6, -7, 0}, {-5, -6, 0}};

        ObservationProvider<Observation> provider = new ArrayObservationProvider<>(training, new ObservationFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar(new double[]{2, 3}, 1.0, 1)));
    }
}
