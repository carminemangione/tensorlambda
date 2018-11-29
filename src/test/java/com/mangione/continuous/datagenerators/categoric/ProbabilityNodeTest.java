package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbabilityNodeTest {

	@Test
	public void visit() {
		ProbabilityNode probabilityNode = new ProbabilityNode(0.5);
		final boolean[] visited = {false};
		probabilityNode.accept(probabilityNode1 -> {
			visited[0] = true;
			assertEquals(0.5, probabilityNode1.getProbability(), 0);
		});
		assertTrue(visited[0]);
	}

	@Test
	public void string() {
		ProbabilityNode probabilityNode = new ProbabilityNode(0.5);
		assertEquals("P(C|A,B) = 0.5", probabilityNode.toString());
	}

}