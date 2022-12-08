package com.mangione.continuous.linalg.matrix;

import java.util.Set;
import java.util.TreeMap;

public class MatrixMapRepresentation extends TreeMap<Integer,TreeMap<Integer,Double>> {
    private final int rowCount;
    private final int columnCount;
    public MatrixMapRepresentation(int rowCount, int columnCount){
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void set(int r, int c, double v){
        if(!containsKey(r)){
            put(r,new TreeMap<>());
        }
        TreeMap<Integer, Double> row = get(r);
        row.put(c,v);
    }

    public double get(int r, int c){
        if(!containsKey(r))return 0.0;
        TreeMap<Integer, Double> row = get(r);
        if(!row.containsKey(c))return 0.0;
        return row.get(c);
    }

    @Override
    public Set<Integer> keySet() {
        return super.keySet();
    }

    public MatrixArrayRepresentation arrayRepresentation(){
        int[] rows = keySet().stream().mapToInt(Integer::valueOf).toArray();
        int[][] columns = new int[rows.length][];
        double[][] values = new double[rows.length][];
        int index = 0;
        for (int row : rows) {
            TreeMap<Integer, Double> localMap = get(row);
            columns[index] = localMap.keySet().stream().mapToInt(Integer::valueOf).toArray();
            values[index] = localMap.keySet().stream().map(localMap::get).mapToDouble(Double::valueOf).toArray();
            index++;
        }
        return new MatrixArrayRepresentation(rowCount, columnCount,rows, columns, values);
    }
}
