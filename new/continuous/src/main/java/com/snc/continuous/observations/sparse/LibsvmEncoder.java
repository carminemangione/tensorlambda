package com.mangione.continuous.observations.sparse;

import java.util.function.Function;

import com.mangione.continuous.observations.ExemplarInterface;

public class LibsvmEncoder<EXEMPLAR extends ExemplarInterface<Integer, Integer>> {
	public String encode(EXEMPLAR exemplar, Function<EXEMPLAR, Integer> tagFunction, Function<EXEMPLAR, String> afterTag) {
		StringBuilder stringBuilder = new StringBuilder();
		Integer tag = tagFunction.apply(exemplar);
		stringBuilder.append(tag != null ? tag : "")
				.append(afterTag.apply(exemplar));

		for (int i = 0; i < exemplar.getColumnIndexes().length; i++)
			stringBuilder
					.append(exemplar.getColumnIndexes()[i])
					.append(":")
					.append(exemplar.getFeature(exemplar.getColumnIndexes()[i]))
					.append(" ");

		return stringBuilder.toString().trim();
	}
}
