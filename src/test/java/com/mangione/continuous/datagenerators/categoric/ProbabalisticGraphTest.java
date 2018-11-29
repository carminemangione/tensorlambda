package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProbabalisticGraphTest {

	@Test
	public void graphWithOneNode() {
		double[] variableProbabilities = {0.1};

		ProbabalisticGraph probabalisticGraph = new ProbabalisticGraph(variableProbabilities);
		ProbabilityNode node = probabalisticGraph.getRootNode(0);
		assertEquals(0.1, node.getProbability(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidVariableIndex() {
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongNumberOfDimensions() {
	}
}