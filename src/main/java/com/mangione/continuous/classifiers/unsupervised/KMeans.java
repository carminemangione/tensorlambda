package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mangione.continuous.model.modelproviders.DoubleUnsupervisedModelProvider;
import com.mangione.continuous.observations.Observation;


public class KMeans<T extends Observation> {

    private final List<Cluster> clusters = new ArrayList<>();
    private final KMeansListener<T> listener;

    public KMeans(int numberOfClusters, DoubleUnsupervisedModelProvider provider, KMeansListener<T> listener) throws Exception {
        this.listener = listener;

        initializeClusters(numberOfClusters, provider);
        clusters.forEach(Cluster::updateCentroid);

        if (listener != null)
            listener.reassignmentCompleted(clusters);
        addThePointsToTheInitialClusters(provider);

        loopThroughJigglingOnCentroidsAndReCluster();
    }

    public KMeans(int numberOfClusters, DoubleUnsupervisedModelProvider provider) throws Exception {
        this(numberOfClusters, provider, null);
    }

    public double getWithinClusterSumOfSquares() {
        final double[] wcss = {0};
        clusters.forEach(cluster-> wcss[0] += cluster.withinClusterSumOfSquares());
        return wcss[0];
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public int getClusterIndex(double[] observation) {
        double distance = Double.MAX_VALUE;
        int index = 0;
        for (int j = 0; j < clusters.size(); j++) {
            if (clusters.get(j).distanceToCentroid(observation) < distance) {
                distance = clusters.get(j).distanceToCentroid(observation);
                index = j;
            }
        }
        return index;
    }

    private void loopThroughJigglingOnCentroidsAndReCluster() {
        boolean rejiggled = false;
        do {
            clusters.forEach(Cluster::updateCentroid);

            for (int i = 0; i < clusters.size(); i++) {
                rejiggled = processTheObservationsForThisCluster(rejiggled, i);
            }
            if (listener != null)
                listener.reassignmentCompleted(clusters);

        } while (rejiggled);

    }

    private boolean processTheObservationsForThisCluster(boolean rejiggled, int i) {
        final Cluster currentCluster = clusters.get(i);
        List<double[]> observationsToMove = currentCluster.getObservations();
        for (double[] observation : observationsToMove) {
            double currentDistance = currentCluster.distanceToCentroid(observation);
            Cluster closest = findCloserClusterIfExists(i, observation, currentDistance);
            rejiggled = moveObservationToCloserCluster(currentCluster, observation, closest);
            if (rejiggled && listener != null)
                listener.reassignmentCompleted(clusters);

        }
        return rejiggled;
    }

    private boolean moveObservationToCloserCluster(Cluster currentCluster, double[] observation, Cluster closest) {
        boolean rejiggled = false;
        if (closest != null) {
            currentCluster.remove(observation);
            closest.add(observation);
            rejiggled = true;
            if (listener != null)
                listener.reassignmentCompleted(clusters);
        }
        return rejiggled;
    }

    private Cluster findCloserClusterIfExists(int i, double[] observation, double currentDistance) {
        Cluster closest = null;
        for (int j = 0; j < clusters.size(); j++) {
            if (i != j) {
                if (clusters.get(j).distanceToCentroid(observation) < currentDistance) {
                    closest = clusters.get(j);
                }
            }
        }
        return closest;
    }

    private void addThePointsToTheInitialClusters(DoubleUnsupervisedModelProvider provider) throws Exception {
	    Iterator<double[]> iter = provider.iterator();
        while (iter.hasNext()) {
            double[] nextObservation = iter.next();

            Cluster closest = null;
            double distance = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                final double toCentroid = cluster.distanceToCentroid(nextObservation);
                if (toCentroid < distance) {
                    closest = cluster;
                    distance = toCentroid;
                }
            }
            //noinspection ConstantConditions
            closest.add(nextObservation);
        }
    }

    private void initializeClusters(int numberOfClusters, DoubleUnsupervisedModelProvider provider) throws Exception {
        Iterator<double[]> iterator = provider.iterator();
        for (int i = 0; i < numberOfClusters && iterator.hasNext(); i++) {
            final double[] next = iterator.next();
            final Cluster cluster = new Cluster(next.length);
            cluster.add(next);
            clusters.add(cluster);
        }
    }
}
