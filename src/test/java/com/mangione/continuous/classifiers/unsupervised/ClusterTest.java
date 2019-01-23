package com.mangione.continuous.classifiers.unsupervised;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.junit.Test;

import com.mangione.continuous.observations.Observation;


public class ClusterTest {
    @Test
    public void testMeanUpdateOnAddFirst() throws Exception {
        Cluster<double[]> cluster = new Cluster(1);
        assertNull(cluster.getCentroid());
        cluster.add(new double[]{1});
        new KMeans().updateCentroid(cluster);
        assertEquals(1, cluster.getCentroid()[0], 0);
    }

    @Test
    public void testMeanUpdateOnUpdate() throws Exception {
        Cluster<double[]> cluster = new Cluster(1);
        cluster.add(new double[]{1});
        new KMeans().updateCentroid(cluster);
        assertEquals(1, cluster.getCentroid()[0], 0);
        cluster.add(new double[]{5});
        assertEquals(1, cluster.getCentroid()[0], 0);
        new KMeans().updateCentroid(cluster);
        assertEquals(3, cluster.getCentroid()[0], 0);

    }

    @Test
    public void testMeanUpdateMultiDimensions() throws Exception {
        Cluster<double[]> cluster = new Cluster(2);
        cluster.add(new double[] {1,2});
        new KMeans().updateCentroid(cluster);
        assertEquals(1, cluster.getCentroid()[0], 0);
        assertEquals(2, cluster.getCentroid()[1], 0);
        cluster.add(new double[] {5,2});
        assertEquals(1, cluster.getCentroid()[0], 0);
        assertEquals(2, cluster.getCentroid()[1], 0);
        new KMeans().updateCentroid(cluster);
        assertEquals(3, cluster.getCentroid()[0], 0);
        assertEquals(2, cluster.getCentroid()[1], 0);

    }

    @Test
    public void distanceToCentroid() throws Exception {
        Cluster<double[]> cluster = new Cluster(2);
        cluster.add(new double[]{1, 2});
        cluster.add(new double[]{5, 2});
        new KMeans().updateCentroid(cluster);
        double[] centroid = cluster.getCentroid();
        EuclideanDistance ed = new EuclideanDistance();
        final double[] test = {10, 12};
        assertEquals(ed.compute(centroid, test), cluster.distanceToCentroid((test)), 0.00000001);
    }

    @Test
    public void withinClusterSumOfSquares() throws Exception {
        Cluster<double[]> cluster = new Cluster(2);
        cluster.add(new double[]{1, 2});
        new KMeans().updateCentroid(cluster);
        assertEquals(1, cluster.getCentroid()[0], 0);
        assertEquals(2, cluster.getCentroid()[1], 0);
        cluster.add(new double[]{5, 2});
        EuclideanDistance ed = new EuclideanDistance();
        double sumOfSquares = Math.pow(ed.compute(new double[]{1, 2}, cluster.getCentroid()), 2) +
                Math.pow(ed.compute(new double[]{5, 2}, cluster.getCentroid()), 2);
        //assertEquals(sumOfSquares, cluster.withinClusterSumOfSquares(), 0);
    }

    @Test
    public void removeObservation() throws Exception {
        Cluster<double[]> cluster = new Cluster(2);
        final double[] observation1 = new double[]{1, 2};
        cluster.add(observation1);
        final double[] observation = new double[]{5, 2};
        cluster.add(observation);
        cluster.remove(observation1);
        new KMeans().updateCentroid(cluster);
        assertEquals(5, cluster.getCentroid()[0], 0);
        assertEquals(2, cluster.getCentroid()[1], 0);

        List<double[]> observations = cluster.getObservations();
        assertEquals(1, observations.size());
        assertEquals(observation, observations.get(0));
    }

    @Test
    public void removeLast() throws Exception {
        Cluster cluster = new Cluster(2);
        final double[] observation1 =new double[]{1, 2};
        cluster.add(observation1);
        final double[] observation = new double[]{5, 2};
        cluster.add(observation);
        cluster.remove(observation1);
        cluster.remove(observation);

        List<double[]> observations = cluster.getObservations();
        assertEquals(0, observations.size());
        assertNull(cluster.getCentroid());
    }

    @Test
    public void removeWorksOffOfIdentity() throws Exception {
        Cluster cluster = new Cluster(2);
        final double[] observation1 = new double[]{1, 2};
        cluster.add(observation1);
        final double[] observation = new double[]{5, 2};
        cluster.add(observation);
        cluster.remove(new double[] {1,2});

        List<double[]> observations = cluster.getObservations();
        assertEquals(2, observations.size());
    }
}