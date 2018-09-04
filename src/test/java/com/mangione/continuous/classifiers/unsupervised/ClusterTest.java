package com.mangione.continuous.classifiers.unsupervised;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.junit.Test;

import com.mangione.cse151.observations.Observation;

public class ClusterTest {
    @Test
    public void testMeanUpdateOnAddFirst() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(1);
        assertNull(cluster.getCentroid());
        cluster.add(new Observation(new double[]{1}));
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
    }

    @Test
    public void testMeanUpdateOnUpdate() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(1);
        cluster.add(new Observation(new double[]{1}));
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
        cluster.add(new Observation(new double[]{5}));
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
        cluster.updateCentroid();
        assertEquals(3, cluster.getCentroid().getFeatures()[0], 0);

    }

    @Test
    public void testMeanUpdateMultiDimensions() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        final Observation observation1 = new Observation(new double[]{1, 2});
        cluster.add(observation1);
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
        assertEquals(2, cluster.getCentroid().getFeatures()[1], 0);
        final Observation observation = new Observation(new double[]{5, 2});
        cluster.add(observation);
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
        assertEquals(2, cluster.getCentroid().getFeatures()[1], 0);

        assertEquals(3, cluster.getCentroid().getFeatures()[0], 0);
        assertEquals(2, cluster.getCentroid().getFeatures()[1], 0);

        List<Observation> observations = cluster.getObservations();
        assertEquals(observation1, observations.get(0));
        assertEquals(observation, observations.get(1));
    }

    @Test
    public void distanceToCentroid() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        cluster.add(new Observation(new double[]{1, 2}));
        cluster.add(new Observation(new double[]{5, 2}));
        Observation centroid = cluster.getCentroid();
        EuclideanDistance ed = new EuclideanDistance();
        final double[] test = {10, 12};
        assertEquals(ed.compute(centroid.getFeatures(), test), cluster.distanceToCentroid(new Observation(test)), 0.00000001);
    }

    @Test
    public void withinClusterSumOfSquares() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        final Observation observation1 = new Observation(new double[]{1, 2});
        cluster.add(observation1);
        assertEquals(1, cluster.getCentroid().getFeatures()[0], 0);
        assertEquals(2, cluster.getCentroid().getFeatures()[1], 0);
        final Observation observation = new Observation(new double[]{5, 2});
        cluster.add(observation);
        EuclideanDistance ed = new EuclideanDistance();
        double sumOfSquares = Math.pow(ed.compute(observation.getFeatures(), cluster.getCentroid().getFeatures()), 2) +
                Math.pow(ed.compute(observation1.getFeatures(), cluster.getCentroid().getFeatures()), 2);
        assertEquals(sumOfSquares, cluster.withinClusterSumOfSquares(), 0);
    }

    @Test
    public void removeObservation() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        final Observation observation1 = new Observation(new double[]{1, 2});
        cluster.add(observation1);
        final Observation observation = new Observation(new double[]{5, 2});
        cluster.add(observation);
        cluster.remove(observation1);
        assertEquals(5, cluster.getCentroid().getFeatures()[0], 0);
        assertEquals(2, cluster.getCentroid().getFeatures()[1], 0);

        List<Observation> observations = cluster.getObservations();
        assertEquals(1, observations.size());
        assertEquals(observation, observations.get(0));
    }

    @Test
    public void removeLast() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        final Observation observation1 = new Observation(new double[]{1, 2});
        cluster.add(observation1);
        final Observation observation = new Observation(new double[]{5, 2});
        cluster.add(observation);
        cluster.remove(observation1);
        cluster.remove(observation);

        List<Observation> observations = cluster.getObservations();
        assertEquals(0, observations.size());
        assertNull(cluster.getCentroid());
    }

    @Test
    public void removeWorksOffOfIdentity() throws Exception {
        Cluster<Observation> cluster = new Cluster<>(2);
        final Observation observation1 = new Observation(new double[]{1, 2});
        cluster.add(observation1);
        final Observation observation = new Observation(new double[]{5, 2});
        cluster.add(observation);
        cluster.remove(new Observation(new double[]{1, 2}));

        List<Observation> observations = cluster.getObservations();
        assertEquals(2, observations.size());
    }
}