package com.mangione.continuous.observations;

import static org.junit.Assert.*;

import org.junit.Test;

public class NamedColumnsTest {
	@Test
	public void isNamedColumn() throws Exception {
		NamedColumns namedColumns = new NamedColumns();
		namedColumns.addColumn(1, "a name");
		namedColumns.addColumn(3, "other name");
		assertFalse(namedColumns.isNamed(0));
		assertFalse(namedColumns.isNamed(2));

		assertTrue(namedColumns.isNamed(1));
		assertTrue(namedColumns.isNamed(3));
	}
	

	@Test
	public void columnName() throws Exception {
		NamedColumns namedColumns = new NamedColumns();
		namedColumns.addColumn(1, "a name");
		namedColumns.addColumn(3, "other name");
		assertEquals("a name", namedColumns.getName(1));
		assertEquals("other name", namedColumns.getName(3));
		assertNull(namedColumns.getName(0));
	}

}