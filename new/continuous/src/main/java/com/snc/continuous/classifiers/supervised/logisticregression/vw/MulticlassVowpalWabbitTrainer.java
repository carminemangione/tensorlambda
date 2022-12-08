package com.mangione.continuous.classifiers.supervised.logisticregression.vw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

import com.indeed.vw.wrapper.learner.VWLearners;
import com.indeed.vw.wrapper.learner.VWMulticlassLearner;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

public class MulticlassVowpalWabbitTrainer<EXEMPLAR extends ExemplarInterface<Integer, Integer>>  {
	public MulticlassVowpalWabbitTrainer(ObservationProviderInterface<Integer, EXEMPLAR> trainingProvider,
			File vwFormatOutputFile, String modelFile, Function<EXEMPLAR, Integer> tagFunction, int classCount) throws Exception {

		new ProviderToVW<>(trainingProvider, vwFormatOutputFile, tagFunction);
		train(vwFormatOutputFile, modelFile, classCount);
	}

	private void train(File vwOutputFile, String modelFile, int classCount) throws IOException {
		VWMulticlassLearner learner = VWLearners.create("--oaa " + classCount + " -f " + modelFile);
		BufferedReader br = new BufferedReader(new FileReader(vwOutputFile));
		String nextLine;
		while ((nextLine = br.readLine()) != null) {
			learner.learn(nextLine);
		}

	}
}
