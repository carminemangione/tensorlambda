package com.mangione.continuous.linalg.vector;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;

public class VectorMapRepresentation {
	private HashMap<Integer, Double> data = new HashMap<>();

	public HashMap<Integer, Double> getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

	private int size;

	private VectorMapRepresentation(int size, HashMap<Integer, Double> data) {
		this.size = size;
		this.data = data;
	}

	public static VectorMapRepresentation convert(SimpleMatrix input) {
		if (input.numRows() != 1) throw new IllegalArgumentException("Matrix must be a row vector");
		HashMap<Integer, Double> hashMap = new HashMap<>();
		IntStream.range(0, input.numCols()).forEach((i) -> hashMap.put(i, input.get(0, i)));
		return new VectorMapRepresentation(input.numCols(), hashMap);
	}

	public VectorMapRepresentation(int size) {
		this.size = size;
	}

	public static VectorMapRepresentation stdBasis(int element, int dimension) {
		if (0 > element || element >= dimension)
			throw new IllegalArgumentException("Element must be greater than or equal to 0 and less than dimension.");
		VectorMapRepresentation vectorMapRepresentation = new VectorMapRepresentation(dimension);
		vectorMapRepresentation.set(element, 1.0);
		return vectorMapRepresentation;
	}

	public void set(int i, double v) {
		if (!(0 <= i && i < size)) throw new IndexOutOfBoundsException();
		data.put(i, v);
	}

	public void set(DMatrixRMaj input) {
		if (input.getNumRows() != 1) throw new IllegalArgumentException("Input must be row vector");
		if (input.getNumCols() != size)
			throw new IllegalArgumentException("Input number of columns must be equal to size.");
	}

	public double get(int i) {
		if (!(0 <= i && i < size)) throw new IndexOutOfBoundsException();
		return data.getOrDefault(i, 0.0);
	}

	public double dot(double[] a, int offset) {
		double sum = 0.0;
		for (int i : this.data.keySet()) {
			sum += a[offset + i] * get(i);
		}
		return sum;
	}

	public double dot(VectorMapRepresentation other) {
		return this.data.keySet().stream().mapToDouble(key -> get(key) * other.get(key)).sum();
	}

	public void applyDiagonal(double[] a, VectorMapRepresentation other) {
		for (int i : this.data.keySet()) {
			other.set(i, this.data.get(i) * a[i]);
		}
	}

	public void applyLeft(DMatrixRMaj F, DMatrixRMaj other) {
		if (other.getNumRows() != 1 || other.getNumCols() != F.getNumRows()) throw new IllegalArgumentException();
		for (int i = 0; i < other.getNumCols(); i++) {
			double sum = 0.0;
			for (int j : this.data.keySet()) {
				sum += F.get(i, j) * get(j);
			}
			other.set(i, sum);
		}
	}

	public void zero() {
		data.clear();
	}

	public Set<Integer> getKeys() {
		return data.keySet();
	}

	public String toString() {
		return this.data.toString();
	}
}
