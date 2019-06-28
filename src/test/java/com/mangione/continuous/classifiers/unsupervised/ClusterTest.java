package com.mangione.continuous.classifiers.unsupervised;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.junit.Test;

import com.mangione.continuous.observations.dense.Observation;


public class ClusterTest {
	@Test
	public void testMeanUpdateOnAddFirst() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(1, new KMeansDistanceMeasure<>());
		assertNull(cluster.getCentroid());
		cluster.add(createObservation(new double[]{1}));
		cluster.updateCentroid();
		assertEquals(1, cluster.getCentroid()[0], 0);
	}

	@Test
	public void testMeanUpdateOnUpdate() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(1, new KMeansDistanceMeasure<>());
		cluster.add(createObservation(new double[]{1}));
		cluster.updateCentroid();
		assertEquals(1, cluster.getCentroid()[0], 0);
		cluster.add(createObservation(new double[]{5}));
		assertEquals(1, cluster.getCentroid()[0], 0);
		cluster.updateCentroid();
		assertEquals(3, cluster.getCentroid()[0], 0);

	}

	@Test
	public void testMeanUpdateMultiDimensions() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(2, new KMeansDistanceMeasure<>());
		cluster.add(createObservation(new double[]{1, 2}));
		cluster.updateCentroid();
		assertEquals(1, cluster.getCentroid()[0], 0);
		assertEquals(2, cluster.getCentroid()[1], 0);
		cluster.add(createObservation(new double[]{5, 2}));
		assertEquals(1, cluster.getCentroid()[0], 0);
		assertEquals(2, cluster.getCentroid()[1], 0);
		cluster.updateCentroid();
		assertEquals(3, cluster.getCentroid()[0], 0);
		assertEquals(2, cluster.getCentroid()[1], 0);

	}

	@Test
	public void distanceToCentroid() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(2, new KMeansDistanceMeasure<>());
		cluster.add(createObservation(new double[]{1, 2}));
		cluster.add(createObservation(new double[]{5, 2}));
		cluster.updateCentroid();
		double[] centroid = cluster.getCentroid();
		EuclideanDistance ed = new EuclideanDistance();
		final double[] test = {10, 12};
		assertEquals(ed.compute(centroid, test), cluster.distanceToCentroid(createObservation(test)), 0.00000001);
	}


	@Test
	public void removeObservation() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(2, new KMeansDistanceMeasure<>());
		final Observation<Double> observation1 = createObservation(new double[]{1, 2});
		cluster.add(observation1);
		final double[] observation = new double[]{5, 2};
		cluster.add(createObservation(observation));
		cluster.remove(observation1);
		cluster.updateCentroid();
		assertEquals(5, cluster.getCentroid()[0], 0);
		assertEquals(2, cluster.getCentroid()[1], 0);

		Set<Observation<Double>> observations = cluster.getObservations();
		assertEquals(1, observations.size());
		List<Double> features = observations.iterator().next().getFeatures();
		assertEquals(observation[0], features.get(0), 0);
		assertEquals(observation[1], features.get(1), 0);
	}

	@Test
	public void removeLast() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(2, new KMeansDistanceMeasure<>());
		final Observation<Double> observation1 = createObservation(new double[]{1, 2});
		cluster.add(observation1);
		final Observation<Double> observation = createObservation(new double[]{5, 2});
		cluster.add(observation);
		cluster.remove(observation1);
		cluster.remove(observation);

		Set<Observation<Double>> observations = cluster.getObservations();
		assertEquals(0, observations.size());
		assertNull(cluster.getCentroid());
	}

	@Test
	public void removeWorksOffOfIdentity() {
		Cluster<Double, Observation<Double>> cluster = new Cluster<>(2, new KMeansDistanceMeasure<>());
		final Observation<Double> observation1 = createObservation(new double[]{1, 2});
		cluster.add(observation1);
		final Observation<Double> observation = createObservation(new double[]{5, 2});
		cluster.add(observation);
		cluster.remove(createObservation(new double[]{1, 2}));

		Set<Observation<Double>> observations = cluster.getObservations();
		assertEquals(2, observations.size());
	}

	private Observation<Double> createObservation(double[] data) {
		return new Observation<>(Arrays.stream(data).boxed().collect(Collectors.toList()));
	}
}