package com.mangione.continuous.performance;

import java.util.List;

public interface ROCInterface<POINT extends ROCPointInterface> {
	List<POINT> getROC();
}
