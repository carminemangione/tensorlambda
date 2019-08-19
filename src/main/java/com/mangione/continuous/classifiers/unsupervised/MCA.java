package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValuesMultiColumn;
import com.mangione.continuous.observations.dense.Observation;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.IntStream;

/* implement using provider at some point, make sure to get the file implementation first */
public class MCA<S extends String, T extends ObservationInterface<S>> {
    private ObservationProviderInterface<S,T> provider;
    private ProxyValuesMultiColumn pv;
    private int rank, batchSize, numRows, numCols;
    private DMatrixRMaj U, Sig, V;
    private Boolean firstIteration = true;



    public MCA(File file, int rank, int batchSize, ObservationProviderInterface<S, T> provider) throws IOException{
        this.provider = provider;
        this.batchSize = batchSize;
        this.numCols = provider.getNumberOfColumns();
        this.numRows = (int) provider.getNumberOfLines();
        assert batchSize < this.numRows : "Bro your batchSize cannot be greater than the length of the dataset lol";
        this.rank = rank;
        this.pv = new ProxyValuesMultiColumn(file);
    }

    public void train(){
        Iterator<T> iter = this.provider.iterator();
        DMatrixSparseCSC Z;
        int numBatches = this.numRows / this.batchSize;
        int leftover = this.numRows % this.batchSize;
        int batch = this.batchSize;
        MCAIter mcai = new MCAIter(batch, this.numCols);
        IterSVD isvd = new IterSVD(this.pv.getNumLevels());
        int i = 0;
        while (i <= numBatches) {
            if(i == numBatches) {
                batch = leftover;
            }
            Z = createZ(batch, pv, iter);
            mcai.findAB(Z, firstIteration);
            isvd.svdUpdate(rank, 0, mcai.getA(), mcai.getB());
            firstIteration = false;
            i++;
        }
        this.U = isvd.getU();
        this.Sig = isvd.getSig();
        this.V = isvd.getV();
    }

    public DMatrixRMaj getU() { return U; }

    public DMatrixRMaj getSig() { return Sig; }

    public DMatrixRMaj getV() { return V; }

    private DMatrixSparseCSC createZ(int numRows, ProxyValuesMultiColumn pv, Iterator<T> iter) {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(numRows, pv.getNumLevels());
        T o;
        String b;
        int row, col;
        for(row = 0; row < numRows; row++) {
            o = iter.next();
            for(col = 0; col < this.numCols; col++) {
                Z.set(row, pv.getIndex(col, o.getFeature(col)), 1);
            }
        }
//        IntStream.range(0, numRows)
//                .forEach(row -> IntStream.range(0, this.numCols)
//                        .forEach(col -> Z.set(row, pv.getIndex(col, iter.next()))));
        return Z;


    }
}
