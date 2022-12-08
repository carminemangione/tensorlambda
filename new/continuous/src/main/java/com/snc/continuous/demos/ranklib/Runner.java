package com.mangione.continuous.demos.ranklib;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.RankerType;
import ciir.umass.edu.learning.tree.LambdaMART;
import ciir.umass.edu.learning.tree.RFRanker;
import ciir.umass.edu.metric.METRIC;
import ciir.umass.edu.metric.MetricScorer;
import ciir.umass.edu.metric.MetricScorerFactory;

import java.util.HashMap;


public class Runner {
    final public static String[] classNames =  {
            "ciir.umass.edu.learning.tree.MART",
            "ciir.umass.edu.learning.boosting.RankBoost",
            "ciir.umass.edu.learning.neuralnet.RankNet",
            "ciir.umass.edu.learning.boosting.AdaRank",
            "ciir.umass.edu.learning.CoorAscent",
            "ciir.umass.edu.learning.neuralnet.LambdaRank",
            "ciir.umass.edu.learning.tree.LambdaMART",
            "ciir.umass.edu.learning.neuralnet.ListNet",
            "ciir.umass.edu.learning.tree.RFRanker",
            "ciir.umass.edu.learning.LinearRegRank"};

    final public static String[] typeNames={
            "MART",
            "RANKBOOST",
            "RANKNET",
            "ADARANK",
            "COOR_ASCENT",
            "LAMBDARANK",
            "LAMBDAMART",
            "LISTNET",
            "RANDOM_FOREST",
            "LINEAR_REGRESSION"};

    static String[] cmd = new String[]
            {
                    "java -jar bin/RankLib.jar -train MQ2008/Fold1/train.txt -test MQ2008/Fold1/test.txt -validate MQ2008/Fold1/vali.txt -ranker 6 -metric2t NDCG@10 -metric2T ERR@10 -save mymodel.txt",
                    "java -cp bin/RankLib.jar ciir.umass.edu.features.FeatureManager -input MQ2008/Fold1/train.txt -output mydata/ -shuffle",
                    "java -jar bin/RankLib.jar -load mymodel.txt -test MQ2008/Fold1/test.txt -metric2T ERR@10"
            };
    public class Builder{
        RankerFactory rankerFactory = new RankerFactory();
        Ranker ranker;
        private MetricScorer trainScorer,testScorer;
        private int k;
        int state = 0;

        public Builder(String rankerModelFile){
            rankerFactory.loadRankerFromFile(rankerModelFile);
            state=2;
        }

        public Builder(RankerType rankerType){
            ranker = rankerFactory.createRanker(rankerType);
        }

        public Builder setTrainScorer(METRIC metric, int k){
            if(state!=0) throw new IllegalStateException();
            this.k = k;
            MetricScorerFactory metricScorerFactory = new MetricScorerFactory();
            trainScorer = metricScorerFactory.createScorer(metric,k);
            testScorer = trainScorer;
            state=1;
            return this;
        }

        public Builder setTestScorer(METRIC testMetric){
            if(state!=1) throw new IllegalStateException();
            MetricScorerFactory metricScorerFactory = new MetricScorerFactory();
            testScorer = metricScorerFactory.createScorer(testMetric,k);
            return this;
        }

        public Builder loadRelevanceJudgment(String qrelFile){
            if(state!=1) throw new IllegalStateException();
            trainScorer.loadExternalRelevanceJudgment(qrelFile);
            if(testScorer != trainScorer){
                testScorer.loadExternalRelevanceJudgment(qrelFile);
            }
            return this;
        }

        public Builder finishScorers(){
            if(state != 1) throw new IllegalStateException();
            state=2;
            return this;
        }
    }

    Evaluator evaluator;
    public static HashMap<String,RankerType> rankerTypeMap = new HashMap<>();
    public static HashMap<String, METRIC> metricMap = new HashMap<>();
    static{
        for(RankerType rt: RankerType.values()){
            rankerTypeMap.put(rt.toString(),rt);
        }
        for(METRIC metric: METRIC.values()){
            metricMap.put(metric.toString(),metric);
        }
    }
    public Runner(final RankerType rt,final METRIC trainMetric, final int trainK, final METRIC testMetric, final int testK, String qrelFile){
        if(qrelFile!=null&&qrelFile.isEmpty()){
            throw new IllegalArgumentException("qrelFile name cannot be empty string");
        }
        RankerFactory rankerFactory = new RankerFactory();
        Ranker ranker = rankerFactory.createRanker(rt);
        MetricScorerFactory metricScorerFactory = new MetricScorerFactory();
        MetricScorer trainScorer = metricScorerFactory.createScorer(trainMetric, trainK);
        metricScorerFactory = new MetricScorerFactory();
        MetricScorer testScorer = metricScorerFactory.createScorer(testMetric, testK);
        if(qrelFile!=null){
            trainScorer.loadExternalRelevanceJudgment(qrelFile);
            testScorer.loadExternalRelevanceJudgment(qrelFile);
        }
    }

    public static void run_from_command_line_args(String[] args){
        Evaluator.main(args);
    }

    public static void main() {
        Evaluator evaluator;

    }
}
