package com.mangione.continuous.coerece;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class Coerce {
	public static double[] coerce(List<Double> values) {
		return ArrayUtils.toPrimitive(values.toArray(new Double[values.size()]));
	}
}
