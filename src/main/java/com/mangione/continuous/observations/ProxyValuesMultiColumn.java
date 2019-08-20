package com.mangione.continuous.observations;

import java.io.*;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProxyValuesMultiColumn {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyValues.class);

    private HashMap<Integer, BiMap<String, Integer>> biMaps = new HashMap<>();
    private int numCols;
    private int numLevels = 0;

    public static ProxyValuesMultiColumn fromFile(File file) throws IOException {
        return new ProxyValuesMultiColumn(file);
    }

    private void initMaps(RandomAccessFile raf) throws IOException{
        raf.seek(0);
        String line = raf.readLine();
        String[] split = line.split(",");
        this.numCols = split.length;
        raf.getFilePointer();

        int i;
        for(i = 0; i < numCols; i++) {
            this.biMaps.put(i, HashBiMap.create());
        }
    }

	public ProxyValuesMultiColumn(File file) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            initMaps(raf);
            int i;
			for(String line = raf.readLine(); line != null; line = raf.readLine()) {
				String[] values = line.split(",");
				for(i = 0; i < numCols; i++) {
                    try {
                        String level = values[i];
                        if(!contains(i, level)) {
                            addPair(i, level, this.numLevels);
                            this.numLevels++;
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.error("Bad input line: " + line);
                    }
                }
			}
            int index = 0;
            for(i = 0; i < this.biMaps.size(); i++) {
                for (BiMap.Entry<String, Integer> entry : this.biMaps.get(i).entrySet()) {
                    entry.setValue(index);
                    index++;
                }
            }
		}
	}

    public int getNumLevels() {
        return this.numLevels;
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

    @Nonnull
    synchronized public ProxyValuesMultiColumn add(int col, String level) {
        if(!contains(col, level))
            biMaps.get(col).put(level, biMaps.get(col).size());
        return this;
    }

    synchronized public String getLevel(int col, Integer intRepOfLevel) {
        return biMaps.get(col).inverse().get(intRepOfLevel);
    }

    synchronized public Integer getIndex(int col, String level) { return biMaps.get(col).get(level); }

    synchronized public String toString(int col){
        return biMaps.get(col).entrySet().stream()
                .map(entry -> entry.getKey() + ", " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    synchronized public int size(int col) {
        return biMaps.get(col).size();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/adult+stretch.data");
        ProxyValuesMultiColumn pv = new ProxyValuesMultiColumn(file);
        System.out.print(pv.biMaps);
    }

}
