package com.mangione.continuous.abalone;

import com.mangione.continuous.calculators.MinMaxScaling;
import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.calculators.VariableCalculator;
import com.mangione.continuous.calculators.stats.ColumnStatsBuilder;
import com.mangione.continuous.observationproviders.*;
import com.mangione.continuous.observations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AbaloneObservationProviderFactory {
	private final static String DATA_FILENAME = "Abalone/abalone.data";

	private final ObservationProviderInterface<Double, DiscreteExemplar<Double>> abaloneProvider;
	private final VariableCalculations<Double, Double> minMaxScaleCalculations;

	public AbaloneObservationProviderFactory() throws Exception {
		URL resource = EncogNeuralNet.class.getClassLoader().getResource(DATA_FILENAME);
		if (resource == null)
			throw new FileNotFoundException(DATA_FILENAME + " is on on resource path.");
		File inputFile = new File(resource.getFile());
		ObservationProviderInterface<String, ObservationInterface<String>>
				csvObservationProvider = new CsvObservationProvider(inputFile, new StringObservationFactory());


		Map<Integer, VariableCalculator<String, Double>> calculators = new HashMap<>();
		calculators.put(0, new SexVariableCalculator());

		VariableCalculations<String, Double> sexAndDoubleCalculators = new VariableCalculations<>(calculators, new StringToDoubleVariableCalculator());


		VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> doubleAndSexMappedVariableCalculator
				= new VariableCalculatorObservationProvider<>(csvObservationProvider,
				sexAndDoubleCalculators, new DoubleObservationFactory());

		ObservationToExemplarProvider discreteExemplars = new ObservationToExemplarProvider(doubleAndSexMappedVariableCalculator, new DiscreteExemplarFactory());

		ArrayObservationProvider<Double, DiscreteExemplar<Double>> arrayObservationProvider =
				new ArrayObservationProvider<>(discreteExemplars, new DiscreteExemplarFactory());

		ColumnStatsBuilder columnStatsBuilder = new ColumnStatsBuilder(doubleAndSexMappedVariableCalculator);
		Map<Integer, VariableCalculator<Double, Double>> columnStatsMap = MinMaxScaling.toIndexedMap(columnStatsBuilder);
		minMaxScaleCalculations = new VariableCalculations<>(columnStatsMap, null);


		abaloneProvider = new VariableCalculatorObservationProvider<>(arrayObservationProvider, minMaxScaleCalculations,
				new DiscreteExemplarFactory());
	}

	public ObservationProviderInterface<Double, DiscreteExemplar<Double>> getAbaloneProvider() {
		return abaloneProvider;
	}


	public VariableCalculations<Double, Double> getMinMaxScaleCalculations() {
		return minMaxScaleCalculations;
	}
}
