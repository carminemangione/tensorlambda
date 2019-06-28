package com.mangione.continuous.calculators.scaling;

import java.io.Serializable;
import java.util.List;

public interface VariableScalingInterface<S extends Number, T extends Number> extends Serializable {
    T apply(S feature);
}
