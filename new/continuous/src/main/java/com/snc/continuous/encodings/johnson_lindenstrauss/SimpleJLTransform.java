package com.mangione.continuous.encodings.johnson_lindenstrauss;

import java.util.Arrays;

import org.ejml.data.DMatrixD1;
import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

import com.mangione.continuous.encodings.TransformInterface;
import com.mangione.continuous.linalg.matrix.MatrixRelation;
import com.mangione.continuous.linalg.vector.VectorMapRepresentation;
import com.mangione.continuous.observations.sparse.SparseObservation;

public class SimpleJLTransform extends TransformInterface {
    private int inputDimension;
    private final int walshHsdamardDimension;
    private final int outputDimension;
    private final double[] randomDiagonal;
    private final MatrixRelation randomProjection;
    private DMatrixRMaj jlMatrix;
    private SimpleMatrix jlSimpleMatrix;

    public SimpleJLTransform(int inputDimension, int walshHadamardDimension,
                             int k, double[] randomDiagonal, MatrixRelation randomProjection){
        super(inputDimension,k);
        this.inputDimension = inputDimension;
        this.walshHsdamardDimension = walshHadamardDimension;
        this.outputDimension = k;
        this.randomDiagonal = randomDiagonal;
        this.randomProjection = randomProjection;
        this.jlMatrix = new DMatrixRMaj(k,inputDimension);
        setJlMatrixSimple(jlMatrix);
    }

    @Override
    public int getInputDimension(){
        return inputDimension;
    }

    @Override
    public int getOutputDimension(){
        return outputDimension;
    }

    @Override
    public void transform(VectorMapRepresentation vector, double[] output){
        for(int r=0;r<outputDimension;r++){
            double sum=0.0;
            for(int c: vector.getKeys()){
                sum+= jlMatrix.get(r,c)*vector.get(c);
            }
            //output.set(r,sum);
            output[r] = sum;
        }
    }

    @Override
    public void transform(DMatrixD1 vector,double[] output) {
        for(int r=0;r<output.length;r++){
            double sum=0.0;
            for(int c=0;c< vector.numCols;c++){
                sum+=jlMatrix.get(r,c)*vector.get(0,c);
            }
            output[r] = sum;
        }
    }

    @Override
    public void transform(SparseObservation<Integer> input, double[] output) {
        for(int r=0;r<output.length;r++){
            int finalR = r;
            output[r] = Arrays.stream(input
                    .getFeatures(Integer[]::new))
                    .mapToDouble(f->input.getFeature(f)*jlMatrix.get(finalR,f)).sum();
        }
    }

    private void setJlMatrixSimple(DMatrixRMaj jlMatrix) {
        DMatrixRMaj inputMatrix = randomProjection.convert((DMatrixRMaj)null);
        int numCols = inputMatrix.getNumCols();
        for(int r =0;r<inputMatrix.getNumRows();r++){
            FastWalshHadamardTransform.apply_in_place(inputMatrix.getData(), numCols, numCols,r*numCols);
        }
        for(int r=0;r<inputMatrix.getNumRows();r++){
            for(int c=0;c<randomDiagonal.length;c++){
                inputMatrix.set(r,c,inputMatrix.get(r,c)*randomDiagonal[c]);
            }
        }
        SimpleMatrix scaled = new SimpleMatrix(inputMatrix);
        this.jlSimpleMatrix = scaled
                .extractMatrix(0, inputMatrix.getNumRows(),0,getInputDimension());
        this.jlMatrix = this.jlSimpleMatrix.getDDRM();
    }
}
