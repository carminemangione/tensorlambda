package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarInterface;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ProviderToChiSquareForFeatureTest {

	private static final Integer[][] COUNTS = new Integer[][]{{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};

	@Test
	// Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
	public void fromExample() {

		ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider =
				new ContingencyTableExemplarProvider(null, 1);

		ProviderToChiSquareForFeature chiSquareForColumn = new ProviderToChiSquareForFeature(provider, 0);
		assertEquals(1.51, chiSquareForColumn.getChiSquare(), 0.01);
	}

	static class ContingencyTableExemplarProvider extends ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> {

		private List<SparseExemplarInterface<Integer, Integer>> fRows = new ArrayList<>();

		ContingencyTableExemplarProvider(ObservationFactoryInterface<Integer, ? extends ExemplarInterface<Integer, Integer>> factory,
										 int numberOfFeatures) {
			super(factory);

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

		private void addAnExemplarForEachCount(SparseExemplar<Integer> exemplarForThisColumn, Integer integer) {
			for (int newRow = 0; newRow < integer; newRow++) {
				fRows.add(exemplarForThisColumn);
			}
		}

		private SparseExemplar<Integer> createExemplarForThisColumn(int numberOfFeatures, int observationState, int targetState) {
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
			return new Iterator<ExemplarInterface<Integer, Integer>>() {
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