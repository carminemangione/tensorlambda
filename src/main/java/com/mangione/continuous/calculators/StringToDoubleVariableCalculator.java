package com.mangione.continuous.calculators;

import java.util.Collections;
import java.util.List;

import com.mangione.continuous.calculators.VariableCalculator;

public class StringToDoubleVariableCalculator implements VariableCalculator<String, Double> {
    @Override
    public List<Double> apply(String feature, List<String> features) {
        return Collections.singletonList(Double.parseDouble(feature));
    }
}
