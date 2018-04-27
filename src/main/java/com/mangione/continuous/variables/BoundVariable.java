package com.mangione.continuous.variables;

import java.util.function.Function;

public class BoundVariable {

	public Object execute(Function<? super Object, ? super Object> function) {
		return function.apply(new Integer(1));
	}

}
