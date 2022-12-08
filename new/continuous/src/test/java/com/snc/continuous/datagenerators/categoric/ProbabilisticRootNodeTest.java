package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbabilisticRootNodeTest {
	@Test
	public void probabilityIsOne() {
		assertEquals(1.0, new ProbabilisticRootNode().getProbability(), 0);
	}
}