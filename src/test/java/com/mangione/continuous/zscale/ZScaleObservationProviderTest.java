package com.mangione.continuous.zscale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

public class ZScaleObservationProviderTest {

	private Double[] singleColumn;
	private ColumnZScale singleColumnZScale;
	private ZScaleObservationProvider<ObservationInterface<Double>> singleColumnObservationProvider;

	@Before
	public void setUp() throws Exception {
		Double[][] singleColumnObservation = new Double[][]{{3d}, {5d}, {7d}, {69d}, {27d}};
		singleColumn = new Double[]{3d, 5d, 7d, 69d, 27d};
		singleColumnZScale = new ColumnZScale(ArrayUtils.toPrimitive(singleColumn));
		ArrayObservationProvider<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(
				singleColumnObservation, new DoubleObservationFactory());
		this.singleColumnObservationProvider = new ZScaleObservationProvider<>(
				provider, new DoubleObservationFactory());
	}

	@Test
	public void oneColumnZScaled() throws Exception {
		Iterator<ObservationInterface<Double>> iterator = singleColumnObservationProvider.iterator();
		for (double aColumn1 : singleColumn) {
			assertTrue(iterator.hasNext());
			final Double[] features = iterator.next().getFeatures();
			assertEquals(1, features.length);
			assertEquals(singleColumnZScale.zscale(aColumn1), features[0], 0.0);
		}
	}

	@Test
	public void twoColumnsZScaled() {
		Double[] column1 = {3d, 5d, 7d, 69d, 27d};
		Double[] column2 = {100d, 367d, 7d, 1811d, 2d};
		Double[][] twoColumnObservations = new Double[column1.length][2];
		for (int i = 0; i < twoColumnObservations.length; i++) {
			twoColumnObservations[i][0] = column1[i];
			twoColumnObservations[i][1] = column2[i];
		}
		ColumnZScale czs1 = new ColumnZScale(ArrayUtils.toPrimitive(column1));
		ColumnZScale czs2 = new ColumnZScale(ArrayUtils.toPrimitive(column2));
		ArrayObservationProvider<Double, ObservationInterface<Double>> aop = new ArrayObservationProvider<>(
				twoColumnObservations, new DoubleObservationFactory());
		ZScaleObservationProvider<ObservationInterface<Double>> observationProvider = new ZScaleObservationProvider<>(
				aop, new DoubleObservationFactory());

		Iterator<ObservationInterface<Double>> iterator = observationProvider.iterator();
		for (int i = 0; i < column1.length; i++) {
			assertTrue(iterator.hasNext());
			final Double[] features = iterator.next().getFeatures();
			assertEquals(2, features.length);
			assertEquals(czs1.zscale(column1[i]), features[0], 0.0);
			assertEquals(czs2.zscale(column2[i]), features[1], 0.0);
		}
		assertFalse(iterator.hasNext());
	}

	@Test
	public void foreach() throws Exception {
		final int[] i = {0};
		singleColumnObservationProvider.forEach(doubleObservationInterface ->
				assertEquals(singleColumnZScale.zscale(singleColumn[i[0]++]),
						doubleObservationInterface.getFeatures()[0], 0));
		assertEquals(singleColumn.length, i[0]);
	}

	@Test
	public void iteratorRemove() throws Exception {
		Iterator<ObservationInterface<Double>> iteratorWithSecondElementRemoved =
				singleColumnObservationProvider.iterator();
		iteratorWithSecondElementRemoved.next();
		iteratorWithSecondElementRemoved.remove();
		assertTrue(iteratorWithSecondElementRemoved.hasNext());

		iteratorWithSecondElementRemoved = singleColumnObservationProvider.iterator();
		int i = 0;
		while (iteratorWithSecondElementRemoved.hasNext()) {
			if (i == 1)
				i++;
			assertEquals(singleColumnZScale.zscale(singleColumn[i++]),
					iteratorWithSecondElementRemoved.next().getFeatures()[0], 0);
		}
		assertEquals(singleColumn.length, i);
	}

	@Test
	public void twoIterators() throws Exception {
		final int[] i = {0};
		singleColumnObservationProvider.forEach(doubleObservationInterface -> {
			assertEquals(singleColumnZScale.zscale(singleColumn[i[0]++]),
					doubleObservationInterface.getFeatures()[0], 0);
			final int[] inner = {0};
			singleColumnObservationProvider.forEach(innerObs -> assertEquals(singleColumnZScale.zscale(singleColumn[inner[0]++]),
					innerObs.getFeatures()[0], 0));
		});
		assertEquals(singleColumn.length, i[0]);
	}

	@Test
	public void emptyProviderDoesNotBlowUp() throws Exception {
		ArrayObservationProvider<Double, ObservationInterface<Double>> empty = new ArrayObservationProvider<>(
				new Double[0][0], new DoubleObservationFactory());
		ZScaleObservationProvider<ObservationInterface<Double>> provider = new ZScaleObservationProvider<>(
				empty, new DoubleObservationFactory());
		assertFalse(provider.iterator().hasNext());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void spliterator() throws Exception {
		singleColumnObservationProvider.spliterator();
	}

}