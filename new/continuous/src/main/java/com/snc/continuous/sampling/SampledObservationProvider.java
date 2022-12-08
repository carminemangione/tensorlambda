package com.mangione.continuous.sampling;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.sampling.reservoir.SamplerFactoryInterface;
import com.mangione.continuous.sampling.reservoir.SamplerInterface;


public class SampledObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {
	private final ListObservationProvider<S, T> sampledObservations;

	public SampledObservationProvider(ObservationProviderInterface<S, T> providerToSample, int sampleSize, int seed, SamplerFactoryInterface<T> factory) {
		SamplerInterface<T> sampler = factory.createSampler(providerToSample.iterator(), sampleSize, seed);
		sampledObservations = new ListObservationProvider<>(sampler.sample());
	}

	public SampledObservationProvider(ObservationProviderInterface<S, T> providerToSample, int sampleSize, int seed, SamplerFactoryInterface<T> factory, Comparator<T> sortFunction) {
		SamplerInterface<T> sampler = factory.createSampler(providerToSample.iterator(), sampleSize, seed);
		List<T> sample = sampler.sample();
		sample.sort(sortFunction);
		sampledObservations = new ListObservationProvider<>(sample);
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return sampledObservations.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		Iterator<T> iterator = iterator();
		iterator.forEachRemaining(action);
	}
}
