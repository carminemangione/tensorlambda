package com.mangione.continuous.encodings;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import com.mangione.continuous.encodings.ProxyValues;

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
		assertEquals("Unknown", proxyValues.getName(3));
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
	public void indexNotThereReturnsUnknown() {
		ProxyValues proxyValues = new ProxyValues();
		assertEquals("Unknown", proxyValues.getName(0));
	}

	@Test
	public void toAndFromString() throws IOException {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.add("one");
		proxyValues.add("two");

		try (ByteArrayInputStream bais = new ByteArrayInputStream(proxyValues.toString().getBytes())) {
			try (Reader reader = new InputStreamReader(bais)) {
				ProxyValues reconstituted = ProxyValues.fromReader(reader);
				assertEquals(2, reconstituted.size());
				assertEquals(Integer.valueOf(0), reconstituted.getIndex("one"));
				assertEquals(Integer.valueOf(1), reconstituted.getIndex("two"));
				assertEquals("one", reconstituted.getName(0));
				assertEquals("two", reconstituted.getName(1));
			}
		}
	}

	@Test
	public void isNamedColumn() {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.addPair(1, "a name");
		proxyValues.addPair(3, "other name");
		assertFalse(proxyValues.contains(0));
		assertFalse(proxyValues.contains(2));

		assertTrue(proxyValues.contains(1));
		assertTrue(proxyValues.contains(3));
	}


	@Test
	public void columnName() {
		ProxyValues proxyValues = new ProxyValues();
		proxyValues.addPair(1, "a name");
		proxyValues.addPair(3, "other name");
		assertEquals("a name", proxyValues.getName(1));
		assertEquals("other name", proxyValues.getName(3));
		assertEquals("Unknown", proxyValues.getName(0));
	}

	@Test
	public void addFiveValues() {
		ProxyValues proxyValues = new ProxyValues();
		for(int i = 0; i < 10; i++) {
			proxyValues.add(i + "");
		}
		for(int i = 0; i < 10; i++) {
			assertTrue(proxyValues.contains(i + ""));
		}

		for(int i = 0; i < 10; i++) {
			assertEquals("" + i, proxyValues.getName(i));
		}
	}


}