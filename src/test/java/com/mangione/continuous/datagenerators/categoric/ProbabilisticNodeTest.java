package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbabilisticNodeTest {

	@Test
	public void visit() {
		ProbabilisticNode probabilisticNode = new ProbabilisticNode(0.5);
		final boolean[] visited = {false};
		probabilisticNode.accept(probabilityNode1 -> {
			visited[0] = true;
			assertEquals(0.5, probabilityNode1.getProbability(), 0);
		});
		assertTrue(visited[0]);
	}




}