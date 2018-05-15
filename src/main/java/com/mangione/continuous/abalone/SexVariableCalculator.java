package com.mangione.continuous.abalone;

import java.util.Arrays;
import java.util.List;

import com.mangione.continuous.observationproviders.VariableCalculator;

public class SexVariableCalculator implements VariableCalculator<String, Double> {
    @Override
    public List<Double> apply(String feature) {
        Double[] out = new Double[]{0., 0., 0.};
        if ("M".equals(feature))
            out[0] = 1.0;
        else if ("F".equals(feature))
            out[1] = 1.0;
        else
            out[3] = 1.0;
        return Arrays.asList(out);
    }
}
