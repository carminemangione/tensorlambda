package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observations.ExemplarInterface;

public interface SparseExemplarFactoryInterface<FEATURE extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, FEATURE>> {
	EXEMPLAR create(Integer[] features, int[] columns,
			int numberOfColumns, EXEMPLAR original);
}
