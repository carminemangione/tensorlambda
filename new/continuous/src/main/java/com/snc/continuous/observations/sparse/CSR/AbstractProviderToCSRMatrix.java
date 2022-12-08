package com.mangione.continuous.observations.sparse.CSR;

import java.io.IOException;
import java.util.Arrays;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public abstract class AbstractProviderToCSRMatrix<FEATURE extends Number, OBSERVATION extends ObservationInterface<FEATURE>> {
	protected final ObservationProviderInterface<FEATURE, OBSERVATION> provider;
	private int rowIndex = 0;

	public AbstractProviderToCSRMatrix(ObservationProviderInterface<FEATURE, OBSERVATION> provider) {
		this.provider = provider;
	}

	public void process() throws IOException {
	    StreamSupport.stream(Spliterators.spliteratorUnknownSize(provider.iterator(), 0), false)
	            .forEach(this::processNextRow);
	    finish();
	}

	protected void processNextRow(OBSERVATION observation) {
	        int[] columnIndexes = observation.getColumnIndexes();
	        rowIndex += columnIndexes.length;
	        Double[] values = Arrays.stream(columnIndexes)
	                .boxed()
	                .map(observation::getFeature)
	                .mapToDouble(Number::doubleValue)
			        .boxed()
	                .toArray(Double[]::new);
	        processNextRow(columnIndexes, rowIndex, values);
	}

	protected abstract void processNextRow(int[] columnIndexes, int rowIndex, Double[] values);

	protected void finish() {

	}
}
