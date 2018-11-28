package com.mangione.continuous.datagenerators.categoric;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ProbabalisticGraphTest {

	@Test
	public void graphWithOneNode() {
		double[] nonDependentProbabilities = {0.1};
		fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidVariableIndex() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongNumberOfDimensions() {
	}


}