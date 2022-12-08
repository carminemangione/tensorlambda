package com.mangione.continuous.linalg.matrix;

import java.util.*;
import java.util.stream.IntStream;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.ops.ConvertDMatrixStruct;


public class MatrixRelation {
    private final int rowCount;
    private final int columnCount;
    List<Triplet> entries = new ArrayList<>();

    public MatrixRelation(int rows, int columns) {
        this.rowCount = rows;
        this.columnCount = columns;
    }

    public void checkBounds(){
        for(Triplet t: entries){
            if(t.row>=rowCount)
                throw new IllegalStateException(String.format("row %d is greater than or equal to row bound  %d",t.row,rowCount));
            if(t.column>=columnCount)
                throw new IllegalStateException(String.format("column %d is greater than or equal to column bound  %d",t.column,columnCount));
        }
    }

    public MatrixRelation(int rowCount, int columnCount, TreeMap<Integer, Integer> map) {
        if(map.size()==0)throw new IllegalStateException("Empty map");
        this.rowCount = map.values().stream().max(Integer::compareTo).get();
        this.columnCount = map.values().stream().max(Integer::compareTo).get();
        for (Integer row : map.keySet()) {
            add(row, map.get(row), 1.0);
        }
    }

    public void add(int row, int column, double value) {
        entries.add(new Triplet(row, column, value));
    }

    public TreeSet<Integer> rowSet() {
        TreeSet<Integer> out = new TreeSet<>();
        getEntries().stream().map(Triplet::getRow).sorted().distinct().forEach(out::add);
        return out;
    }

    public TreeSet<Integer> columnSet() {
        TreeSet<Integer> out = new TreeSet<>();
        getEntries().stream().map(Triplet::getColumn).sorted().distinct().forEach(out::add);
        return out;
    }

    public static int[][] createGatherPair(TreeSet<Integer> sparseSet){
        int[] sparse = sparseSet.stream().mapToInt(Integer::intValue).toArray();
        int[] index = IntStream.range(0,sparse.length).toArray();
        return new int[][]{index,sparse};
    }

    public static TreeMap<Integer,Integer> createGatherMap(TreeSet<Integer> sparseSet){
        int[][] pair = createGatherPair(sparseSet);
        TreeMap<Integer,Integer> out = new TreeMap<>();
        IntStream.of(pair[0]).forEach((i)->out.put(pair[1][i],i));
        return out;
    }

    public static MatrixRelation createGatherRelation(int sparseBound, TreeSet<Integer> sparseSet){
        int[][] pair = createGatherPair(sparseSet);
        MatrixRelation out = new MatrixRelation(pair[0].length,sparseBound);
        IntStream.of(pair[0]).forEach((i)->out.add(i, pair[1][i],1.0));
        return out;
    }


    public MatrixRelation reMapColumns(TreeMap<Integer, Integer> columnMap) {
        if(columnMap.size()==0)throw new IllegalArgumentException();
        MatrixRelation out = new MatrixRelation(rowCount,columnMap.values().stream().max(Integer::compareTo).get()+1);
        for (Triplet t : getEntries()) {
            int row = t.getRow();
            int columnRaw = t.getColumn();
            int column = columnMap.get(columnRaw);
            double value = t.getValue();
            out.add(row, column, value);
        }
        return out;
    }

    public MatrixRelation reMapRows(TreeMap<Integer,Integer> rowMap){
        if(rowMap.size()==0)throw new IllegalArgumentException();
        MatrixRelation out = new MatrixRelation(rowMap.values().stream().max(Integer::compareTo).get(),columnCount);
        for (Triplet t : getEntries()) {
            int row = rowMap.get(t.getRow());
            int column = t.getColumn();
            double value = t.getValue();
            out.add(row, column, value);
        }
        return out;

    }

    public MatrixRelation transpose() {
        MatrixRelation tripletList = new MatrixRelation(columnCount, rowCount);
        for (Triplet s : getEntries()) {
            tripletList.add(s.column, s.row, s.value);
        }
        return tripletList;
    }

    public MatrixMapRepresentation mapRepresentation(){
        MatrixMapRepresentation out = new MatrixMapRepresentation(rowCount,columnCount);
        for(Triplet t: getEntries()){
            out.set(t.row,t.column,t.value);
        }
        return out;
    }

    public DMatrixSparseTriplet convert(DMatrixSparseTriplet ignored) {
        DMatrixSparseTriplet matrixSparseTriplet = new DMatrixSparseTriplet(rowCount,columnCount, entries.size());
        for (Triplet e : getEntries()) {
            matrixSparseTriplet.addItem(e.row, e.column, e.value);
        }
        return matrixSparseTriplet;
    }

    public DMatrixSparseCSC convert(DMatrixSparseCSC ignored) {
        DMatrixSparseTriplet t = convert((DMatrixSparseTriplet) null);
        return ConvertDMatrixStruct.convert(t, (DMatrixSparseCSC) null);
    }

    public DMatrixRMaj convert(DMatrixRMaj ignored) {
        DMatrixRMaj out = new DMatrixRMaj(rowCount, columnCount);
        for (Triplet t : getEntries()) {
            out.set(t.row, t.column, t.value);
        }
        return out;
    }

    public TreeMap<Integer, TreeMap<Integer, Double>> rowMajorMap() {
        TreeMap<Integer, TreeMap<Integer, Double>> out = new TreeMap<>();
        for (Triplet entry : getEntries()) {
            int row = entry.getRow();
            int column = entry.getColumn();
            double value = entry.getValue();
            if (!out.containsKey(row)) {
                out.put(row, new TreeMap<>());
            }
            out.get(row).put(column, value);
        }
        return out;
    }

    public List<Triplet> getEntries() {
        checkBounds();
        return entries;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    static public class Triplet {
        private final int row;
        private final int column;
        private final double value;

        public Triplet(int row, int column, double value) {

            this.row = row;
            this.column = column;
            this.value = value;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public double getValue() {
            return value;
        }

        public String toString() {
            return '<' + Integer.toString(row) + ':' + Integer.toString(column) + ':' + Double.toString(value) + '>';
        }
    }

}
