package com.mangione.continuous.encodings.hashing;

import java.util.function.Function;

public class ParityAsSignum implements Function<Integer, Integer> {
	@Override
	public Integer apply(Integer i) {
		return  (Integer.remainderUnsigned(i, 2) == 0) ? -1 : +1;
	}
}
