package com.mangione.continuous.observations;

import java.util.Arrays;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProxyValues {

	private final BiMap<String, Integer> biMap = HashBiMap.create();

	public static ProxyValues fromString(String serializedByMap) {
		return new ProxyValues(serializedByMap);
	}

	private ProxyValues(String serializedBiMap) {
		String stripped = serializedBiMap.replace("{", "").replace("}", "");

		String[] elements = stripped.split(",");
		Arrays.stream(elements)
				.map(keyValue->keyValue.trim().split("="))
				.forEach(keyValue-> biMap.put(keyValue[0], Integer.valueOf(keyValue[1])));
	}

	public ProxyValues() {
	}

	synchronized boolean contains(Integer i) {
		return biMap.inverse().containsKey(i);
	}

	synchronized boolean contains(String i) {
		return biMap.containsKey(i);
	}

	synchronized public void addPair(Integer i, String val) {
		biMap.inverse().put(i, val);
	}

	synchronized public void addPair(String i, Integer val) {
		biMap.put(i, val);
	}

	synchronized public void add(String i) {
		if(!contains(i))
			biMap.put(i, biMap.size());
	}

	synchronized public String getName(Integer i) {
		return biMap.inverse().get(i);
	}

	synchronized public Integer getIndex(String i) { return biMap.get(i); }

	@Override
	synchronized public String toString(){
		return biMap.toString();
	}

	synchronized public int size() {
		return biMap.size();
	}
}
