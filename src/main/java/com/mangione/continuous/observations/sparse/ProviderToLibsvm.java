package com.mangione.continuous.observations.sparse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

public class ProviderToLibsvm<EXEMPLAR extends SparseExemplar<Integer, Integer>> extends AbstractProviderToLibsvm<EXEMPLAR> {
	private final BufferedWriter bufferedWriter;

	public ProviderToLibsvm(ObservationProviderInterface<Integer, EXEMPLAR> sparseExemplars, File file) throws Exception {
		bufferedWriter = new BufferedWriter(new FileWriter(file));
		processProvider(sparseExemplars, ExemplarInterface::getLabel);
		bufferedWriter.close();
	}

	@Override
	protected String afterTag(EXEMPLAR exemplar) {
		return " ";
	}

	@Override
	protected void processNextLine(String nextLine) throws IOException {
		bufferedWriter.write(nextLine);
		bufferedWriter.newLine();
	}
}
