package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;

public class L2rankTrain {
	public static void main(String[] args) {
		try {
			DMatrix trainMat = new DMatrix("/Users/carmine.mangione/projects/xgboost/demo/rank/mq2008.train");
			DMatrix testMat = new DMatrix("/Users/carmine.mangione/projects/xgboost/demo/rank/mq2008.test");

			String trainGroupFile = "/Users/carmine.mangione/projects/xgboost/demo/rank/mq2008.train.group";
			String testGroupFile = "/Users/carmine.mangione/projects/xgboost/demo/rank/mq2008.test.group";

			BufferedReader trainBr = new BufferedReader(new FileReader(trainGroupFile));
			BufferedReader testBr = new BufferedReader(new FileReader(testGroupFile));

			String line;
			List<Integer> trainGroupValueList = new ArrayList<>();
			List<Integer> testGroupValueList = new ArrayList<>();
			while ((line = trainBr.readLine()) != null) {
				trainGroupValueList.add(Integer.parseInt(line));
			}

			while ((line = testBr.readLine()) != null) {
				testGroupValueList.add(Integer.parseInt(line));
			}

			int[] trainGroupArr = new int[trainGroupValueList.size()];
			for (int i = 0; i < trainGroupArr.length; i++) {
				trainGroupArr[i] = trainGroupValueList.get(i);
			}
			int[] testGroupArr = new int[testGroupValueList.size()];
			for (int i = 0; i < testGroupArr.length; i++) {
				testGroupArr[i] = testGroupValueList.get(i);
			}

			HashMap<String, Object> params = new HashMap<>();
			params.put("eta", 0.1);
			params.put("gamma", 1.0);
			params.put("min_child_weight", 0.1);
			params.put("max_depth", 6);
			params.put("silent", 1);

			// rank:pairwise  rank:ndcg
			params.put("objective", "rank:pairwise");

			trainMat.setGroup(trainGroupArr);
			testMat.setGroup(testGroupArr);

			HashMap<String, DMatrix> watches = new HashMap<>();
			watches.put("train", trainMat);
			watches.put("test", testMat);

			int round = 4;
			Booster booster = XGBoost.train(trainMat, params, round, watches, null, null);

//          booster.saveModel(modelPath);
//          XGBoost.loadModel(modelPath);
//          XGBoost.loadModel(in);
			// predict

			float[][] predicts = booster.predict(testMat);
			System.out.println("predicts.length: " + predicts.length);
			for (float[] pred : predicts) {
				for (float v : pred) {
					System.out.println(v);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
