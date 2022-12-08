package com.mangione.continuous.observations.sparse.CSR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ProviderToCSRMatrixToFile<FEATURE extends Number, OBSERVATION extends ObservationInterface<FEATURE>>
        extends AbstractProviderToCSRMatrix<FEATURE, OBSERVATION> {

    private final BufferedWriter columnWriter;
    private final BufferedWriter rowWriter;
    private final BufferedWriter valueWriter;
    private boolean firstLine = true;

    public ProviderToCSRMatrixToFile(ObservationProviderInterface<FEATURE, OBSERVATION> provider,
            File columns, File rows, File values) throws IOException {
        super(provider);
        columnWriter = new BufferedWriter(new FileWriter(columns));
        rowWriter = new BufferedWriter(new FileWriter(rows));
        valueWriter = new BufferedWriter(new FileWriter(values));
    }

    @Override
    protected void finish() {
        try {
            columnWriter.close();
            rowWriter.close();
            valueWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void processNextRow(int[] columnIndexes, int rowIndex, Double[] values) {
        try {
            if (!firstLine) {
                columnWriter.write(",");
                rowWriter.write(",");
                valueWriter.write(",");
            } else {
                firstLine = false;
                rowWriter.write("0,");
            }
            columnWriter.write(Arrays.stream(columnIndexes)
                    .boxed()
                    .map(Object::toString)
                    .collect(Collectors.joining(",")));
            rowWriter.write("" + rowIndex);
            String collect = Arrays.stream(values)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            valueWriter.write(collect);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
