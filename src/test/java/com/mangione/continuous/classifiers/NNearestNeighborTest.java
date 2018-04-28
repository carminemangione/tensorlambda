package com.mangione.continuous.classifiers;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.ExemplarInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NNearestNeighborTest {

    @Test
    public void threePointsOneClass() throws Exception {
        Double[][] training = {{1d, 1d, 1d}, {2d, 2d, 1d}, {1d, 1.5d, 1d}};

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider = new ArrayObservationProvider<>(training,
                new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor = new NNearestNeighbor<>(provider, 
                new DiscreteExemplarFactory(), n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2d, 3d}, 1.0, 1)));
    }

    @Test
    public void sixPointsTwoClassesSeparable() throws Exception {
        Double[][] training = {{1d, 1d, 1d}, {2d, 2d, 1d}, {1d, 1.5d, 1d}, {-4d, -3d, 0d}, {-6d, -7d, 0d}, {-5d, -6d, 0d}};

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider = new ArrayObservationProvider<>(
                training, new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor = new NNearestNeighbor<>(provider, 
                new DiscreteExemplarFactory(), n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2d, 3d}, 1.0, 1)));
    }

    @Test
    public void sixPointsOneMisclassified() throws Exception {
        Double[][] training = {{1d, 1d, 1d}, {2d, 2d, 1d}, {1d, 1.5d, 1d}, {-4d, -3d, 1d}, {-6d, -7d, 0d}, {-5d, -6d, 0d}};

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider =
                new ArrayObservationProvider<>(training, new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor = new NNearestNeighbor<>(provider,
                new DiscreteExemplarFactory(), n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2d, 3d}, 1.0, 1)));
    }
}
