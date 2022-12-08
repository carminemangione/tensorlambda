package com.mangione.continuous.observationproviders.csv;

import java.io.IOException;

import com.mangione.continuous.observationproviders.ProviderException;

class CsvObservationSpliteratorShared {
	void closeResources(CsvObservationIterator iterator) {
		try {
			iterator.closeReader();
		} catch (IOException e) {
			throw new ProviderException(e);
		}
	}
}
