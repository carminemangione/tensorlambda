package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ProbabalisticGraphTest {

	@Test
	public void graphWithOneNode() {
		double[] variableProbabilities = {0.1};

		ProbabalisticGraph probabalisticGraph = new ProbabalisticGraph(variableProbabilities);
		List<ProbabilityNode> nodes = probabalisticGraph.getRootNodes();
		assertEquals(1, nodes.size());
		assertEquals(0.1, nodes.get(0).getProbability(), 0);
	}

	@Test
	public void twoNodesIndependent() {
		double[] variableProbabilities = {0.1};

	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidVariableIndex() {
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongNumberOfDimensions() {
	}
}