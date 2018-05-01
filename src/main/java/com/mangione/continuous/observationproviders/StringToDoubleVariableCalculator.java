package com.mangione.continuous.observationproviders;

import java.util.Collections;
import java.util.List;

public class StringToDoubleVariableCalculator implements VariableCalculator<String, Double> {
    @Override
    public List<Double> apply(String feature) {
        return Collections.singletonList(Double.parseDouble(feature));
    }
}
