package com.mangione.continuous.datagenerators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;

import com.mangione.continuous.observations.dense.ContinuousExemplar;

@SuppressWarnings("WeakerAccess")
public class ScaledContinuousExemplarGenerator {


    private final ContinuousExemplarGenerator fContinuousExemplarGenerator;
    private final double[][] featureMeanAndSD;

    ScaledContinuousExemplarGenerator(int numberOfExemplars, double[][] featureMeanAndSD, double bias, double sdEpsilon, MersenneTwister twister) {
        this.featureMeanAndSD = featureMeanAndSD;
        fContinuousExemplarGenerator = new ContinuousExemplarGenerator(featureMeanAndSD.length, numberOfExemplars, bias, sdEpsilon, twister);

    }

    public List<ContinuousExemplar> generateDataSet() {
        final List<ContinuousExemplar> exemplars = fContinuousExemplarGenerator.getExemplars();
        final List<ContinuousExemplar> scaledExemplars = new ArrayList<>();
        exemplars.forEach(exemplar -> {

            final int[] scaledIndex = {0};
            final double[] scaled = Arrays
                    .stream(exemplar.getFeatures(Double[]::new))
                    .map(x -> x * featureMeanAndSD[scaledIndex[0]][1] + featureMeanAndSD[scaledIndex[0]++][0])
		            .mapToDouble(Double::doubleValue)
                    .toArray();

            scaledExemplars.add(new ContinuousExemplar(scaled, exemplar.getLabel()));
        });
        return scaledExemplars;
    }
}
