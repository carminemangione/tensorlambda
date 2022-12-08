package com.mangione.continuous.classifiers.unsupervised;

import com.mangione.continuous.classifiers.unsupervised.nearestneighbor.IterSVD;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.encodings.ProxyValuesMultiCategory;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/* Figure out what to do about c */
public class MCA<S extends String, T extends ObservationInterface<S>> {
    private ObservationProviderInterface<S,T> provider;
    private ProxyValuesMultiCategory pv;
    private int rank, batchSize, numRows, numCols;
    private DMatrixRMaj U, Sig, V;
    private Boolean firstIteration = true;



    public MCA(File file, int rank, int batchSize, ObservationProviderInterface<S, T> provider) throws IOException{
        this.provider = provider;
        this.batchSize = batchSize;
        this.numCols = provider.getNumberOfFeatures();
        this.numRows = (int) provider.size();
        assert batchSize < this.numRows : "Batchsize cannot be larger than the number of entries in dataset";
        this.rank = rank;
        this.pv = new ProxyValuesMultiCategory(file);
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

    /* might create class out of this so that I can test it */
    private DMatrixSparseCSC createZ(int numRows, ProxyValuesMultiCategory pv, Iterator<T> iter) {
        DMatrixSparseCSC Z = new DMatrixSparseCSC(numRows, pv.getNumLevels());
        T o;
        int row, col;
        for(row = 0; row < numRows; row++) {
            o = iter.next();
            for(col = 0; col < this.numCols; col++) {
                Z.set(row, pv.getIndex(col, o.getFeature(col)), 1);
            }
        }
        return Z;


    }
}
