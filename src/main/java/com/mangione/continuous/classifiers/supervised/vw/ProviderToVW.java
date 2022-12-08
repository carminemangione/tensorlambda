package com.mangione.continuous.classifiers.supervised.vw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.AbstractProviderToLibsvm;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class ProviderToVW<EXEMPLAR extends ExemplarInterface<Integer, Integer>> extends AbstractProviderToLibsvm<EXEMPLAR> {

	private final BufferedWriter bufferedWriter;

	public ProviderToVW(ObservationProviderInterface<Integer, EXEMPLAR> sparseExemplars, File file,
			Function<EXEMPLAR, Integer> tagFunction) throws Exception {
		bufferedWriter = new BufferedWriter(new FileWriter(file));
		processProvider(sparseExemplars, tagFunction);
		bufferedWriter.close();
	}

	@Override
	protected String afterTag(EXEMPLAR exemplar) {
		return "|";
	}

	@Override
	protected void processNextLine(String nextLine) throws IOException {
		bufferedWriter.write(nextLine);
		bufferedWriter.newLine();
	}
}
