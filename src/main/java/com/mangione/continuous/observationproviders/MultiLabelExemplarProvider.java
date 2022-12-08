package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ExemplarInterface;

public class MultiLabelExemplarProvider<FEATURE extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, Integer>,
		MULTICLASSEXEMPLAR extends ExemplarInterface<FEATURE, Integer[]>>
		implements ObservationProviderInterface<FEATURE, MULTICLASSEXEMPLAR> {

	private final ObservationProviderInterface<FEATURE, EXEMPLAR> provider;
	private final BiFunction<EXEMPLAR, List<Integer>, MULTICLASSEXEMPLAR> exemplarFactory;
	private final BiPredicate<EXEMPLAR, EXEMPLAR> sameGroupPredicate;

	public MultiLabelExemplarProvider(ObservationProviderInterface<FEATURE, EXEMPLAR> provider,
			BiPredicate<EXEMPLAR, EXEMPLAR> sameGroupPredicate, BiFunction<EXEMPLAR, List<Integer>,
			MULTICLASSEXEMPLAR> exemplarFactory) {
		this.sameGroupPredicate = sameGroupPredicate;
		if (!provider.iterator().hasNext())
			throw new IllegalStateException("Empty provider not allowed");
		this.provider = provider;
		this.exemplarFactory = exemplarFactory;
	}

	@Nonnull
	@Override
	public Iterator<MULTICLASSEXEMPLAR> iterator() {
		return new MultiLabelIterator();
	}

	@Override
	public int size() {
		Set<MULTICLASSEXEMPLAR> collect = getStream().collect(Collectors.toSet());
		return collect.size();
	}

	private class MultiLabelIterator implements Iterator<MULTICLASSEXEMPLAR> {
		private EXEMPLAR last;
		private final Iterator<EXEMPLAR> iterator;
		private boolean finished;
		private boolean mustProcessLast;

		private MultiLabelIterator() {
			iterator = provider.iterator();
			if (!iterator.hasNext())
				throw new IllegalStateException("Can't create with empty iterator.");
			last = iterator.next();
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext() || !finished || mustProcessLast;
		}

		@Override
		public MULTICLASSEXEMPLAR next() {
			if (finished  && !mustProcessLast)
				throw new IllegalStateException("No more entries. Did you check 'hasNext'?");
			List<Integer> labels = new ArrayList<>(last.getLabel());
			labels.add(last.getLabel());
			EXEMPLAR prototype = last;
			if (!mustProcessLast) {
				while (iterator.hasNext()) {
					EXEMPLAR next = iterator.next();
					if (!sameGroupPredicate.test(last, next)) {
						last = next;
						mustProcessLast = true;
						break;
					} else {
						labels.add(next.getLabel());
						mustProcessLast = false;
					}
				}
			} else
				mustProcessLast = false;
			finished = ! iterator.hasNext();

			return exemplarFactory.apply(prototype, labels);
		}
	}

}
