package com.mangione.continuous.classifiers;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import com.mangione.continuous.zscale.ZScaleObservationProvider;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.HashMap;
import java.util.Map;

public class NNearestNeighbor {
    private final ZScaleObservationProvider<DiscreteExemplar> cachedMemoryProvider;
    private final int n;

    public NNearestNeighbor(ObservationProvider provider, int n) throws Exception {
        this.n = n;
        cachedMemoryProvider = new ZScaleObservationProvider<>(
                new ArrayObservationProvider<>(provider, new ObservationFactory()), new DiscreteExemplarFactory(), true);
    }

    public int classify(Observation observation) throws Exception {
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        SortedArray<DistanceAndLabel> lowestDistances = new SortedArray<>(n);
        while (cachedMemoryProvider.hasNext()) {
            DiscreteExemplar currentTrainObs = cachedMemoryProvider.next();
            final DiscreteExemplar scaled = cachedMemoryProvider.scale(observation);
            final double distance = euclideanDistance.compute(scaled.getFeatures(), currentTrainObs.getFeatures());
            DistanceAndLabel distanceAndLabel = new DistanceAndLabel(distance, currentTrainObs.getTarget());
            lowestDistances.add(distanceAndLabel);
        }

        cachedMemoryProvider.reset();
        Map<Integer, Integer> counts = new HashMap<>();
        lowestDistances.get().forEach(distance -> {
            if (counts.get(distance.getLabel()) == null) {
                counts.put(distance.getLabel(), 0);
            }
            counts.put(distance.getLabel(), counts.get(distance.getLabel()) + 1);
        });
        final int[] currentMaxCount = {0};
        final int[] currentLabelOfMax = {0};
        counts.entrySet().forEach(entry -> {
            if (entry.getValue() > currentMaxCount[0]) {
                currentMaxCount[0] = entry.getValue();
                currentLabelOfMax[0] = entry.getKey();
            }
        });
        return currentLabelOfMax[0];
    }

    private class DistanceAndLabel implements Comparable<DistanceAndLabel> {
        private final double distance;
        private final int label;

        private DistanceAndLabel(double distance, int label) {
            this.distance = distance;
            this.label = label;
        }

        public int getLabel() {
            return label;
        }

        @Override
        public int compareTo(DistanceAndLabel o) {
            return distance > o.distance ? 1 : distance < o.distance ? -1 : 0;
        }
    }
}
