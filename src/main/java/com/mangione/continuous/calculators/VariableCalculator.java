package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface VariableCalculator<S, T> extends Function<S, List<T>>, Serializable {
    @Override
    List<T> apply(S feature);
}
