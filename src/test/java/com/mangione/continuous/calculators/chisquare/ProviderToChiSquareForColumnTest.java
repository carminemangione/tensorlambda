package com.mangione.continuous.calculators.chisquare;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarInterface;

public class ProviderToChiSquareForColumnTest {

	private static final int[][] COUNTS = new int[][]{{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};

	@Test
	// Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
	public void fromExample() {

		ObservationProvider<Integer, SparseExemplarInterface<Integer, Integer>> provider =
				new SampleSparseExemplarProvider(null);

		ProviderToChiSquareForColumn chiSquareForColumn = new ProviderToChiSquareForColumn(provider, 0);
		assertEquals(1.51, chiSquareForColumn.getChiSquare(), 0.01);
	}

	private static class SampleSparseExemplarProvider extends ObservationProvider<Integer, SparseExemplarInterface<Integer, Integer>> {

		private List<SparseExemplarInterface<Integer, Integer>> fRows = new ArrayList<>();

		private SampleSparseExemplarProvider(ObservationFactoryInterface<Integer, ? extends SparseExemplarInterface<Integer, Integer>> factory) {
			super(factory);
			int[] columns = {0, 1};

			for (int observationState = 0; observationState < 3; observationState++) {
				for (int targetState = 0; targetState < 3; targetState++) {
					Integer[] values = {observationState, targetState};
					for (int newRow = 0; newRow < COUNTS[observationState][targetState]; newRow++) {
						fRows.add(new SparseExemplar<>(values, columns, 2, 0, 1));
					}
				}
			}
		}

		@Nonnull
		@Override
		public Iterator<SparseExemplarInterface<Integer, Integer>> iterator() {
			return new Iterator<SparseExemplarInterface<Integer, Integer>>() {
				private int fCurrentRow;
				@Override
				public boolean hasNext() {
					return fCurrentRow < fRows.size();
				}

				@Override
				public SparseExemplarInterface<Integer, Integer> next() {
					return fRows.get(fCurrentRow++);
				}
			};
		}

	}
}