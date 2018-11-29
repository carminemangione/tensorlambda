package com.mangione.continuous.datagenerators.categoric;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ProbabalisticGraph {
	private final List<ProbabilityNode> rootNodes;

	ProbabalisticGraph(double[] variableProbabilities) {
		rootNodes = Arrays.stream(variableProbabilities)
				.mapToObj(ProbabilityNode::new)
				.collect(Collectors.toList());
	}

	ProbabilityNode getRootNode(int variableIndex) {
		return rootNodes.get(variableIndex);
	}
}
