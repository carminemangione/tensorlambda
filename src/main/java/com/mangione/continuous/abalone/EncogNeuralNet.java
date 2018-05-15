package com.mangione.continuous.abalone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.encog.ConsoleStatusReportable;
import org.encog.ml.MLRegression;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.simple.EncogUtility;

import com.mangione.continuous.calculators.MinMaxScaling;
import com.mangione.continuous.calculators.stats.ColumnStatsBuilder;
import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.CsvObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.ObservationToExemplarProvider;
import com.mangione.continuous.observationproviders.StringToDoubleVariableCalculator;
import com.mangione.continuous.observationproviders.VariableCalculator;
import com.mangione.continuous.observationproviders.VariableCalculatorObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.StringObservationFactory;
import com.mangione.continuous.thirdparty.encog.MLDataSetProviderDelegate;

public class EncogNeuralNet {
	private final static String DATA_FILENAME = "Abalone/abalone.data";
	private final static String EGA_FILENAME = "src/main/resources/Abalone/abalone.ega";

	private static String[] theInputHeadings = {"Sex", "Shell Length",
			"Shell Diameter", "Shell Height", "Total Abalone Weight",
			"Shucked Weight", "Viscera Weight", "Shell Weight", "Rings"};

	private EncogModel abaloneModel;

	private EncogNeuralNet() throws FileNotFoundException {

		URL resource = EncogNeuralNet.class.getClassLoader().getResource(DATA_FILENAME);
		if (resource == null)
			throw new FileNotFoundException(DATA_FILENAME + " is on on resource path.");
		File inputFile = new File(resource.getFile());
		ObservationProviderInterface<String, ObservationInterface<String>>
				csvObservationProvider = new CsvObservationProvider(inputFile, new StringObservationFactory());


		Map<Integer, VariableCalculator<String, Double>>  calculators = new HashMap<>();
		calculators.put(0, new SexVariableCalculator());


		VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> doubleAndSexMappedVariableCalculator
				= new VariableCalculatorObservationProvider<>(csvObservationProvider,
				new StringToDoubleVariableCalculator(), calculators, new DoubleObservationFactory());

		ObservationToExemplarProvider discreteExemplars = new ObservationToExemplarProvider(doubleAndSexMappedVariableCalculator, new DiscreteExemplarFactory());

		ArrayObservationProvider<Double, DiscreteExemplar<Double>> arrayObservationProvider =
				new ArrayObservationProvider<>(discreteExemplars, new DiscreteExemplarFactory());

		ColumnStatsBuilder columnStatsBuilder = new ColumnStatsBuilder(doubleAndSexMappedVariableCalculator);

		AtomicInteger index = new AtomicInteger();
		Map<Integer, ?  extends VariableCalculator<Double, Double>> columnStatsMap = columnStatsBuilder.getStats()
				.stream()
				.collect(Collectors.toMap(columnStats -> index.getAndIncrement(), MinMaxScaling::new));


		VariableCalculatorObservationProvider<Double, Double, DiscreteExemplar<Double>> minMaxScaledProvider =
				new VariableCalculatorObservationProvider<>(arrayObservationProvider, null,
						columnStatsMap, new DiscreteExemplarFactory());

		MLDataSetProviderDelegate dataSetProviderDelegate = new MLDataSetProviderDelegate(arrayObservationProvider);

		MLRegression bestMethod = trainModel(discreteExemplars, .3, 5, true, 1001);

		System.out.println("Training error: "
				+ EncogUtility.calculateRegressionError(bestMethod,
				abaloneModel.getTrainingDataset()));
		System.out.println("Validation error: "
				+ EncogUtility.calculateRegressionError(bestMethod,
				abaloneModel.getValidationDataset()));

		System.out.println("Final model: " + bestMethod);

	}

	private MLRegression trainModel(ObservationProviderInterface<Double, DiscreteExemplar<Double>> provider,
			double validationFraction, int folds, boolean shuffle, int seed) {

		MLDataSetProviderDelegate mlDataPairs = new MLDataSetProviderDelegate(provider);
		// Define the model and set the training method
		abaloneModel = new EncogModel(mlDataPairs);
		abaloneModel.selectMethod(mlDataPairs, MLMethodFactory.TYPE_NEAT);

		// Send all output to the console
		abaloneModel.setReport(new ConsoleStatusReportable());

		// Hold back 30% of the data for final validation
		abaloneModel.holdBackValidation(validationFraction, shuffle, seed);

		// Select default training type for given dataset
		abaloneModel.selectTrainingType(mlDataPairs);

		// Train with 5-fold cross-validation
		return (MLRegression) abaloneModel.crossvalidate(folds, shuffle);
	}


	public static void main(String[] args) throws IOException {
		EncogNeuralNet nnet = new EncogNeuralNet();


	}
}
