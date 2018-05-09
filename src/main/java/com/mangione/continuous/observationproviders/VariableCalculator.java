package com.mangione.continuous.observationproviders;

import java.util.List;
import java.util.function.Function;

public interface VariableCalculator<S, T> extends Function<S, List<T>> {
    @Override
    List<T> apply(S feature);
}
