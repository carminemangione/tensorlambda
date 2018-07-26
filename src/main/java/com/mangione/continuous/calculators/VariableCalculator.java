package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.List;

public interface VariableCalculator<S, T> extends Serializable {

    List<T> apply(S feature, List<S> features);
}
