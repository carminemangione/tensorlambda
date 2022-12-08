package com.mangione.continuous.classifiers.unsupervised.nearestneighbor;//package com.mangione.continuous.classifiers.unsupervised;
//
//import static java.util.Arrays.stream;
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.List;
//import java.util.Random;
//
//import org.junit.Ignore;
//import org.junit.Test;
//
//import com.mangione.continuous.model.modelproviders.DoubleUnsupervisedModelProvider;
//import com.mangione.continuous.observationproviders.ArrayObservationProvider;
//import com.mangione.continuous.observations.DoubleObservationFactory;
//import com.mangione.continuous.observations.Observation;
//import com.mangione.continuous.observations.ObservationInterface;
//
//public class KModesTest {
//
//	//@Test
//	@Ignore
//	public void testMultiThreading() throws Exception {
//		long initialTime = System.currentTimeMillis();
//		int n = 1000;
//		Double[][] data = new Double[n][n];
//		Random rand = new Random();
//
//		for (int i = 0; i < n; i++) {
//			for(int j = 0; j < n; j++) {
//				data[i][j] = (double) rand.nextInt(2);
//			}
//		}
//		int numThreads = 4;
//		double minError = 1000000000;
//		int minK = 0;
//		for (int numClusters = 2; numClusters < 50; numClusters++) {
//			initialTime = System.currentTimeMillis();
//
//			ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//			KClustering<double[], Observation> kmeans = new KClustering<>(numClusters, new DoubleUnsupervisedModelProvider(provider), numThreads, new KModes());
//			List<Cluster<double[]>> clusters = kmeans.getClusters();
//			assertEquals(numClusters, clusters.size());
//
//			//System.out.println("Checking ");
//			checkCorrectAnswer(clusters);
//
//			double totalError = 0;
//			for(Cluster clus : clusters) {
//				totalError += calculateError(clus);
//			}
//			System.out.println(numClusters + " " + totalError);
//			if(totalError < minError) {
//				minK = numClusters;
//				minError = totalError;
//			}
//
//			//System.out.println("Time: " + (System.currentTimeMillis() - initialTime) + " Number Of Threads: " + numThreads + " Number of Clusters: " + numClusters);
//			//clusters.forEach(x -> System.out.println(x.getCentroid()[0]));
//		}
//		System.out.println("MIN K: " + minK + ", MIN ERROR: " + minError);
//	}
//
//	//@Test
//	public void testFewKmodesPoints() throws Exception {
//		Double[][] data = {{1., 1., 1., 1.}, {0., 0., 0., 0.}, {1., 0., 0., 0.}, {0., 1., 1., 1.}};
//		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//		KClustering<double[], Observation> kmodes = new KClustering<>(2, new DoubleUnsupervisedModelProvider(provider), 1, new KModes());
//		List<Cluster<double[]>> clusters = kmodes.getClusters();
//		assertEquals(2, clusters.size());
//
//		checkCorrectAnswer(clusters);
//
//		double totalError = 0;
//		for(Cluster clus : clusters) {
//			totalError += calculateError(clus);
//		}
//		System.out.println(2 + " " + totalError);
//
//
//	}
//
//	private void checkCorrectAnswer(List<Cluster<double[]>> clusters) {
//		for (int i = 0; i < clusters.size(); i++) {
//			for (double[] elem : clusters.get(i).getObservations()) {
//				double dist = clusters.get(i).distanceToCentroid(elem);
//				for (int j = 0; j < clusters.size(); j++) {
//					assertTrue(new KModes().distanceToCentroid(clusters.get(j), elem) >= dist);
//				}
//			}
//		}
//	}
//
//	private double calculateError(Cluster<double[]> clus) {
//		if(clus.getObservations().size() == 0)
//			return 0.0;
//
//		double error = 0;
//
//		for(int i = 0; i < clus.getObservations().size(); i++) {
//			error += (new KModes().distanceToCentroid(clus, clus.getObservations().get(i)));
//		}
//		return error;
//	}
//
//	//@Test
//	public void testTwoClustersTwoObservations() throws Exception {
//		Double[][] data = {{0., 1.}, {1., 1.}};
//		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//		KClustering<double[],Observation> kmeans = new KClustering<>(2, new DoubleUnsupervisedModelProvider(provider), new KModes());
//		List<Cluster<double[]>> clusters = kmeans.getClusters();
//		assertEquals(2, clusters.size());
//		for (int i = 0; i < data.length; i++) {
//			assertArrayEquals(convertDoubleArrayUsingStream(data)[i], clusters.get(i).getObservations().get(0), 0);
//		}
//	}
//
//	public double[][] convertDoubleArrayUsingStream(Double[][] data) {
//		return stream(data)
//				.map(row -> stream(row)
//						.mapToDouble(Number::doubleValue).toArray())
//				.toArray(double[][]::new);
//	}
//
//	//@Test
//	public void testTwoClustersFourObservations() throws Exception {
//		Double[][] data = {{0.}, {1.}, {1.}, {0.}};
//
//		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//		KClustering<double[],Observation> kmeans = new KClustering<>(2, new DoubleUnsupervisedModelProvider(provider), new KModes());
//		List<Cluster<double[]>> clusters = kmeans.getClusters();
//		assertEquals(2, clusters.size());
//		assertEquals(2, kmeans.getClusters().get(0).getObservations().size());
//		assertEquals(kmeans.getClusters().get(0).getObservations().get(0)[0], (double) data[0][0], 0);
//		assertEquals(kmeans.getClusters().get(0).getObservations().get(1)[0], (double) data[3][0], 0);
//		//assertEquals(0, kmeans.getWithinClusterSumOfSquares(), 0);
//	}
//
//	//@Test
//	@Ignore
//	public void testTwoWillJiggleWithOutlier() throws Exception {
//		Double[][] data = {{0.}, {0.}, {1.}, {1.}, {1.}};
//
//
//		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//		KClustering<double[], Observation> kmeans = new KClustering<>(2, new DoubleUnsupervisedModelProvider(provider), new KModes());
//		List<Cluster<double[]>> clusters = kmeans.getClusters();
//		assertEquals(2, clusters.size());
//		assertEquals(4, kmeans.getClusters().get(0).getObservations().size());
//		assertEquals(kmeans.getClusters().get(0).getObservations().get(0)[0], (double) data[2][0], 0);
//		assertEquals(kmeans.getClusters().get(0).getObservations().get(1)[0], (double) data[3][0], 0);
//		assertEquals(kmeans.getClusters().get(0).getObservations().get(2)[0], (double) data[4][0], 0);
//
//
//		assertEquals(2, kmeans.getClusters().get(1).getObservations().size());
//		assertEquals(kmeans.getClusters().get(1).getObservations().get(0)[0], (double) data[0][0], 0);
//		assertEquals(kmeans.getClusters().get(1).getObservations().get(0)[0], (double) data[1][0], 0);
//	}
//
//	//@Test
//	public void clusterIndex() throws Exception {
//		Double[][] data = {{0.}, {0.}, {1.}, {1.}, {1.}};
//
//
//		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());
//		KClustering<double[], Observation> kmeans = new KClustering<>(2, new DoubleUnsupervisedModelProvider(provider), new KModes());
//		assertEquals(1, kmeans.getClusterIndex(new double[]{0}));
//		assertEquals(1, kmeans.getClusterIndex(new double[]{0}));
//		assertEquals(0, kmeans.getClusterIndex(new double[]{1}));
//		assertEquals(1, kmeans.getClusterIndex(new double[]{0}));
//
//	}
//}
