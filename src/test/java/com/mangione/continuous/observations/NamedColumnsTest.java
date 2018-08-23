package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class NamedColumnsTest {
	@Test
	public void isNamedColumn() throws Exception {
		ProxyValues ProxyValues = new ProxyValues();
		ProxyValues.addPair(1, "a name");
		ProxyValues.addPair(3, "other name");
		assertFalse(ProxyValues.isNamed(0));
		assertFalse(ProxyValues.isNamed(2));

		assertTrue(ProxyValues.isNamed(1));
		assertTrue(ProxyValues.isNamed(3));
	}
	

	@Test
	public void columnName() throws Exception {
		ProxyValues ProxyValues = new ProxyValues();
		ProxyValues.addPair(1, "a name");
		ProxyValues.addPair(3, "other name");
		assertEquals("a name", ProxyValues.getName(1));
		assertEquals("other name", ProxyValues.getName(3));
		assertNull(ProxyValues.getName(0));
	}

}