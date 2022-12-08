package com.mangione.continuous.linalg.matrix;
import org.ejml.data.DMatrixRMaj;

public class MatrixArrayRepresentation {
    private final int rowCount;
    private final int columnCuont;
    private final int[] rows;
    private final int[][] columns;
    private final double[][] values;

    public MatrixArrayRepresentation(int rowCount, int columnCuont,int[] rows, int[][]columns, double[][] values){
        this.rowCount = rowCount;
        this.columnCuont = columnCuont;
        this.rows = rows;
        this.columns = columns;
        this.values = values;
    }

    public DMatrixRMaj convertToDMatrixRMaj(int rowSize, int columnSize){
        DMatrixRMaj out = new DMatrixRMaj(rowSize,columnSize);
        for(int r=0; r<rows.length;r++){
            for(int c=0;c<columns[r].length;c++){
                out.set(rows[r],columns[r][c],values[r][c]);
            }
        }
        return out;
    }

    public int[] getRows() {
        return rows;
    }

    public int[][] getColumns() {
        return columns;
    }

    public double[][] getValues() {
        return values;
    }

    public int maxRow(){
        int candidate =0;
        for(int r: rows){
            candidate = Math.max(candidate,r);
        }
        return candidate;
    }

    public int maxColumn(){
        int candidate = 0;
        for(int[] cA: columns){
            if(cA.length!=0){
                for(int c:cA){
                    candidate = Math.max(candidate,c);
                }
            }
        }
        return candidate;
    }

    public DMatrixRMaj columnLike(){
        return new DMatrixRMaj(1,maxRow());
    }

    public DMatrixRMaj rowLike(){
        return new DMatrixRMaj(1,maxColumn());
    }

    public void apply(DMatrixRMaj input, DMatrixRMaj output){
        for(int r=0;r<rows.length;r++){
            double sum=0.0;
            for(int c=0; c< columns[r].length;c++){
                double v = values[r][c];
                int col = columns[r][c];
                sum+=input.get(0, col)* v;
            }
            int row = rows[r];
            output.set(0, row,sum);
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCuont() {
        return columnCuont;
    }
}
