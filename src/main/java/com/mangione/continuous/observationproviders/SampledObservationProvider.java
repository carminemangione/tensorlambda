package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import com.mangione.continuous.sampling.SamplingWithoutReplacement;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.IOException;

public class SampledObservationProvider extends ObservationProvider<Observation> {

    private final SamplingWithoutReplacement samplingWithoutReplacement;
    private final ObservationProvider<Observation> provider;
    private final boolean isTestSet;

    public SampledObservationProvider(double testSamplingPercent, ObservationProvider<Observation> provider,
            RandomGenerator generator, boolean isTestSet) throws Exception {
        super(new ObservationFactory());
        this.provider = provider;
        this.isTestSet = isTestSet;
        long numberOfObservations = provider.getNumberOfLines();
        samplingWithoutReplacement = new SamplingWithoutReplacement(testSamplingPercent, numberOfObservations, generator);
    }

    @Override
    public boolean hasNext() throws Exception {
        return provider.hasNext();
    }

    @Override
    public Observation next() throws Exception {
        while (provider.hasNext()) {
            if (samplingWithoutReplacement.select() == isTestSet)
                return provider.next();
            provider.next();
        }
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
