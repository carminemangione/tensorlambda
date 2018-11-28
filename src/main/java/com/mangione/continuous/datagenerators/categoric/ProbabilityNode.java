package com.mangione.continuous.datagenerators.categoric;

public class ProbabilityNode {
	private final String[] givens;
	private final String targetName;
	private final double probability;


	ProbabilityNode(String targetName, double probability, String... givens) {
		this.givens = givens;
		this.targetName = targetName;
		this.probability = probability;
	}

	public void accept(Visitor  visitor) {
		visitor.visit(this);
	}

	String[] getGivens() {
		return givens;
	}

	String getTargetName() {
		return targetName;
	}

	double getProbability() {
		return probability;
	}

	@Override
	public String toString() {
		return "P(" + targetName + "|" + String.join(",", givens) + ") = " + probability;
	}
}
