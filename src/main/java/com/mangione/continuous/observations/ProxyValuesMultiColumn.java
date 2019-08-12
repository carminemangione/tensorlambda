package com.mangione.continuous.observations;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProxyValuesMultiColumn {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyValues.class);

    private HashMap<Integer, BiMap<String, Integer>> biMaps = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> setMap= new HashMap<>();
    private HashMap<Integer, Integer> ctrMap = new HashMap<>();

    public static ProxyValues fromReader(Reader reader) throws IOException {
        return new ProxyValues(reader);
    }

	public ProxyValuesMultiColumn(File file) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
		    raf.seek(0);
			String line = raf.readLine();
			String[] split = line.split(",");
			int lenLine = split.length;
			raf.getFilePointer();

			int i;
			for(i = 0; i < lenLine; i++) {
                biMaps.put(i, HashBiMap.create());
                setMap.put(i, new HashSet<>());
                ctrMap.put(i, 0);
            }
			int ctr = 0;
			for(line = raf.readLine(); line != null; line = raf.readLine()) {
				String[] values = line.split(",");
				for(i = 0; i < lenLine; i++) {
                    try {
                        String level = values[i];
//                        System.out.println(level);
                        if(!contains(i, level)) {

//                            System.out.println(l);
//                            addPair(i, level, l);
                        }
                    } catch (NumberFormatException e) {
//                        System.out.println("heeb");
//                        LOGGER.error("Bad input line: " + line);
                    }
                }

			}
		}
	}

    public ProxyValuesMultiColumn() {
    }

    synchronized boolean contains(int col, Integer intRepOfLevel) {
        return biMaps.get(col).inverse().containsKey(intRepOfLevel);
    }

    synchronized boolean contains(int col, String level) {
        return biMaps.get(col).containsKey(level);
    }

    synchronized public ProxyValuesMultiColumn addPair(int col, Integer key, String val) {
        biMaps.get(col).inverse().put(key, val);
        return this;
    }

    private synchronized void addPair(int col, String key, Integer val) {
        biMaps.get(col).put(key, val);
    }

//    @Nonnull
//    synchronized public ProxyValuesMultiColumn add(int col, String level) {
//        if(!contains(col, level))
//            biMaps.get(col).put(level, biMaps.get(col).size());
//        return this;
//    }

    synchronized public String getIntValOfLevel(int col, Integer intRepOfLevel) {
        return biMaps.get(col).inverse().get(intRepOfLevel);
    }

    synchronized public Integer getLevel(int col, String level) { return biMaps.get(col).get(level); }

//    @Override
//    synchronized public String toString(){
//        return biMap.entrySet().stream()
//                .map(entry -> entry.getKey() + "," + entry.getValue())
//                .collect(Collectors.joining("\n"));
//    }

    synchronized public int size() {
        return biMaps.get(0).size();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/adult+stretch.data");
        ProxyValuesMultiColumn pv = new ProxyValuesMultiColumn(file);
        System.out.println(pv.biMaps.get(0));
    }
}
