package com.mangione.continuous.observations;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProxyValues {

	private BiMap<String, Integer> biMap = HashBiMap.create();

	boolean contains(Integer i) {
		return biMap.inverse().containsKey(i);
	}

	boolean contains(String i) {
		return biMap.containsKey(i);
	}

	public void addPair(Integer i, String val) {
		biMap.inverse().put(i, val);
	}

	public void addPair(String i, Integer val) {
		biMap.put(i, val);
	}

	public void add(String i) {
		if(!contains(i))
			biMap.put(i, biMap.size());
	}

	public String getName(Integer i) {
		return biMap.inverse().get(i);
	}

	public Integer getName(String i) { return biMap.get(i); }

	@Override
	public String toString(){
		return biMap.toString();
	}

	public int size() {
		return biMap.size();
	}
}
