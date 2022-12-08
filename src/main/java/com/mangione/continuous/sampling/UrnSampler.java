package com.mangione.continuous.sampling;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class UrnSampler {
	private final boolean replace;
	private final Random random;
	private final int[] countOfEachColor;
	private int totalNumberOfBalls;

	public UrnSampler(boolean replace, Random random, int... countOfEachColor) {
		this.replace = replace;
		this.random = random;
		this.countOfEachColor = Arrays.copyOf(countOfEachColor, countOfEachColor.length);
		this.totalNumberOfBalls = IntStream.of(countOfEachColor).sum();
	}

	private void remove(int index) {
		if (countOfEachColor[index] > 0) {
			countOfEachColor[index] -= 1;
			totalNumberOfBalls -= 1;
		} else
			throw new IllegalStateException(String.format("Negative totalNumberOfBalls at index %d.", index));
	}

	public boolean hasNext() {
		return totalNumberOfBalls > 0;
	}

	public void pick() {
		double r = random.nextDouble() * this.totalNumberOfBalls;
		int index = findIndexForReplacement(r);

		if (!replace)
			remove(index);
	}

	private int findIndexForReplacement(double r) {
		int index = 0;
		while (r > 0) {
			r -= countOfEachColor[index];
			index++;
		}
		index--;
		return index;
	}

	@Override
	public String toString() {
		return "UrnSampler{" +
				"countOfEachColor=" + Arrays.toString(countOfEachColor) +
				", totalNumberOfBalls=" + totalNumberOfBalls +
				'}';
	}
}
