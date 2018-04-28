package com.mangione.continuous.observationproviders;

import java.util.Collections;
import java.util.List;

public class DoubleVariableCalculator implements VariableCalculator<Double> {
    @Override
    public List<Double> calculateVariable(String feature) {
        return Collections.singletonList(Double.parseDouble(feature));
    }
}
