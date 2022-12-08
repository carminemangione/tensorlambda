package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;

public class XGBoostFromFiles {
	public static void main(String[] args) throws Exception {
		File testFile = new File("test.libsvm");
		DMatrix testMat = new DMatrix(testFile.getAbsolutePath());
		Booster model = XGBoost.loadModel(testFile.getAbsolutePath());
		float[][] predict = model.predict(testMat);

	}


	public XGBoostFromFiles(File train, File test, int numClasses) throws Exception {
		DMatrix trainMat = new DMatrix(train.getAbsolutePath());
		//trainMat.setGroup(trainGroups);
		DMatrix testMat = new DMatrix(test.getAbsolutePath());
		//testMat.setGroup(testGroups);

		Map<String, Object> params = new HashMap<String, Object>() {
			{
				put("eta", 0.1);
				put("gamma", 1.0);
				put("max_depth", 6);
				put("objective", "multi:softmax");
				put("num_class", numClasses);
				put("min_child_weight", 0.1);
				put("eval_metric", "mlogloss");
				put("verbosity", 2);
				put("nthread", 4);
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
		float[][] predict = booster.predict(testMat);

	}
}
