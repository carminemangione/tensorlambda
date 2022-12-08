package com.mangione.continuous.demos.ranklib;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.metric.MetricScorer;
import java.util.List;


public class ModelTrainer extends ModelFactory {
    Evaluator evaluator;

    String qrelFile = "";

    public ModelTrainer(final String shortClassName,
                        final List<RankList> trainList,
                        final List<RankList> validationList,
                        final int[] features,
                        final MetricScorer scorer)
            throws IllegalAccessException, InstantiationException {
        r = (Ranker) ModelTrainer.typeNameToClass.get(shortClassName).newInstance();
        r.setTrainingSet(trainList);
        r.setValidationSet(validationList);
        r.setFeatures(features);
        r.setMetricScorer(scorer);
        state=State.UNTRAINED;
    }

    public ModelTrainer(final String shortClassName,
                        final List<RankList> trainList,
                        final int[] features,
                        final MetricScorer scorer)
            throws IllegalAccessException, InstantiationException {
        r = (Ranker) ModelTrainer.typeNameToClass.get(shortClassName).newInstance();
        r.setTrainingSet(trainList);
        r.setFeatures(features);
        r.setMetricScorer(scorer);
        state=State.UNTRAINED;
    }

    public void setQrelFile(String qrelFile){
        this.qrelFile = qrelFile;
    }

}
