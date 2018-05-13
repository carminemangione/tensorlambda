package com.mangione.continuous.demos.encog.abalone;

import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;

import java.io.*;
import java.util.Arrays;

public class NormalizeAbaloneData {
    private final static String DATA_FILENAME = "src/main/resources/Abalone/abalone.data";
    private final static String DATANECG_FILENAME = "src/main/resources/Abalone/abaloneEGG.data";

    private final static char DATA_FILE_DELIMITER = ',';

    private static String[] theInputHeadings = {"Sex", "Shell Length",
            "Shell Diameter", "Shell Height", "Total Abalone Weight",
            "Shucked Weight", "Viscera Weight", "Shell Weight", "Rings"};

    private static VersatileMLDataSet prepareDataset() {

        // Create data source
        VersatileDataSource abaloneDataSource = new CSVDataSource(new File(
                DATA_FILENAME), false, DATA_FILE_DELIMITER);

        // Create a dataset from the data source
        VersatileMLDataSet abaloneDataset = new VersatileMLDataSet(
                abaloneDataSource);

        // Define sex column
        ColumnDefinition sexColumn = abaloneDataset.defineSourceColumn("Sex",
                0, ColumnType.nominal);
        sexColumn.defineClass(new String[] { "M", "F" });

        // Define continuous columns
        abaloneDataset.defineSourceColumn("Shell Length", 1,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Shell Diameter", 2,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Shell Height", 3,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Total Abalone Weight", 4,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Shucked Weight", 5,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Viscera Weight", 6,
                ColumnType.continuous);
        abaloneDataset.defineSourceColumn("Shell Weight", 7,
                ColumnType.continuous);

        // Define predicted column
        ColumnDefinition numRingsColumn = abaloneDataset.defineSourceColumn(
                "Rings", 8, ColumnType.continuous);

        // Analyze the dataset (calculate sum, mean, and standard deviation)
        abaloneDataset.analyze();

        // Setup dataset with number of rings as the predicted value (which
        // indicates its age in 1.5 year increments)
        abaloneDataset.defineSingleOutputOthersInput(numRingsColumn);
        return abaloneDataset;
    }

    public static void main(String[] args) throws IOException {
        VersatileMLDataSet dataSet = prepareDataset();
        EncogModel abaloneModel = new EncogModel(dataSet);
        abaloneModel.selectMethod(dataSet,MLMethodFactory.TYPE_NEAT);
        dataSet.normalize();
        double[][] data = dataSet.getData();
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DATANECG_FILENAME)));
        Arrays.stream(data)
                .<String[]>map(
                        (row)->Arrays
                                .stream(row)
                                .<String>mapToObj(String::valueOf)
                                .toArray(String[]::new)
                )
                .<String>map(x->String.join(",",x))
                .forEach(out::println);
        out.close();
    }

}
