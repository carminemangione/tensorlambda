package com.mangione.continuous.classifiers.unsupervised;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mangione.cse151.observationproviders.ArrayObservationProvider;
import com.mangione.cse151.observations.Observation;
import com.mangione.cse151.observations.ObservationFactory;

public class KMeansTest {
    @Test
    public void testTwoClustersTwoObservations() throws Exception {
        double[][] data = {{2, 1}, {4, 2}};
        ArrayObservationProvider<Observation> provider = new ArrayObservationProvider<>(data, new ObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, provider);
        List<Cluster<Observation>> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        for (int i = 0; i < data.length; i++) {
            assertArrayEquals(data[i], clusters.get(i).getObservations().get(0).getFeatures(), 0);
        }
    }

    @Test
    public void testTwoClustersFourObservations() throws Exception {
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(new double[] {0}));
        observations.add(new Observation(new double[] {10}));
        observations.add(new Observation(new double[] {1}));
        observations.add(new Observation(new double[] {11}));

        ArrayObservationProvider<Observation> provider = new ArrayObservationProvider<>(observations, new ObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, provider);
        List<Cluster<Observation>> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        assertEquals(2, kmeans.getClusters().get(0).getObservations().size());
        assertTrue(kmeans.getClusters().get(0).getObservations().contains(observations.get(0)));
        assertTrue(kmeans.getClusters().get(1).getObservations().contains(observations.get(3)));
        assertEquals(1, kmeans.getWithinClusterSumOfSquares(), 0);
    }

    @Test
    public void testTwoWillJiggleWithOutlier() throws Exception {
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(new double[] {0}));
        observations.add(new Observation(new double[] {10}));
        observations.add(new Observation(new double[] {1}));
        observations.add(new Observation(new double[] {11}));
        observations.add(new Observation(new double[] {100}));


        ArrayObservationProvider<Observation> provider = new ArrayObservationProvider<>(observations, new ObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, provider);
        List<Cluster<Observation>> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        assertEquals(4, kmeans.getClusters().get(0).getObservations().size());
        assertTrue(kmeans.getClusters().get(0).getObservations().contains(observations.get(0)));
        assertTrue(kmeans.getClusters().get(0).getObservations().contains(observations.get(1)));
        assertTrue(kmeans.getClusters().get(0).getObservations().contains(observations.get(2)));
        assertTrue(kmeans.getClusters().get(0).getObservations().contains(observations.get(3)));

        assertEquals(1, kmeans.getClusters().get(1).getObservations().size());
        assertTrue(kmeans.getClusters().get(1).getObservations().contains(observations.get(4)));

    }

    @Test
    public void clusterIndex() throws Exception {
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(new double[] {0}));
        observations.add(new Observation(new double[] {10}));
        observations.add(new Observation(new double[] {1}));
        observations.add(new Observation(new double[] {11}));
        observations.add(new Observation(new double[] {100}));


        ArrayObservationProvider<Observation> provider = new ArrayObservationProvider<>(observations, new ObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, provider);
        assertEquals(0, kmeans.getClusterIndex(new Observation(new double[]{2})));
        assertEquals(0, kmeans.getClusterIndex(new Observation(new double[]{12})));
        assertEquals(0, kmeans.getClusterIndex(new Observation(new double[]{20})));
        assertEquals(1, kmeans.getClusterIndex(new Observation(new double[]{80})));

    }




}
