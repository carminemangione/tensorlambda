package com.mangione.continuous.datagenerators.categoric;

import java.util.ArrayList;
import java.util.List;

public class ProbabilisticNode {
	private final double probability;
	private final List<ProbabilisticNode> children = new ArrayList<>();

	ProbabilisticNode(double probability)  {
		this.probability = probability;
	}

	public void accept(Visitor  visitor) {
		visitor.visit(this);
	}

	List<ProbabilisticNode> getChildren() {
		return children;
	}

	double getProbability() {
		return probability;
	}

	void addIndependentChild(double probability) {
		children.add(new ProbabilisticNode(probability * this.probability));
	}


}
