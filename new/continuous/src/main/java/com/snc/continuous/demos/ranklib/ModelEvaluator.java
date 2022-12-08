package com.mangione.continuous.demos.ranklib;

import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.utilities.RankLibError;

import java.io.BufferedReader;
import java.io.StringReader;

public class ModelEvaluator extends ModelFactory {

    public ModelEvaluator(final String fullText) {
        try (BufferedReader in = new BufferedReader(new StringReader(fullText))) {
            final String content = in.readLine().replace("## ", "").trim();//read the first line to get the name of the ranking algorithm
            String shortName = classNameToTypeName.get(content.toUpperCase());
            final Ranker r = (Ranker) typeNameToClass.get(shortName).newInstance();
            r.loadFromString(fullText);
            state = State.TRAINED;
        } catch (final Exception ex) {
            throw RankLibError.create(ex);
        }
    }

}
