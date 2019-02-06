package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observations.ObservationInterface;

import java.util.List;
import java.util.stream.Collectors;

class CSRRow<S extends ObservationInterface<? extends Number>> {
    private final List<Double> values;
    private final List<Integer> indexes;


    CSRRow(S observation) {
        indexes = observation.getColumnIndexes();
        values = observation.getColumnIndexes().stream()
                .mapToDouble(index -> observation.getFeature(index).doubleValue())
                .boxed()
                .collect(Collectors.toList());
    }

    List<Double> getValues() {
        return values;
    }

    List<Integer> getIndexes() {
        return indexes;
    }

    int getNumValues() {
        return values.size();
    }
}
