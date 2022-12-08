package com.mangione.continuous.encodings;

import com.mangione.continuous.linalg.vector.VectorMapRepresentation;
import com.mangione.continuous.observations.sparse.SparseObservation;
import org.ejml.data.DMatrixD1;
import org.ejml.data.DMatrixRMaj;

import java.util.Arrays;

public abstract class TransformInterface {
    private final int inputDimension;
    private final int outputDimension;

    public TransformInterface(int inputDimension, int outputDimension){
        this.inputDimension = inputDimension;
        this.outputDimension = outputDimension;
    }

    DMatrixD1 createOutput(){
        DMatrixD1 out = new DMatrixRMaj(1,outputDimension);
        Arrays.fill(out.getData(),0.0);
        return out;
    }

    public int getInputDimension() {
        return inputDimension;
    }

    public int getOutputDimension() {
        return outputDimension;
    }

    abstract public void transform(VectorMapRepresentation input, double[] output);

    abstract public void transform(DMatrixD1 input, double[] output);

    abstract public void transform(SparseObservation<Integer> input, double[] output);

    public DMatrixD1 apply(VectorMapRepresentation vector){
        DMatrixD1 out = createOutput();
        transform(vector,out.getData());
        return out;
    }

    public DMatrixD1 apply(DMatrixD1 vector){
        DMatrixD1 out = createOutput();
        transform(vector,out.getData());
        return out;
    }

    public DMatrixD1 apply(SparseObservation<Integer> input){
        DMatrixD1 out = createOutput();
        transform(input,out.getData());
        return  out;
    }
}
