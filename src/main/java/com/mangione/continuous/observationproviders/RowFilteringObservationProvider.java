package com.mangione.continuous.observationproviders;

import java.util.Iterator;

import com.mangione.continuous.observations.ObservationInterface;

public class RowFilteringObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	ObservationProviderInterface<S, T> provider;


	public RowFilteringObservationProvider(ObservationProviderInterface<S, T> provider) {
		this.provider = provider;
	}

	@Override
	public Iterator<T> iterator() {
		return new RowFilteringObservationProviderIterator();
	}

	private class RowFilteringObservationProviderIterator implements Iterator<T> {
		Iterator<T> iterator;

		private RowFilteringObservationProviderIterator() {
			iterator = provider.iterator();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			T list = iterator.next();
			while (list.getFeatures().size() != 14 || ((String) (list.getFeatures().get(6))).equalsIgnoreCase("\"\"") || list.getFeatures().get(6).equals("\"object.group.name\"")){
				if(iterator.hasNext())
					list = iterator.next();
				else
					return null;
			}
			return list;
		}



	}
}
