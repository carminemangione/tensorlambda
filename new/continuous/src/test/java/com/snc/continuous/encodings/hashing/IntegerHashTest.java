package com.mangione.continuous.encodings.hashing;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegerHashTest {
	@Test
	public void hashInteger() {
		IntegerHash featureMap = new IntegerHash(1305);
		int a = featureMap.apply(1);
		int b = featureMap.apply(2);
		//Assert.assertNotExotic...
		assertNotEquals(a,b);
	}
}