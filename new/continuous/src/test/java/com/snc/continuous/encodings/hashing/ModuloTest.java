package com.mangione.continuous.encodings.hashing;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

public class ModuloTest {
	@Test
	public void modulo() {
		int base = 5;
		Function<Integer, Integer> f = new Modulo(base);
		for (int i = 0; i < 12; i++) {
			int expected = Integer.remainderUnsigned(i, base);
			int actual = f.apply(i);
			Assert.assertEquals(expected, actual);
			Assert.assertTrue(actual >= 0);
			Assert.assertTrue(actual <= base);
		}
	}
}