package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.CSR.ProviderToCSRMatrixWithTarget;

public class XGBoostFromProvider<FEATURE extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, Integer>> {
	public XGBoostFromProvider(ObservationProviderInterface<FEATURE, EXEMPLAR> train, ObservationProviderInterface<FEATURE, EXEMPLAR> test,
			Function<EXEMPLAR, Integer> targetFunction, Function<EXEMPLAR, Integer> groupingFunction, int size) throws Exception {

		ProviderToCSRMatrixWithTarget<FEATURE, Integer, EXEMPLAR> trainCSR = new ProviderToCSRMatrixWithTarget<>(train, targetFunction);
		trainCSR.process();
		int[] trainGroups = calculateGroups(train, groupingFunction);

		ProviderToCSRMatrixWithTarget<FEATURE, Integer, EXEMPLAR> testCSR = new ProviderToCSRMatrixWithTarget<>(test, targetFunction);
		testCSR.process();
		int[] testGroups = calculateGroups(test, groupingFunction);
		int numberOfFeatures = test.getNumberOfFeatures();
		new XGBoostFromCSR(trainCSR.getCSRWithTags(), testCSR.getCSRWithTags(),
				trainGroups, testGroups, numberOfFeatures);

	}


	private int[] calculateGroups(ObservationProviderInterface<FEATURE, EXEMPLAR> provider, Function<EXEMPLAR, Integer> groupFunction) {
		return new Groups<>(provider, groupFunction).getGroups();
	}
}
