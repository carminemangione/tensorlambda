package com.mangione.continuous.observationproviders.variable;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observationproviders.VariableCalculator;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariableCalculationProvider extends ObservationProvider<Observation> {
    private final ObservationProvider<Observation> provider;
    private final Map<Integer, VariableCalculator> indexToCalculator;

    public VariableCalculationProvider(ObservationProvider<Observation> provider,
            Map<Integer, VariableCalculator> indexToCalculator) {
        super(new ObservationFactory());
        this.provider = provider;
        this.indexToCalculator = indexToCalculator;
    }


    @Override
    public boolean hasNext() throws Exception {
        return provider.hasNext();
    }

    @Override
    public Observation next() throws Exception {
        return null;
    }

    @Override
    public void reset() throws Exception {
        provider.reset();
    }

    @Override
    public long getNumberOfLines() throws IOException {
        return provider.getNumberOfLines();
    }



}
