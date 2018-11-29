package com.mangione.continuous.datagenerators.categoric;

public class ProbabilityNode {
	private final double probability;

	ProbabilityNode(double probability)  {
		this.probability = probability;
	}

	public void accept(Visitor  visitor) {
		visitor.visit(this);
	}

	double getProbability() {
		return probability;
	}

}
