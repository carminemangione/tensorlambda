package com.mangione.continuous.observations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProxyValues {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyValues.class);

	private final BiMap<String, Integer> biMap = HashBiMap.create();

	public static ProxyValues fromReader(Reader reader) throws IOException {
		return new ProxyValues(reader);
	}

	public ProxyValues(Reader reader) throws IOException {
		try (BufferedReader br = new BufferedReader(reader)) {
			while (br.ready()) {
				String line = br.readLine();
				String[] nameAndValue = line.split(",");
				try {
					int index = Integer.parseInt(nameAndValue[1]);
					addPair(nameAndValue[0], index);
				} catch (NumberFormatException e) {
					LOGGER.error("Bad input line: " + line);
				}
			}
		}
	}

	public ProxyValues() {
	}

	synchronized boolean contains(Integer i) {
		return biMap.inverse().containsKey(i);
	}

	synchronized boolean contains(String i) {
		return biMap.containsKey(i);
	}

	synchronized public ProxyValues addPair(Integer i, String val) {
		biMap.inverse().put(i, val);
		return this;
	}

	private synchronized void addPair(String i, Integer val) {
		biMap.put(i, val);
	}

	@Nonnull
	synchronized public ProxyValues add(String i) {
		if(!contains(i))
			biMap.put(i, biMap.size());
		return this;
	}

	synchronized public String getName(Integer i) {
		return biMap.inverse().get(i);
	}

	synchronized public Integer getIndex(String i) { return biMap.get(i); }

	@Override
	synchronized public String toString(){
		return biMap.entrySet().stream()
				.map(entry -> entry.getKey() + "," + entry.getValue())
				.collect(Collectors.joining("\n"));
	}

	synchronized public int size() {
		return biMap.size();
	}
}
