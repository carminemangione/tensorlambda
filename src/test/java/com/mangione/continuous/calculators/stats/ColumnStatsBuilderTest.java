package com.mangione.continuous.calculators.stats;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.dense.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

public class ColumnStatsBuilderTest {

	private ColumnStatsBuilder columnStatsBuilder;

	@Before
	public void setUp() throws Exception {
		ArrayObservationProvider<Double, ObservationInterface<Double>> provider =
				new ArrayObservationProvider<>(new Double[][]{{2., 1.0}, {20., 2.0}, {18., 4.0}, {0., 1.0}}, new DoubleObservationFactory());

		columnStatsBuilder = new ColumnStatsBuilder(provider);
	}

	@Test
	public void multipleColumns() throws Exception {
		validateBuiltColumnStats(columnStatsBuilder);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyIteratorExcepts() throws Exception {
		ArrayObservationProvider<Double, ObservationInterface<Double>> provider =
				new ArrayObservationProvider<>(new Double[0][0], new DoubleObservationFactory());

		new ColumnStatsBuilder(provider);
	}

	@Test
	public void serialize() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(baos);
		stream.writeObject(columnStatsBuilder);
		stream.flush();
		stream.close();

		ColumnStatsBuilder reconstituted = (ColumnStatsBuilder) new ObjectInputStream(
				new ByteArrayInputStream(baos.toByteArray())).readObject();
		validateBuiltColumnStats(reconstituted);
	}

	private void validateBuiltColumnStats(ColumnStatsBuilder columnStatsBuilder) {
		ColumnStats stats = columnStatsBuilder.get(1);
		assertEquals(2.0, stats.avg(), Double.MIN_VALUE);
		assertEquals(4.0, stats.max(), Double.MIN_VALUE);
		assertEquals(1.0, stats.min(), Double.MIN_VALUE);
		assertEquals(1.4142135623731, stats.std(), 1.0e-12);

		stats = columnStatsBuilder.get(0);
		assertEquals(10, stats.avg(), Double.MIN_VALUE);
		assertEquals(20, stats.max(), Double.MIN_VALUE);
		assertEquals(0, stats.min(), Double.MIN_VALUE);
		assertEquals(10.456258094239, stats.std(), 1.0e-12);
	}

}