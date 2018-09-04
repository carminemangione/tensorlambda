package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;

public class RowFilteringObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final Map<String, ArrayList<String>> mapOfBranchToRepo;
	private final HashMap<String, ArrayList<List<String>>> mapOfCases;
	ObservationProviderInterface<S, T> provider;


	public RowFilteringObservationProvider(ObservationProviderInterface<S, T> provider, Map<String, ArrayList<String>> mapOfBranchToRepo) {
		this.provider = provider;
		this.mapOfBranchToRepo = mapOfBranchToRepo;
		mapOfCases = null;
	}

	public RowFilteringObservationProvider(ObservationProviderInterface<S, T> provider, Map<String, ArrayList<String>> mapOfBranchToRepo, HashMap<String, ArrayList<List<String>>> mapOfCases) {
		this.provider = provider;
		this.mapOfBranchToRepo = mapOfBranchToRepo;
		this.mapOfCases = mapOfCases;

	}

	@Nonnull
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
			while (validRow(list)){
				if(iterator.hasNext())
					list = iterator.next();
				else
					return null;
			}
			return list;
		}

		private boolean validRow(T list) {
			if(list.getFeatures().size() != 14)
				return true;
			if(((String) (list.getFeatures().get(6))).equalsIgnoreCase("\"\""))
				return true;

			if(list.getFeatures().get(6).equals("\"object.group.name\""))
				return true;
			if(((String) list.getFeatures().get(7)).substring(1,8).compareTo("2016-08") < 0)
				return true;

			String trackName = ((String) list.getFeatures().get(6)).substring(1,((String) list.getFeatures().get(6)).length() - 1).replace('-', '/');


			if(!mapOfBranchToRepo.containsKey(trackName))
				return true;

			if(mapOfCases != null && (mapOfCases.get(list.getFeatures().get(6)).size() <= 15000 || mapOfCases.get(list.getFeatures().get(6)).size() > 20000))
				return true;

			return false;
		}


	}
}
