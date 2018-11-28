package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbabilityNodeTest {

	@Test
	public void visit() {
		ProbabilityNode probabilityNode = new ProbabilityNode("C", 0.5, "A", "B");
		final boolean[] visited = {false};
		probabilityNode.accept(probabilityNode1 -> {
			visited[0] = true;
			assertEquals(0.5, probabilityNode1.getProbability(), 0);
			assertEquals("C", probabilityNode1.getTargetName());
			assertArrayEquals(new String[]{"A", "B"}, probabilityNode1.getGivens());
		});
		assertTrue(visited[0]);
	}

	@Test
	public void string() {
		ProbabilityNode probabilityNode = new ProbabilityNode("C", 0.5, "A", "B");
		assertEquals("P(C|A,B) = 0.5", probabilityNode.toString());
	}

}