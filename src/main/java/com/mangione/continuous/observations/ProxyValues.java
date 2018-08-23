package com.mangione.continuous.observations;

import java.util.HashMap;

public class ProxyValues {

	private HashMap<Integer, String> mapOfVals1 = new HashMap<>();
	private HashMap<String, Integer> mapOfVals2 = new HashMap<>();

	boolean isNamed(Integer i) {
		return mapOfVals1.containsKey(i);
	}

	boolean isNamed(String i) {
		return mapOfVals2.containsKey(i);
	}

	public void addPair(Integer i, String val) {
		mapOfVals1.put(i, val);
	}

	public void addPair(String i, Integer val) {
		mapOfVals2.put(i, val);
	}

	public String getName(Integer i) {
		return mapOfVals1.get(i);
	}

	public Integer getName(String i) { return mapOfVals2.get(i); }

	@Override
	public String toString(){
		return mapOfVals1.toString() + mapOfVals2.toString();
	}

	public int size() {
		return mapOfVals2.size();
	}
}
