package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.sparse.CSR.CSRWithTags;
import com.mangione.continuous.util.coersion.CoerceToFloatArray;
import com.mangione.continuous.util.coersion.CoerceToLongArray;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;

public class XGBoostFromCSR {
	public XGBoostFromCSR(CSRWithTags<Integer> train, CSRWithTags<Integer> test,
			int[] trainGroups, int[] testGroups, int numberOfFeatures) throws Exception {
		DMatrix trainMat = getDMat(train, numberOfFeatures, trainGroups);
		DMatrix testMat = getDMat(test, numberOfFeatures, testGroups);

		Map<String, Object> params = new HashMap<String, Object>() {
			{
				put("eta", 0.1);
				put("gamma", 1.0);
				put("max_depth", 6);
				put("objective", "rank:ndcg");
				put("min_child_weight", 0.1);
				put("eval_metric", "logloss");
				put("verbosity", 2);
			}
		};

		HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>() {
			{
				put("train", trainMat);
				put("test", testMat);
			}
		};

		Booster booster = XGBoost.train(trainMat, params, 4, watches, null, null);
		booster.saveModel("ndcg");
		float[][] predict = booster.predict(getTestMatrix(test, numberOfFeatures, testGroups.length));
		float[][] predict1 = booster.predict(testMat);

	}

	@Nonnull
	private DMatrix getDMat(CSRWithTags<Integer> csrWithTags, int numberOfFeatures, int[] groups) throws Exception {
		DMatrix matrix = new DMatrix(CoerceToLongArray.coerce(csrWithTags.getRows()), csrWithTags.getColumns(),
				CoerceToFloatArray.coerce(Arrays.asList(csrWithTags.getValues())), DMatrix.SparseType.CSR,
				numberOfFeatures);
		matrix.setLabel(CoerceToFloatArray.coerce(csrWithTags.getTags()));
		matrix.setGroup(groups);
		return matrix;
	}

	@Nonnull
	private static DMatrix getTestMatrix(CSRWithTags<Integer> csrWithTags, int numberOfFeatures, int numberOfCategories) throws Exception {
		int rowSize = csrWithTags.getRows()[0];
		int[] columns = Arrays.copyOfRange(csrWithTags.getColumns(), 0, rowSize);
		float[] values = CoerceToFloatArray.coerce(Arrays.asList(Arrays.copyOfRange(csrWithTags.getValues(), 0, rowSize)));
		int numColumns = numberOfCategories * rowSize;

		int[] allColumns = new int[numColumns];
		for (int i = 0; i < numColumns; i++)
			allColumns[i] = columns[i % rowSize];
		float[] allValues = new float[numColumns];
		for (int i = 0; i < numColumns; i++)
			allValues[i] = values[i % rowSize];
		long[] allRows = new long[numberOfCategories + 1];
		for (int i = 0; i <= numberOfCategories; i++)
			allRows[i] = i * rowSize;

		DMatrix matrix = new DMatrix(allRows, allColumns, allValues, DMatrix.SparseType.CSR,
				numberOfFeatures);
		matrix.setGroup(IntStream.range(0, numberOfCategories).boxed().mapToInt(x->1).toArray());
		return matrix;


	}

}
