package com.mangione.continuous.classifiers.unsupervised;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.mangione.continuous.model.modelproviders.DoubleUnsupervisedModelProvider;
import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationInterface;

public class KMeansTest {
    @Test
    public void testTwoClustersTwoObservations() throws Exception {
        Double[][] data = {{2., 1.}, {4., 2.}};
        ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, new DoubleUnsupervisedModelProvider(provider));
        List<Cluster> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        for (int i = 0; i < data.length; i++) {
            assertArrayEquals(convertDoubleArrayUsingStream(data)[i], clusters.get(i).getObservations().get(0), 0);
        }
    }

    @Test
    public void testMultiThreading() throws Exception {
    	long initialTime = System.currentTimeMillis();
	    int n = 1000;
	    Double[][] data = new Double[n][1];
	    Random rand = new Random();

	    for (int i = 0; i < n; i++) {
		    data[i][0] = (double) i;
	    }
	    for(int numThreads = 1; numThreads < 4; numThreads++) {
		    for (int numClusters = 4; numClusters < 5; numClusters++) {
			    initialTime = System.currentTimeMillis();

			    ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
			    KMeans<Observation> kmeans = new KMeans<>(numClusters, new DoubleUnsupervisedModelProvider(provider), numThreads);
			    List<Cluster> clusters = kmeans.getClusters();
			    assertEquals(numClusters, clusters.size());

				System.out.println("Checking ");
			    for (int i = 0; i < clusters.size(); i++) {
				    for (double[] elem : clusters.get(i).getObservations()) {
					    double dist = clusters.get(i).distanceToCentroid(elem);
					    for (int j = 0; j < clusters.size(); j++) {
						    assertTrue(clusters.get(i).distanceToCentroid(elem) >= dist);
					    }
				    }
			    }

			    System.out.println("Time: " + (System.currentTimeMillis() - initialTime) + " Number Of Threads: " + numThreads + " Number of Clusters: " + numClusters);
		        clusters.forEach(x -> System.out.println(x.getCentroid()[0]));
		    }

	    }
    }

    public double[][] convertDoubleArrayUsingStream(Double[][] data) {
        return stream(data)
                .map(row -> stream(row)
                        .mapToDouble(Number::doubleValue).toArray())
                .toArray(double[][]::new);
    }

    @Test
    public void testTwoClustersFourObservations() throws Exception {
        Double[][] data = {{0.}, {10.}, {1.}, {11.}};

        ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, new DoubleUnsupervisedModelProvider(provider));
        List<Cluster> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        assertEquals(2, kmeans.getClusters().get(0).getObservations().size());
        assertEquals(kmeans.getClusters().get(0).getObservations().get(0)[0], (double) data[0][0], 0);
	    assertEquals(kmeans.getClusters().get(0).getObservations().get(1)[0], (double) data[2][0], 0);
        assertEquals(1, kmeans.getWithinClusterSumOfSquares(), 0);
    }

    @Test
    public void testTwoWillJiggleWithOutlier() throws Exception {
	    Double[][] data = {{0.}, {10.}, {1.}, {11.}, {100.}};


        ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, new DoubleUnsupervisedModelProvider(provider));
        List<Cluster> clusters = kmeans.getClusters();
        assertEquals(2, clusters.size());
        assertEquals(4, kmeans.getClusters().get(0).getObservations().size());
        assertEquals(kmeans.getClusters().get(0).getObservations().get(0)[0], (double) data[0][0], 0);
	    assertEquals(kmeans.getClusters().get(0).getObservations().get(1)[0], (double) data[2][0], 0);
	    assertEquals(kmeans.getClusters().get(0).getObservations().get(2)[0], (double) data[1][0], 0);
	    assertEquals(kmeans.getClusters().get(0).getObservations().get(3)[0], (double) data[3][0], 0);


        assertEquals(1, kmeans.getClusters().get(1).getObservations().size());
	    assertEquals(kmeans.getClusters().get(1).getObservations().get(0)[0], (double) data[4][0], 0);
    }

    @Test
    public void clusterIndex() throws Exception {
	    Double[][] data = {{0.}, {10.}, {1.}, {11.}, {100.}};


        ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
        KMeans<Observation> kmeans = new KMeans<>(2, new DoubleUnsupervisedModelProvider(provider));
        assertEquals(0, kmeans.getClusterIndex(new double[]{2}));
        assertEquals(0, kmeans.getClusterIndex(new double[]{12}));
        assertEquals(0, kmeans.getClusterIndex(new double[]{20}));
        assertEquals(1, kmeans.getClusterIndex(new double[]{80}));

    }




}
