package com.mangione.continuous.demos.encog.abalone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.MLRegression;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import com.mangione.continuous.demos.encog.ColumnDefinitionFunction;
import com.mangione.continuous.demos.encog.ColumnDefinitionFunctionFactory;
import com.mangione.continuous.demos.encog.NamedVersatileDataSource;
import com.mangione.continuous.observationproviders.CsvObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;

public class NNet {
    private final static String DATA_FILENAME = "src/main/resources/Abalone/abalone.data";
    private final static String EGA_FILENAME = "src/main/resources/Abalone/abalone.ega";
    private final static char DATA_FILE_DELIMITER = ',';

    private static String[] theInputHeadings = {"Sex", "Shell Length",
            "Shell Diameter", "Shell Height", "Total Abalone Weight",
            "Shucked Weight", "Viscera Weight", "Shell Weight", "Rings"};
    private final String target;

    public NNet(NamedVersatileDataSource namedVersatileDataSource, String target) throws FileNotFoundException {
        this.target = target;
        // Create ColumnDefinitions
        HashMap<String, ColumnDefinitionFunction> cdfMap
                = createColumnDefinitionFunctionMap();
        HashMap<String, ColumnDefinition> cdMap = new HashMap<>();
        // Create a dataset from the data source
        VersatileMLDataSet preparedDataSet = new VersatileMLDataSet(
                namedVersatileDataSource);
        cdfMap.forEach((key, value) -> cdMap.put(key, cdfMap.get(key).apply(key, preparedDataSet)));

        // Analyze the dataset (calculate sum, mean, and standard deviation)
        preparedDataSet.analyze();

        // Setup dataset with number of rings as the predicted value (which
        // indicates its age in 1.5 year increments)
        ColumnDefinition targetColumn = cdMap.get(target);
        // Prep the abalone model
        MLRegression abaloneModel = trainModel(preparedDataSet,
                targetColumn,
                0.3,
                5, true,
                1001
        );
        NeuralNetwork.ScoreRecords(abaloneModel,preparedDataSet.getNormHelper());
    }

    private static MLRegression trainModel(VersatileMLDataSet dataset,
                                           ColumnDefinition targetColumn,
                                           double validationFraction,
                                           int folds, boolean shuffle,
                                           int seed)

    {

        dataset.defineSingleOutputOthersInput(targetColumn);
        // Define the model and set the training method
        EncogModel abaloneModel = new EncogModel(dataset);
        abaloneModel.selectMethod(dataset,MLMethodFactory.TYPE_NEAT);

        // Normalize the data based on the selected model
        dataset.normalize();

        // Send all output to the console
        abaloneModel.setReport(new ConsoleStatusReportable());

        // Hold back 30% of the data for final validation
        abaloneModel.holdBackValidation(validationFraction, shuffle, seed);

        // Select default training type for given dataset
        abaloneModel.selectTrainingType(dataset);

        // Train with 5-fold cross-validation
        return (MLRegression) abaloneModel.crossvalidate(folds, shuffle);
    }


    private static HashMap<String, ColumnDefinitionFunction> createColumnDefinitionFunctionMap() {
        HashMap<String, ColumnDefinitionFunction> map = new HashMap<>();
        for (String name : theInputHeadings) {
            ColumnDefinitionFunction def;
            if (name.equals("Sex")) {
                def = ColumnDefinitionFunctionFactory.createNamed(new String[]{"M", "F"});
            } else {
                def = ColumnDefinitionFunctionFactory.createContinuous();
            }
            map.put(name, def);
        }
        return map;
    }

    private static HashMap<String, Integer> createColumnNmberMap(String[] headers) {
        HashMap<String, Integer> columnNumber = new HashMap<>();
        IntStream.range(0, theInputHeadings.length)
                .forEach((column) -> columnNumber.put(headers[column], column));
        return columnNumber;
    }

    private static NamedVersatileDataSource getVersatileDataSource(final CsvObservationProvider csvObservationProvider,
                                                                   final String[] columnNames) {

        final HashMap<String, Integer> columnNumber = createColumnNmberMap(columnNames);
        return new NamedVersatileDataSource() {
            Iterator<ObservationInterface<String>> iterator = csvObservationProvider.iterator();
            @Override
            public String[] columnNames() {
                return columnNames;
            }

            @Override
            public String[] readLine() {
                if (iterator.hasNext()) {
                    ObservationInterface<String> next = iterator.next();
                    return next.getFeatures();
                } else {
                    return null;
                }
            }

            @Override
            public void rewind() {
                iterator = csvObservationProvider.iterator();
            }

            @Override
            public int columnIndex(String name) {
                Integer index = columnNumber.get(name);
                if (index == null) {
                    throw new IllegalArgumentException(String.format("column[%s] is null.", name));
                }
                return index;
            }
        };
    }

    private static BasicNetwork createMultilayerNetwork(int... nodes) {
        OptionalInt minNodes = Arrays.stream(nodes).min();
        if (!minNodes.isPresent()) {
            throw new IllegalArgumentException("Array of node counts per layer is empty.");
        } else {
            int first = 0;
            int last = nodes.length - 1;
            //Add input layers.
            if (nodes.length < 3) {
                throw new IllegalArgumentException("Network must have 3 or more layers.");
            }
            if (minNodes.getAsInt() < 1) {
                throw new IllegalArgumentException("Network must have positive node counts.");
            }
            if (nodes[first] < nodes[last]) {
                throw new IllegalArgumentException("Number of input nodes must exceed the number of output nodes");
            }

            BasicNetwork network = new BasicNetwork();

            //Add layers.
            Arrays.stream(nodes, first + 1, last)
                    .mapToObj(i -> new BasicLayer(i == first ? null : new ActivationTANH(), i != last, nodes[i]))
                    .forEach(network::addLayer);
            network.getStructure().finalizeStructure();
            network.reset();

            return network;
        }
    }


    public static void main(String[] args) throws IOException {
        File file = new File(DATA_FILENAME);
        final CsvObservationProvider csvObservationProvider = new CsvObservationProvider(file);
        NNet nnet = new NNet(getVersatileDataSource(csvObservationProvider, theInputHeadings), "Rings");
    }
}
