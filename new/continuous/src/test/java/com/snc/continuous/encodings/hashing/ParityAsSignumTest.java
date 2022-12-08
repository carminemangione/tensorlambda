package com.mangione.continuous.encodings.hashing;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

public class ParityAsSignumTest {
	@Test
	public void parityToSignum() {
		Function<Integer, Integer> f = new ParityAsSignum();
		for (int i = 0; i < 12; i++) {
			int expected = i % 2 == 1 ? 1 : -1;
			int actual = f.apply(i);
			Assert.assertEquals(expected, actual);
		}
	}
}