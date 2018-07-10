package com.mangione.continuous.observations;

import java.util.HashMap;

public class NamedColumns {

	HashMap<Integer, String> mapOfColumns = new HashMap<>();

	public boolean isNamed(int i) {
		return mapOfColumns.containsKey(i);
	}

	public void addColumn(int i, String nameOfColumn) {
		mapOfColumns.put(i, nameOfColumn);
	}

	public String getName(int i) {
		return mapOfColumns.get(i);
	}

	@Override
	public String toString(){
		return mapOfColumns.toString();
	}
}
