package com.mangione.continuous.calculators;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import com.mangione.continuous.calculators.stats.ColumnStats;


public class MinMaxScalingTest {

	@Test
	public void minMaxScale() throws Exception {
		ColumnStats.Builder builder = new ColumnStats.Builder(20);
		builder.add(2);
		builder.add(4);

		ColumnStats stats = builder.build();
		MinMaxScaling minMaxScaling = new MinMaxScaling(stats);

		assertEquals(0, minMaxScaling.apply(2.).get(0), Double.MIN_VALUE);
		assertEquals(1, minMaxScaling.apply(4.).get(0), Double.MIN_VALUE);
		assertEquals(0.5, minMaxScaling.apply(3.).get(0), Double.MIN_VALUE);
	}

	@Test
	public void serialize() throws Exception {
		ColumnStats.Builder builder = new ColumnStats.Builder(20);
		builder.add(2);
		builder.add(4);

		ColumnStats stats = builder.build();
		MinMaxScaling minMaxScaling = new MinMaxScaling(stats);


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(baos);
		stream.writeObject(minMaxScaling);
		stream.flush();
		stream.close();

		MinMaxScaling reconstituted = (MinMaxScaling) new ObjectInputStream(
				new ByteArrayInputStream(baos.toByteArray())).readObject();

		assertEquals(0, reconstituted.apply(2.).get(0), Double.MIN_VALUE);
		assertEquals(1, reconstituted.apply(4.).get(0), Double.MIN_VALUE);
		assertEquals(0.5, reconstituted.apply(3.).get(0), Double.MIN_VALUE);
	}

}