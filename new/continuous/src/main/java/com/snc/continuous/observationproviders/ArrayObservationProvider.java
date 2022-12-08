package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class ArrayObservationProvider<FEATURE, OBSERVATION extends ObservationInterface<FEATURE>>
        extends ListObservationProvider<FEATURE, OBSERVATION> {
    public ArrayObservationProvider(FEATURE[][] arrayOfObservations, Function<FEATURE[], OBSERVATION> observationFromArrayFactory) {
        super(Arrays.stream(arrayOfObservations)
                .map(observationFromArrayFactory)
                .collect(Collectors.toList()));
    }

    public static Double[][] doubleFromPrimitive(double[][] doubles) {
        Double[][] objects = new Double[doubles.length][];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = DoubleStream.of(doubles[i]).boxed().toArray(Double[]::new);
        }
        return objects;
    }

    @SuppressWarnings("WeakerAccess")
    public static Integer[][] integerFromPrimitive(int[][] ints) {
        Integer[][] objects = new Integer[ints.length][];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = IntStream.of(ints[i]).boxed().toArray(Integer[]::new);
        }
        return objects;
    }
}

