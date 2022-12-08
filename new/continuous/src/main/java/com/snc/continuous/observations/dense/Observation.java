package com.mangione.continuous.observations.dense;

import java.util.Arrays;
import java.util.function.IntFunction;

import com.mangione.continuous.observations.ObservationInterface;

public class Observation<FEATURE> implements ObservationInterface<FEATURE> {
    private final FEATURE[] features;

    public Observation(FEATURE[] features) {
        this.features = features;
    }

    public FEATURE[] getFeatures(IntFunction<FEATURE[]> featureBuilder) {
        return features;
    }

    @Override
    public FEATURE getFeature(Integer index) {
        return features[index];
    }

    @Override
    public int numberOfFeatures() {
        return features.length;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "features=" + Arrays.toString(features) +
                '}';
    }
}
