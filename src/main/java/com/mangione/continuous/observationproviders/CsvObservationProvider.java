package com.mangione.continuous.observationproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationInterface;

public class CsvObservationProvider implements ObservationProviderInterface<String, ObservationInterface<String>> {

	private final File file;

	public CsvObservationProvider(File file) throws FileNotFoundException {
		this.file = file;

	}

	@Override
	public long getNumberOfLines() {
		final long[] numberOfLines = {0};

		forEach(stringObservationInterface -> numberOfLines[0]++);
		return numberOfLines[0];
	}

	@Override
	public ObservationInterface<String> create(String[] data) {
		return new Observation<>(data);
	}

	@Override
	@Nonnull
	public Iterator<ObservationInterface<String>> iterator() {
		return new CsvObservationIterator();
	}

	@Override
	public void forEach(Consumer<? super ObservationInterface<String>> action) {
		for (ObservationInterface<String> stringObservationInterface : this) {
			action.accept(stringObservationInterface);
		}
	}

	@Override
	public Spliterator<ObservationInterface<String>> spliterator() {
		throw new UnsupportedOperationException("Spliterator is not supported...");
	}

	private class CsvObservationIterator implements Iterator<ObservationInterface<String>> {
		BufferedReader bufferedReader;

		private CsvObservationIterator() {
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				throw new ProviderException(e);
			}
		}

		@Override
		public boolean hasNext() {
			boolean ready = false;
			try {
				if (bufferedReader != null) {
					ready = bufferedReader.ready();
				}
			} catch (IOException e) {
				throw new ProviderException(e);
			}
			return ready;
		}

		@Override
		public ObservationInterface<String> next() {
			String[] nextLine;
			try {
				nextLine = bufferedReader.readLine().split(",");
			} catch (IOException e) {
				throw new ProviderException(e);
			}

			return create(nextLine);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove is not supported...");
		}

		@Override
		public void forEachRemaining(Consumer<? super ObservationInterface<String>> action) {
			while(hasNext())
				action.accept(next());
		}
	}
}

