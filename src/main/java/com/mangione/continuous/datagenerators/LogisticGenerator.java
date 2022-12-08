package com.mangione.continuous.datagenerators;

import com.opencsv.CSVWriter;
import com.mangione.continuous.encodings.random.FastRareBinomial;
import com.mangione.continuous.encodings.random.WRSampler;

import org.ejml.data.DMatrixRMaj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogisticGenerator {
	private final DMatrixRMaj weights;
	private final double logisticBias;
	private final double sigma;
	private final FastRareBinomial fastRareBinomial;
	private final WRSampler wrSampler;
	private final Random random;
	private final DMatrixRMaj values;


	double getLogistic() {
		double u = random.nextDouble();
		return Math.log(u) - Math.log(1 - u);
	}

	double getStandardNormal() {
		return random.nextGaussian();
	}

	int[] getPicks() {
		int k = fastRareBinomial.next();
		return wrSampler.next(k);
	}

	void setRowToSparse(DMatrixRMaj values) {
		int[] choices = wrSampler.next(fastRareBinomial.next());
		values.zero();
		for (int c : choices) values.set(0, c, 1.0);
	}

	public static DMatrixRMaj createWeights01(int k, int n, Random random) {
		DMatrixRMaj out = new DMatrixRMaj(1, n);
		out.zero();
		WRSampler sampler = new WRSampler(k, n, random);
		for (int c : sampler.next()) {
			out.set(0, c, 1.0);
		}
		return out;
	}

	void setRowToSparseGaussian(DMatrixRMaj values) {
		int[] choices = wrSampler.next(fastRareBinomial.next());
		values.zero();
		for (int c : choices) values.set(0, c, getStandardNormal() * sigma);
	}

	public LogisticGenerator(DMatrixRMaj weights, double logisticBias, double p, double sigma, Random random) {
		this.weights = weights;
		this.logisticBias = logisticBias;
		this.sigma = sigma;
		this.random = random;
		values = weights.createLike();
		fastRareBinomial = new FastRareBinomial(p, weights.getNumCols(), random);
		wrSampler = new WRSampler(0, weights.getNumCols(), random);
	}

	public List<String> getNames(String prefix) {
		ArrayList<String> names = new ArrayList<>();
		IntStream.range(0, weights.getNumCols() + 1).forEach((x) -> names.add(prefix + x));
		return names;
	}

	public List<Double> getWeights() {
		ArrayList<Double> values = new ArrayList<>();
		values.add(logisticBias);
		IntStream.range(0, weights.getNumCols()).forEach((c) -> values.add(weights.get(0, c)));
		return values;
	}

	private double[] getSample() {
		double[] sample = new double[weights.getNumCols() + 1];
		double y = 0.0;

		setRowToSparseGaussian(values);
		for (int c = 0; c < weights.getNumCols(); c++)
			y += weights.get(0, c) * values.get(0, c);
		y += getLogistic() + logisticBias;
		double f = (y < 0.0) ? 1.0 : 0.0;

		for (int c = 0; c < values.getNumCols(); c++) {
			sample[c] = values.get(0, c);
		}
		sample[values.getNumCols()] = f;
		return sample;
	}

	public static void main(String[] args) {
		String dictionaryFileName = "/Users/carmine.mangione/logisticDictionary.csv";
		String dataFileName = "/Users/carmine.mangione/logisticData.csv";
		Random random = new Random(1305385);
		DMatrixRMaj weights = createWeights01(3, 8, random);
		LogisticGenerator lg = new LogisticGenerator(weights, -0.75, 0.25, 1.5, random);
		List<String[]> dictionary = lg.exportDictionary();
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(dictionaryFileName));
			writer.writeAll(dictionary);
			writer.close();

			BufferedWriter bw = new BufferedWriter(new FileWriter(dataFileName));
			for (int i = 0; i < 10000; i++) {
				bw.write(Arrays.stream(lg.getSample())
						.boxed()
						.map(x->Double.toString(x))
						.collect(Collectors.joining(",")));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String[]> exportDictionary() {
		List<String> names = getNames("x");
		List<Double> values = getWeights();
		List<String[]> data = new LinkedList<>();
		for (int r = 0; r < names.size(); r++) {
			String[] row = {names.get(r), Double.toString(values.get(r))};
			data.add(row);
		}
		return data;
	}
}
