package com.mangione.continuous.calculators.chisquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class ProviderToChiSquareForFeatureTest {

	private static final Integer[][] COUNTS = new Integer[][]{{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};

	@Test
	// Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
	public void fromExample() {

		///assertEquals(1.51, chiSquareForColumn.getChiSquare(), 0.01);
	}

	static class ContingencyTableExemplarProvider implements ObservationProviderInterface<Integer, ExemplarInterface<Integer, Integer>> {

		private final List<ExemplarInterface<Integer, Integer>> fRows = new ArrayList<>();

		ContingencyTableExemplarProvider(int numberOfFeatures) {

			for (int observationState = 0; observationState < 3; observationState++) {
				for (int targetState = 0; targetState < 3; targetState++) {
					addRowsForEachColumnForEachCount(numberOfFeatures, observationState, targetState);
				}
			}
		}

		private void addRowsForEachColumnForEachCount(int numberOfFeatures, int observationState, int targetState) {
				addAnExemplarForEachCount(createExemplarForThisColumn(numberOfFeatures,
						observationState, targetState), COUNTS[observationState][targetState]);
		}

		private void addAnExemplarForEachCount(SparseExemplar<Integer, Integer> exemplarForThisColumn, Integer integer) {
			for (int newRow = 0; newRow < integer; newRow++) {
				fRows.add(exemplarForThisColumn);
			}
		}

		private SparseExemplar<Integer, Integer> createExemplarForThisColumn(int numberOfFeatures, int observationState, int targetState) {
			int[] columns = IntStream.range(0, numberOfFeatures + 1).toArray();
			Integer[] values = new Integer[numberOfFeatures + 1];
			Arrays.fill(values, 0);
			Arrays.fill(values, 0, numberOfFeatures, observationState);
			values[numberOfFeatures] = targetState;
			return new SparseExemplar<>(values, columns, numberOfFeatures + 1, 0, numberOfFeatures);
		}

		@Nonnull
		@Override
		public Iterator<ExemplarInterface<Integer, Integer>> iterator() {
			return new Iterator<>() {
				private int fCurrentRow;

				@Override
				public boolean hasNext() {
					return fCurrentRow < fRows.size();
				}

				@Override
				public ExemplarInterface<Integer, Integer> next() {
					return fRows.get(fCurrentRow++);
				}
			};
		}

	}
}