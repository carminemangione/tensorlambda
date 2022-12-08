package com.mangione.continuous.util.coersion;

import java.util.List;
import java.util.stream.IntStream;

public class CoerceToFloatArray {
	public static float[] coerce(List<? extends Number> values) {
		float[] floats = new float[values.size()];
		IntStream.range(0, values.size())
				.boxed()
				.forEach(i->floats[i] = values.get(i).floatValue());
		return floats;
	}
}
