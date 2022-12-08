package com.mangione.continuous.encodings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ZeroOneEncoderTest {
	@Test
	public void zeroOneEncode() {
		int numCols = 10;
		ZeroOneEncoder zeroOneEncoder = new ZeroOneEncoder(numCols);
		double[] encoded = zeroOneEncoder.encode(1, 5, 7);

		assertEquals(10, encoded.length);
		assertEquals(0, encoded[0], 0);
		assertEquals(1, encoded[1], 0);
		assertEquals(0, encoded[2], 0);
		assertEquals(0, encoded[3], 0);
		assertEquals(0, encoded[4], 0);
		assertEquals(1, encoded[5], 0);
		assertEquals(0, encoded[6], 0);
		assertEquals(1, encoded[7], 0);
		assertEquals(0, encoded[0], 0);
		assertEquals(0, encoded[0], 0);
	}


}