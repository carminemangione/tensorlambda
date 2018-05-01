package com.mangione.continuous.observationproviders;

import java.util.List;

public interface VariableCalculator<S, T> {
    List<T> calculateVariable(S feature);
}
