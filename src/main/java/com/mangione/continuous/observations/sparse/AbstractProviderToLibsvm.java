package com.mangione.continuous.observations.sparse;

import java.io.IOException;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

public abstract class AbstractProviderToLibsvm<EXEMPLAR extends ExemplarInterface<Integer, Integer>> {

	protected void processProvider(ObservationProviderInterface<Integer, EXEMPLAR> provider, Function<EXEMPLAR, Integer> tagFunction) throws Exception {
		LibsvmEncoder<EXEMPLAR> encoder = new LibsvmEncoder<>();
		for (EXEMPLAR exemplar : provider) {
			processNextLine(encoder.encode(exemplar, tagFunction, this::afterTag));
		}
	}

	protected abstract String afterTag(EXEMPLAR exemplar);
	protected abstract void processNextLine(String nextLine) throws IOException;
}
