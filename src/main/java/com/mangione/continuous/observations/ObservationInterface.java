package com.mangione.continuous.observations;

<<<<<<< HEAD
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ObservationInterface<T> {
	List<T> getFeatures();
	List<T> getAllColumns();

	default T getFeature(int index) {
		return getFeatures().get(index);
	}

	default Set<Integer> getColumnIndexes() {
		return IntStream.range(0,numberOfFeatures())
				.boxed()
				.collect(Collectors.toSet());
	}

	default int numberOfFeatures() {
		return getFeatures().size();
	}
=======
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public interface ObservationInterface<FEATURE> {
	FEATURE[] getFeatures(IntFunction<FEATURE[]> featureBuilder);

	FEATURE getFeature(Integer index);

	default int[] getColumnIndexes() {
		return IntStream.range(0,numberOfFeatures())
				.boxed()
				.mapToInt(i->i)
				.toArray();
	}

	int numberOfFeatures();
>>>>>>> 73d9563 (Migrated file changes from the source.)
}
