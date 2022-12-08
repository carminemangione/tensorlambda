package com.mangione.continuous.classifiers.supervised.logisticregression.vw;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indeed.vw.wrapper.learner.VWLearners;
import com.indeed.vw.wrapper.learner.VWScalarLearner;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.AbstractProviderToLibsvm;
import com.mangione.continuous.util.LoggingTimer;

public class MulticlassVowpalWabbitPredictor<EXEMPLAR extends ExemplarInterface<Integer, Integer>>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(MulticlassVowpalWabbitPredictor.class);
	private static final LoggingTimer LOGGING_TIMER = new LoggingTimer(LOGGER, 1000, "Predicted ");
	private final List<Prediction> predictions = new ArrayList<>();

	public MulticlassVowpalWabbitPredictor(ObservationProviderInterface<Integer, EXEMPLAR> predictionProvider, String modelFile,
			Function<EXEMPLAR, Integer> tagFunction) throws Exception {
		new PredictorProviderToVW(predictionProvider, modelFile, tagFunction);
	}

	public List<Prediction> getPredictions() {
		return predictions;
	}

	private class PredictorProviderToVW extends AbstractProviderToLibsvm<EXEMPLAR> {
		private final VWScalarLearner learner;

		public PredictorProviderToVW(ObservationProviderInterface<Integer, EXEMPLAR> sparseExemplars, String modelFile, Function<EXEMPLAR, Integer> tagFunction) throws Exception {
			learner = VWLearners.create("-i " + modelFile + " -t");
			processProvider(sparseExemplars, tagFunction);
		}

		@Override
		protected String afterTag(EXEMPLAR exemplar) {
			return "|";
		}

		@Override
		protected void processNextLine(String nextLine) {
			predictions.add(new Prediction((int)learner.predict(nextLine),
					Integer.parseInt(nextLine.split("\\|")[0].trim())));
			LOGGING_TIMER.nextStep();
		}
	}
}
