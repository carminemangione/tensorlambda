package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class CsvObservationProvider implements ObservationProviderInterface<String, ObservationInterface<String>> {

	private final File file;
	private final ObservationFactoryInterface<String, ObservationInterface<String>> factory;
	private final ProxyValues namedColumns = new ProxyValues();

	public CsvObservationProvider(File file, ObservationFactoryInterface<String, ObservationInterface<String>> factory, boolean hasColumnHeader) throws FileNotFoundException {
		this.file = file;
		this.factory = factory;
		if(hasColumnHeader)
			fillNamedColumns();
	}

	private void fillNamedColumns() {
		CsvObservationIterator iterator = new CsvObservationIterator();
		ObservationInterface<String> next = iterator.next();
		IntStream.range(0, next.getFeatures().size()).forEach(i -> namedColumns.addPair(i, next.getFeatures().get(i)));
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

	public ProxyValues getNamedColumns() {
		return namedColumns;
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
				nextLine = bufferedReader.readLine().split(" ");
			} catch (IOException e) {
				throw new ProviderException(e);
			}

			return factory.create(Arrays.asList(nextLine));
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

