package com.mangione.continuous.datagenerators.categoric;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ProbabalisticGraphBuilder {
	private final List<ProbabilisticNode> rootNodes;

	ProbabalisticGraphBuilder(double[] variableProbabilities) {
		rootNodes = Arrays.stream(variableProbabilities)
				.mapToObj(ProbabilisticNode::new)
				.collect(Collectors.toList());
	}

	public List<ProbabilisticNode> getRootNodes() {
		return rootNodes;
	}
}
