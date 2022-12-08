package com.mangione.continuous.observationproviders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.Exemplar;
import com.mangione.continuous.observations.dense.Observation;

public class MultiLabelExemplarProviderTest {
	@Test
	public void oneEntry() {
		Integer[][] observations = new Integer[][]{{1, 2}};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(data.getFeatures(Integer[]::new)), 3));

		MultiLabelExemplarProvider<Integer, DiscreteExemplar, ExemplarInterface<Integer, Integer[]>> multiProvider =
				new MultiLabelExemplarProvider<>(provider, (x, y) -> true,
						(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0])));
		Iterator<ExemplarInterface<Integer, Integer[]>> iterator = multiProvider.iterator();
		assertTrue(iterator.hasNext());
		ExemplarInterface<Integer, Integer[]> next = iterator.next();
		assertArrayEquals(new Integer[]{3}, next.getLabel());
		assertArrayEquals(new Integer[]{1, 2}, next.getFeatures(Integer[]::new));
		assertFalse(iterator.hasNext());
	}

	@Test
	public void twoEntriesSameGroup() {
		Integer[][] observations = new Integer[][]{{1, 2, 3}, {1, 2, 6}, {3, 5, 7}};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(Arrays.copyOfRange(data.getFeatures(Integer[]::new), 0, 2)), data.getFeatures(Integer[]::new)[2]));

		MultiLabelExemplarProvider<Integer, DiscreteExemplar, ExemplarInterface<Integer, Integer[]>> multiProvider =
				new MultiLabelExemplarProvider<>(provider, (x, y) -> Arrays.equals(x.getFeatures(Integer[]::new), y.getFeatures(Integer[]::new)),
						(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0])));
		Iterator<ExemplarInterface<Integer, Integer[]>> iterator = multiProvider.iterator();
		assertTrue(iterator.hasNext());
		ExemplarInterface<Integer, Integer[]> next = iterator.next();
		assertArrayEquals(new Integer[]{3, 6}, next.getLabel());
		assertArrayEquals(new Integer[]{1, 2}, next.getFeatures(Integer[]::new));
		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertArrayEquals(new Integer[]{7}, next.getLabel());
		assertArrayEquals(new Integer[]{3, 5}, next.getFeatures(Integer[]::new));
		assertFalse(iterator.hasNext());
	}

	@Test(expected = IllegalStateException.class)
	public void nextAtEndExcepts()  {
		Integer[][] observations = new Integer[][]{{1, 2, 3}};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(Arrays.copyOfRange(data.getFeatures(Integer[]::new), 0, 2)), data.getFeatures(Integer[]::new)[2]));

		MultiLabelExemplarProvider<Integer, DiscreteExemplar, ExemplarInterface<Integer, Integer[]>> multiProvider =
				new MultiLabelExemplarProvider<>(provider, (x, y) -> x.getFeature(0).equals(y.getFeature(0)),
						(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0])));
		Iterator<ExemplarInterface<Integer, Integer[]>> iterator = multiProvider.iterator();
		iterator.next();
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = IllegalStateException.class)
	public void emptyExcepts()  {
		Integer[][] observations = new Integer[][]{};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(Arrays.copyOfRange(data.getFeatures(Integer[]::new), 0, 2)), data.getFeatures(Integer[]::new)[2]));

		new MultiLabelExemplarProvider<>(provider, (x, y) -> x.getFeature(0).equals(y.getFeature(0)),
				(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0]))).iterator();
	}

	@Test
	public void sizeReturnsNumberOfGroups() {
		Integer[][] observations = new Integer[][]{{1, 2, 3}, {1, 2, 6}, {1, 5, 7}};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(Arrays.copyOfRange(data.getFeatures(Integer[]::new), 0, 2)), data.getFeatures(Integer[]::new)[2]));

		MultiLabelExemplarProvider<Integer, DiscreteExemplar, ExemplarInterface<Integer, Integer[]>> multiProvider =
				new MultiLabelExemplarProvider<>(provider, (x, y) -> x.getFeature(0).equals(y.getFeature(0)),
						(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0])));
		assertEquals(1, multiProvider.size());
	}


	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void emptyProviderExcepts()  {
		Integer[][] observations = new Integer[][]{};
		ObservationProviderInterface<Integer, Observation<Integer>> aop = new ArrayObservationProvider<>(observations, Observation::new);
		ObservationToExemplarProvider<Integer, DiscreteExemplar> provider = new ObservationToExemplarProvider<>(aop, (data) ->
				new DiscreteExemplar(Arrays.asList(data.getFeatures(Integer[]::new)), 3));

		expectedException.expect(IllegalStateException.class);
		new MultiLabelExemplarProvider<>(provider, (x, y) -> true,
						(f, l)->new Exemplar<>(Arrays.asList(f.getFeatures(Integer[]::new)), l.toArray(new Integer[0])));
	}
}