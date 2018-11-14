package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProxyValuesTest {
	@Test
	public void addSeveral() {
		ProxyValues proxyValues = new ProxyValues();
		assertEquals(0, proxyValues.size());
		proxyValues.add("one");
		assertEquals(1, proxyValues.size());
		proxyValues.add("two");
		assertEquals(2, proxyValues.size());

		assertEquals(Integer.valueOf(0), proxyValues.getIndex("one"));
		assertEquals(Integer.valueOf(1), proxyValues.getIndex("two"));
		assertEquals("one", proxyValues.getName(0));
		assertEquals("two", proxyValues.getName(1));

		assertNull(proxyValues.getIndex("notThere"));
		assertNull(proxyValues.getName(3));
	}


	@Test
	public void addDuplicateDoesNotRepeat() {
		ProxyValues proxyValues = new ProxyValues();
		assertEquals(0, proxyValues.size());
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		proxyValues.add("one");
		assertEquals(1, proxyValues.size());
	}


	@Test
	public void toAndFromString() {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.add("one");
		proxyValues.add("two");

		ProxyValues reconstituted = ProxyValues.fromString(proxyValues.toString());
		assertEquals(2, reconstituted.size());
		assertEquals(Integer.valueOf(0), reconstituted.getIndex("one"));
		assertEquals(Integer.valueOf(1), reconstituted.getIndex("two"));
		assertEquals("one", reconstituted.getName(0));
		assertEquals("two", reconstituted.getName(1));
	}

}