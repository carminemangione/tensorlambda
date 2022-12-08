package com.mangione.continuous.demos.ranklib;

import ciir.umass.edu.learning.Ranker;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class ModelFactory {
    public static final Class<?> superClass = Ranker.class;
    public static final Map<String,Class<?>> classNameToClass = new HashMap<>();
    public static final Map<String, String> classNameToTypeName;
    public static final Map<String, String> typeNameToClassName;
    public static final Map<String,Class<?>> typeNameToClass;

    static {
        Consumer<String> saveClass= s -> {
            try {
                Class<?> c = Class.forName(s);
                classNameToClass.put(s,c);
            } catch (ClassNotFoundException e) {
                //do nothing
            }
        };

        new ClassGraph()
                .enableAllInfo()
                .whitelistPackages(Ranker.class.getPackage().getName())
                .scan().getSubclasses(superClass.getName())
                .stream()
                .map(ClassInfo::getName)
                .forEach(saveClass);


        String[] classNames = {
                "ciir.umass.edu.learning.tree.MART",
                "ciir.umass.edu.learning.boosting.RankBoost",
                "ciir.umass.edu.learning.neuralnet.RankNet",
                "ciir.umass.edu.learning.boosting.AdaRank",
                "ciir.umass.edu.learning.CoorAscent",
                "ciir.umass.edu.learning.neuralnet.LambdaRank",
                "ciir.umass.edu.learning.tree.LambdaMART",
                "ciir.umass.edu.learning.neuralnet.ListNet",
                "ciir.umass.edu.learning.tree.RFRanker",
                "ciir.umass.edu.learning.LinearRegRank"
        };

        String[] typeNames = {
                "MART",
                "RANKBOOST",
                "RANKNET",
                "ADARANK",
                "COOR_ASCENT",
                "LAMBDARANK",
                "LAMBDAMART",
                "LISTNET",
                "RANDOM_FOREST",
                "LINEAR_REGRESSION"
        };
        Function<Integer, String> indexToClaasName = (Integer i) -> classNames[i];
        Function<Integer, String> indexToTypeName = (Integer i) -> typeNames[i];
        classNameToTypeName = range(0,typeNames.length).boxed().collect(Collectors.toMap(indexToClaasName, indexToTypeName));
        typeNameToClassName = range(0,typeNames.length).boxed().collect(Collectors.toMap(indexToTypeName, indexToClaasName));

        typeNameToClass =Arrays.stream(typeNames).collect(Collectors.toMap(
                Function.identity(),
                (String name)->classNameToClass.get(typeNameToClassName.get(name)
                )
        ));
    }

    State state;
    Ranker r;

    public enum State {
        UNTRAINED, TRAINED
    }

    public Ranker getRanker() {
        return r;
    }

    public static void main(String[] args) {
        for (Class<?> c : classNameToClass.values()) {
            System.out.println(">" + c.getName());
        }
    }
}
