package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
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

	@Test
	public void addIndependentChild() {
		// P(A|B) = P(B|A) = P(A)P(B)
		double PofA = 0.5;
		ProbabilisticNode probabilisticNode = new ProbabilisticNode(PofA);
		assertEquals(0, probabilisticNode.getChildren().size());
		double PofB = 0.40;
		probabilisticNode.addIndependentChild(PofB);
		List<ProbabilisticNode> children = probabilisticNode.getChildren();
		assertEquals(1, children.size());
		assertEquals(PofA*PofB, children.get(0).getProbability(), 0);
	}

	@Test
	@Ignore
	public void indexesForParentAndChild() {
		fail();
	}


}