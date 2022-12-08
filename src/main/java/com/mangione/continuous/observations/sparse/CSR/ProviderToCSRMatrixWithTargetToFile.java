package com.mangione.continuous.observations.sparse.CSR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

public class ProviderToCSRMatrixWithTargetToFile<FEATURE extends Number, TAG_TYPE extends Number,
        EXEMPLAR extends ExemplarInterface<FEATURE, TAG_TYPE>> extends ProviderToCSRMatrixToFile<FEATURE, EXEMPLAR> {

    private final BufferedWriter targetWriter;
    private final Function<EXEMPLAR, TAG_TYPE> targetFunction;
    private boolean firstRow = true;

    public ProviderToCSRMatrixWithTargetToFile(ObservationProviderInterface<FEATURE, EXEMPLAR> provider,
            File columns, File rows, File values, File targets, Function<EXEMPLAR, TAG_TYPE> targetFunction) throws IOException {
        super(provider, columns, rows, values);
        targetWriter = new BufferedWriter(new FileWriter(targets));
        this.targetFunction = targetFunction;
    }

    @Override
    public void process() throws IOException {
        super.process();
        targetWriter.newLine();
        targetWriter.close();
    }

    @Override
    protected void processNextRow(EXEMPLAR exemplar) {
        super.processNextRow(exemplar);
        try {
            if (!firstRow)
                targetWriter.write(",");
            else
                firstRow = false;
            targetWriter.write(targetFunction.apply(exemplar).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
