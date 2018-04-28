package com.mangione.continuous.classifiers;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.zscale.ZScaleObservationProvider;

public class NNearestNeighbor<T extends ExemplarInterface<Double, Integer>> {
    private final ZScaleObservationProvider<T> cachedMemoryProvider;
    private final int n;

    public NNearestNeighbor(ObservationProvider<Double, T> provider, ObservationFactoryInterface<Double, T> factory, int n) throws Exception {
        this.n = n;
        cachedMemoryProvider = new ZScaleObservationProvider<>(
                new ArrayObservationProvider<>(provider, factory),
                factory);
    }

    public int classify(T observation) {
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        SortedArray<DistanceAndLabel> lowestDistances = new SortedArray<>(n);
        while (cachedMemoryProvider.hasNext()) {
            T currentTrainObs = cachedMemoryProvider.next();
            final T scaled = cachedMemoryProvider.scale(observation);
            final double distance = euclideanDistance.compute(toPrimitive(scaled.getFeatures()),
                    toPrimitive(currentTrainObs.getFeatures()));

            DistanceAndLabel distanceAndLabel = new DistanceAndLabel(distance, currentTrainObs.getTarget());
            lowestDistances.add(distanceAndLabel);
        }

        cachedMemoryProvider.reset();
        Map<Integer, Integer> counts = new HashMap<>();
        lowestDistances.get().forEach(distance -> {
            counts.putIfAbsent(distance.getLabel(), 0);
            counts.put(distance.getLabel(), counts.get(distance.getLabel()) + 1);
        });
        final int[] currentMaxCount = {0};
        final int[] currentLabelOfMax = {0};
        counts.forEach((key, value) -> {
            if (value > currentMaxCount[0]) {
                currentMaxCount[0] = value;
                currentLabelOfMax[0] = key;
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

        int getLabel() {
            return label;
        }

        @Override
        public int compareTo(@Nonnull DistanceAndLabel o) {
            return Double.compare(distance, o.distance);
        }
    }
}
