package com.mangione.continuous.abalone;

import com.mangione.continuous.observationproviders.VariableCalculator;

public class SexVariableCalculator implements VariableCalculator {
    @Override
    public double[] calculateVariable(String feature) {
        double[] out = new double[1];
//        switch (feature) {
//            case "M":
//                out[0] = 1;
//                break;
//        }
//        return out;
        return new double[0];
    }
}
