package com.mangione.continuous.calculators;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;


public class StringToDoubleVariableCalculator implements Function<String, List<Double>> {
    @Override
    public List<Double> apply(String feature) {
        return Collections.singletonList(Double.parseDouble(feature));
    }
}
