package com.mangione.continuous.observations;

import java.util.List;

public interface ObservationFactoryInterface<S, T extends ObservationInterface<S>> {
    T create(List<S> data);
}
