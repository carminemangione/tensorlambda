package com.mangione.continuous.calculators.scaling;

import java.io.Serializable;

public interface VariableScalingInterface<S extends Number, T extends Number> extends Serializable {
    T apply(S feature);
}
