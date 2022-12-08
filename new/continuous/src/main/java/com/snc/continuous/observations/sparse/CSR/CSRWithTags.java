package com.mangione.continuous.observations.sparse.CSR;

import java.util.List;

public class CSRWithTags<TAG> {
	private final int[] columns;
	private final int[] rows;
	private final Double[] values;
	private final List<TAG> tags;

	public CSRWithTags(int[] rows, int[] columns, Double[] values, List<TAG> tags) {
		this.columns = columns;
		this.rows = rows;
		this.values = values;
		this.tags = tags;
	}

	public int[] getColumns() {
		return columns;
	}

	public int[] getRows() {
		return rows;
	}

	public Double[] getValues() {
		return values;
	}

	public List<TAG> getTags() {
		return tags;
	}
}
