package com.mangione.continuous.calculators.stats;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class ColumnStatsTest {

	private ColumnStats stats;

	@Before
	public void setUp() throws Exception {
		Double[] doubles = {1.0, 2.0, 4.0, 1.0};

		ColumnStats.Builder builder = new ColumnStats.Builder(20);
		Arrays.stream(doubles)
				.forEach(builder::add);

		stats = builder.build();
	}

	@Test
	public void statsInFirstColumn() throws Exception {

		assertEquals(2.0, stats.avg(), Double.MIN_VALUE);
		assertEquals(4.0, stats.max(), Double.MIN_VALUE);
		assertEquals(1.0, stats.min(), Double.MIN_VALUE);
		assertEquals(1.4142135623731, stats.std(), 1.0e-12);
	}

	@Test
	public void serialize() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(baos);
		stream.writeObject(stats);
		stream.flush();
		stream.close();

		ColumnStats reconstituted = (ColumnStats) new ObjectInputStream(
				new ByteArrayInputStream(baos.toByteArray())).readObject();

		assertEquals(2.0, reconstituted.avg(), Double.MIN_VALUE);
		assertEquals(4.0, reconstituted.max(), Double.MIN_VALUE);
		assertEquals(1.0, reconstituted.min(), Double.MIN_VALUE);
		assertEquals(1.4142135623731, reconstituted.std(), 1.0e-12);
	}


}