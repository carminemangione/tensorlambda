package com.mangione.continuous.classifiers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.ExemplarInterface;


public class NNearestNeighborTest {

    @Test
    public void threePointsOneClass() throws Exception {
        Double[][] training = {{1d, 1d, 1d}, {2d, 2d, 1d}, {1d, 1.5, 1d}};

        ArrayObservationProvider<Double, ExemplarInterface<Double, Integer>> provider =
		        new ArrayObservationProvider<>(training, new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2., 3.}, 1.0, 1)));
    }

    @Test
    public void sixPointsTwoClassesSeparable() throws Exception {
        Double[][] training = {{1., 1., 1.}, {2., 2., 1.}, {1., 1.5, 1.}, {-4., -3., 0.}, {-6., -7., 0.}, {-5., -6., 0.}};

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider = new ArrayObservationProvider<>(training, new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2d, 3d}, 1.0, 1)));
    }

    @Test
    public void sixPointsOneMisclassified() throws Exception {
        Double[][] training = {{1., 1., 1.}, {2., 2., 1.}, {1., 1.5, 1.}, {-4., -3., 1.}, {-6., -7., 0.}, {-5., -6., 0.}};

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider = new ArrayObservationProvider<>(training, new DiscreteExemplarFactory());
        int n = 2;
        NNearestNeighbor nearestNeighbor = new NNearestNeighbor(provider, n);
        assertEquals(1, nearestNeighbor.classify(new DiscreteExemplar<>(new Double[]{2., 3.}, 1.0, 1)));
    }
}
