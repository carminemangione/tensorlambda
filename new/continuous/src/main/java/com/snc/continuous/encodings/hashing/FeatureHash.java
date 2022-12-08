package com.mangione.continuous.encodings.hashing;

import com.mangione.continuous.encodings.TransformInterface;
import com.mangione.continuous.linalg.vector.VectorMapRepresentation;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.sparse.SparseObservation;

import org.ejml.data.DMatrixD1;

import java.util.Arrays;
import java.util.function.Function;

public class FeatureHash extends TransformInterface {
	private final int numberOfInputCells;
	private final int numberOfOutputCells;
	private final Function<Integer, Integer> index;
	private final Function<Integer, Integer> weight;

	public FeatureHash(int numberOfInputCells, int numberOfOutputCells, int indexSeed, int weightSeed) {
		super(numberOfInputCells, numberOfOutputCells);
		this.numberOfInputCells = numberOfInputCells;
		this.numberOfOutputCells = numberOfOutputCells;
		Function<Integer, Integer> h = new IntegerHash(indexSeed);
		Function<Integer, Integer> g = new Modulo(numberOfOutputCells);
		index = g.compose(h);
		h = new IntegerHash(weightSeed);
		g = new ParityAsSignum();
		weight = g.compose(h);
	}

	public int[] hash(int[] featureSet) {
		int[] hash = new int[numberOfOutputCells];
		for (int feature : featureSet) {
			updateHash(feature, hash);
		}
		return hash;
	}

	public int[] hash(ObservationInterface<Integer> featureSet) {
		int[] hash = new int[numberOfOutputCells];
		Arrays.stream(featureSet.getColumnIndexes())
				.forEach(i->updateHash(i, hash));
		return hash;
	}

	private void updateHash(Number feature, int[] hash) {
		hash[index.apply(feature.intValue())] += weight.apply(feature.intValue());
	}

	private void updateHash(int feature, double featureValue, double[] hash) {
		hash[index.apply(feature)] += weight.apply(feature) * featureValue;
	}

	@Override
	public int getInputDimension() {
		return numberOfInputCells;
	}

	@Override
	public int getOutputDimension() {
		return numberOfOutputCells;
	}

	@Override
	public void transform(SparseObservation<Integer> observation, double[] output) {
		Arrays.stream(observation.getColumnIndexes())
				.forEach(feature -> updateHash(feature, observation.getFeature(feature), output));
	}

	@Override
	public void transform(VectorMapRepresentation vector, double[] output) {
		vector.getKeys()
				.forEach(feature -> updateHash(feature, vector.get(feature), output));
	}

	@Override
	public void transform(DMatrixD1 vector, double[] output) {
		for (int i = 0; i < vector.getNumCols(); i++) {
			updateHash(i, vector.get(i), output);
		}
	}
}
