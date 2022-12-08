package com.mangione.continuous.observations.sparse;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
javax.annotation.Nonnull;

import com.mangione.continuous.observations.ExemplarInterface;

public class SparseExemplar<FEATURE, TARGET> extends SparseObservation<FEATURE> implements ExemplarInterface<FEATURE, TARGET> {
	private final TARGET target;

	public static <TARGET> SparseExemplar<Integer, TARGET> createBinaryExemplar(int[] columns, int numberOfColumns, TARGET target) {
		Integer[] values = createBinaryFeatureArray(columns);
		return new SparseExemplar<>(values, columns, numberOfColumns, 0, target);
	}

	@NotNull
	private static Integer[] createBinaryFeatureArray(int[] columns) {
		return Arrays.stream(columns)
				.boxed()
				.mapToInt(c -> 1)
				.boxed()
				.toArray(Integer[]::new);
	}

	public SparseExemplar(FEATURE[] values, int[] columns, int numberOfColumns, FEATURE missingValue, TARGET target) {
		super(values, columns, numberOfColumns, missingValue);
		this.target = target;
	}

	@Override
	public TARGET getLabel() {
		return target;
	}

	@Override
	public FEATURE getFeature(Integer index) {
		return super.getFeature(index);
	}

	public TARGET getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof SparseExemplar)) return false;

		SparseExemplar<?, ?> that = (SparseExemplar<?, ?>) o;

		return new EqualsBuilder()
				.appendSuper(super.equals(o))
				.append(getLabel(), that.getLabel())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(super.hashCode())
				.append(getLabel())
				.toHashCode();
	}

	@Override
	public String toString() {
		return "SparseExemplar{" +
				"target=" + target +
				"} " + super.toString();
	}
}
