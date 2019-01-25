package com.mangione.continuous.thirdparty.encog;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataPairCentroid;
import org.encog.util.kmeans.Centroid;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.DiscreteExemplarFactory;

public class MLDataSetProviderDelegate implements MLDataSet {

	private final ArrayObservationProvider<Double, DiscreteExemplar<Double>> provider;
	private final int inputSize;

	public MLDataSetProviderDelegate(@Nonnull ObservationProviderInterface<Double, DiscreteExemplar<Double>> provider) {
		if (!provider.iterator().hasNext()) {
			throw new IllegalArgumentException("Nothing to train on... Provider is empty.");
		}
		if (provider instanceof ArrayObservationProvider)
			this.provider = (ArrayObservationProvider<Double, DiscreteExemplar<Double>>) provider;
		else
			this.provider = new ArrayObservationProvider<>(provider,
					new DiscreteExemplarFactory());

		inputSize = provider.iterator().next().getFeatures().size();
	}


	@Override
	public int getIdealSize() {
		return 1;
	}

	@Override
	public int getInputSize() {
		return inputSize;
	}

	@Override
	public boolean isSupervised() {
		return true;
	}

	@Override
	public long getRecordCount() {
		return provider.getNumberOfLines();
	}

	@Override
	public void getRecord(long index, MLDataPair pair) {
		DiscreteExemplar<Double> exemplar = provider.getByIndex((int) index);
		ExemplarMLDataPair exemplarMLDataPair = new ExemplarMLDataPair(exemplar);
		pair.setIdealArray(exemplarMLDataPair.getIdealArray());
		pair.setInputArray(exemplarMLDataPair.getInputArray());
	}

	@Override
	public MLDataSet openAdditional() {
		return new MLDataSetProviderDelegate(provider);
	}

	@Override
	public void add(MLData data1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(MLData inputData, MLData idealData) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(MLDataPair inputData) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {

	}

	@Override
	public int size() {
		return (int)provider.getNumberOfLines();
	}

	@Override
	public void forEach(Consumer<? super MLDataPair> action) {
		provider.forEach(exemplar -> action.accept(new ExemplarMLDataPair(exemplar)));
	}

	@Override
	public Spliterator<MLDataPair> spliterator() {
		throw new UnsupportedOperationException("Spliterator not supported");
	}

	@Override
	public MLDataPair get(int index) {
		return null;
	}

	@Override
	@Nonnull
	public Iterator<MLDataPair> iterator() {
		return new Iterator<MLDataPair>() {
			private Iterator<DiscreteExemplar<Double>> iterator =  provider.iterator();
			@Override
			public boolean hasNext() {
				return iterator().hasNext();
			}

			@Override
			public MLDataPair next() {
				return new ExemplarMLDataPair(iterator.next());
			}
		};
	}


	private class ExemplarMLDataPair implements MLDataPair {

		private final DiscreteExemplar<Double> exemplar;
		private double significance;

		private ExemplarMLDataPair(DiscreteExemplar<Double> exemplar) {
			this.exemplar = exemplar;
		}

		@Override
		public double[] getIdealArray() {
			return new double[]{exemplar.getContinuousValue()};
		}

		@Override
		public double[] getInputArray() {
			return exemplar.getFeatures().stream().mapToDouble(Double::doubleValue).toArray();
		}

		@Override
		public void setIdealArray(double[] data) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInputArray(double[] data) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isSupervised() {
			return true;
		}

		@Override
		public MLData getIdeal() {
			return new BasicMLData(getIdealArray());
		}

		@Override
		public MLData getInput() {
			return new BasicMLData(getInputArray());
		}

		@Override
		public double getSignificance() {
			return significance;
		}

		@Override
		public void setSignificance(double s) {
			significance = s;
		}

		@Override
		public Centroid<MLDataPair> createCentroid() {
			return new BasicMLDataPairCentroid(new BasicMLDataPair(getInput(), getIdeal()));
		}
	}
}
