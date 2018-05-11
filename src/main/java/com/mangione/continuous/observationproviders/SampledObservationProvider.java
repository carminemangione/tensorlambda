package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.sampling.SamplingWithoutReplacement;


public class SampledObservationProvider<S, T extends ObservationInterface<S>> extends ObservationProvider<S, T> {


	private final double testSamplingPercent;
	private final ObservationProviderInterface<S, T> provider;
	private final RandomGeneratorFactory generatorFactory;
	private final long seed;
	private final boolean isTestSet;
	private final long numberOfObservations;


	@SuppressWarnings("WeakerAccess")
	public SampledObservationProvider(double testSamplingPercent, ObservationProviderInterface<S, T> provider,
			ObservationFactoryInterface<S, T> observationFactory,
			RandomGeneratorFactory generatorFactory, long seed, boolean isTestSet) throws Exception {
		super(observationFactory);
		this.testSamplingPercent = testSamplingPercent;
		this.provider = provider;
		this.generatorFactory = generatorFactory;
		this.seed = seed;
		this.isTestSet = isTestSet;
		numberOfObservations = provider.getNumberOfLines();

	}

	@Override
	public long getNumberOfLines() {
		return provider.getNumberOfLines();
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return new SampledObservationProviderIterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		Iterator<T> iterator = iterator();
		iterator.forEachRemaining(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		throw new UnsupportedOperationException("Spliterator not supported.");
	}

	private class SampledObservationProviderIterator implements Iterator<T> {
		private final Iterator<T> iterator = provider.iterator();
		private final SamplingWithoutReplacement samplingWithoutReplacement;
		private boolean isCurrentSampleTest;
		private T nextObservation;

		private SampledObservationProviderIterator() {
			samplingWithoutReplacement =
					new SamplingWithoutReplacement(testSamplingPercent, numberOfObservations,
							generatorFactory.generate(seed));
			if (iterator.hasNext())
				getNextAndUpdateCurrentSampleTest();
		}

		@Override
		public boolean hasNext() {
			while (iterator.hasNext() && isCurrentSampleTest != isTestSet) {
				getNextAndUpdateCurrentSampleTest();
			}
			return iterator.hasNext();
		}

		@Override
		public T next() {
			while (iterator.hasNext() && isCurrentSampleTest != isTestSet) {
				getNextAndUpdateCurrentSampleTest();
			}

			T next = null;
			if (isCurrentSampleTest == isTestSet) {
				next = nextObservation;
				if (iterator.hasNext())
					getNextAndUpdateCurrentSampleTest();

			}
			return next;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove not supported.");

		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			while (hasNext()) {
				action.accept(next());
			}
		}

		private void getNextAndUpdateCurrentSampleTest() {
			isCurrentSampleTest = samplingWithoutReplacement.select();
			nextObservation = iterator.next();
		}
	}
}
