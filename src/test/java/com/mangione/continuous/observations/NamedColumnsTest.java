package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class NamedColumnsTest {
	@Test
	public void isNamedColumn() throws Exception {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.addPair(1, "a name");
		proxyValues.addPair(3, "other name");
		assertFalse(proxyValues.contains(0));
		assertFalse(proxyValues.contains(2));

		assertTrue(proxyValues.contains(1));
		assertTrue(proxyValues.contains(3));
	}
	

	@Test
	public void columnName() throws Exception {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.addPair(1, "a name");
		proxyValues.addPair(3, "other name");
		assertEquals("a name", proxyValues.getName(1));
		assertEquals("other name", proxyValues.getName(3));
		assertNull(proxyValues.getName(0));
	}

	@Test
	public void addFiveValues() throws Exception {
		ProxyValues proxyValues = new ProxyValues();
		for(int i = 0; i < 10; i++) {
			proxyValues.add(i/2 + "");
		}
		for(int i = 0; i < 10; i++) {
			assertTrue(proxyValues.contains(i/2 + ""));
		}

		for(int i = 0; i < 10; i++) {
			assertEquals("" + i/2, proxyValues.getName(i/2));
		}
	}

}