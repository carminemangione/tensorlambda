package com.mangione.continuous.observationproviders;

import java.util.List;

public interface VariableCalculator<S> {
    List<S> calculateVariable(String feature);
}
