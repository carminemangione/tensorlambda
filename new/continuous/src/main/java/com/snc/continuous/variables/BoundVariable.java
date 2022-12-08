package com.mangione.continuous.variables;

import java.util.function.Function;

public class BoundVariable {
	public Object execute(Function<? super Object, ? super Object> function) {
		Object apply = function.apply(1);
		System.out.println("apply = " + apply);
		return apply;
	}

}
