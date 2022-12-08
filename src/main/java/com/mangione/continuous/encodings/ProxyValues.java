package com.mangione.continuous.encodings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProxyValues {

	private final Map<Integer, String> indexToName = new HashMap<>();
	private final Map<String, Integer> nameToIndex = new HashMap<>();

	public static ProxyValues fromReader(Reader reader)  {
		return new Gson().fromJson(reader, ProxyValues.class);
	}

	public static ProxyValues fromFile(File proxyFile) throws IOException {
		try (Reader reader = new FileReader(proxyFile)) {
			return fromReader(reader);
		}
	}

	public synchronized boolean contains(Integer index) {
		return indexToName.containsKey(index);
	}

	public synchronized boolean contains(String name) {
		return nameToIndex.containsKey(name);
	}

	public synchronized void addPair(Integer index, String name) {
		nameToIndex.put(name, index);
		indexToName.put(index, name);
	}

	private synchronized void addPair(String name, Integer index) {
		addPair(index, name);
	}

	@Nonnull
	public synchronized ProxyValues add(String name) {
		if(!contains(name))
			addPair(name, nameToIndex.size());
		return this;
	}
		
	public synchronized String getName(Integer index) {
		return indexToName.getOrDefault(index, "Unknown");
	}

	public synchronized Integer getIndex(String name) {
		return nameToIndex.get(name);
	}

	public synchronized Integer getIndexOrAdd(String name) {
		Integer index;
		if (nameToIndex.containsKey(name))
			index = nameToIndex.get(name);
		else
			index = add(name).getIndex(name);
		return index;
	}

	public synchronized int size() {
		return nameToIndex.size();
	}

	@Override
	public String toString() {
		return toJson();
	}

	private synchronized String toJson() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}

