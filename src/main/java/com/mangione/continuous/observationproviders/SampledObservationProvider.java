package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.sampling.SamplingWithoutReplacement;
import org.apache.commons.math3.random.RandomGenerator;

public class SampledObservationProvider<S, T extends ObservationInterface<S>> extends ObservationProvider<S, T> {

    private final SamplingWithoutReplacement samplingWithoutReplacement;
    private final ObservationProvider<S, T> provider;
    private final boolean isTestSet;

    public SampledObservationProvider(double testSamplingPercent, ObservationProvider<S, T> provider,
            ObservationFactoryInterface<S, T> observationFactory,
            RandomGenerator generator, boolean isTestSet) throws Exception {
        super(observationFactory);
        this.provider = provider;
        this.isTestSet = isTestSet;
        long numberOfObservations = provider.getNumberOfLines();
        samplingWithoutReplacement = new SamplingWithoutReplacement(testSamplingPercent, numberOfObservations, generator);
    }

    @Override
    public boolean hasNext() {
        return provider.hasNext();
    }

    @Override
    public T next()  {
        while (provider.hasNext()) {
            if (samplingWithoutReplacement.select() == isTestSet)
                return provider.next();
            provider.next();
        }
        return null;
    }

    @Override
    public void reset() {
        provider.reset();
    }

    @Override
    public long getNumberOfLines() {
        return provider.getNumberOfLines();
    }
}
