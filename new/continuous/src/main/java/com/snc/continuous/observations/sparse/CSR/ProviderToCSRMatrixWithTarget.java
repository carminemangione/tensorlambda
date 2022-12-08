package com.mangione.continuous.observations.sparse.CSR;

import static com.mangione.continuous.util.coersion.CoerceToIntArray.coerce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.util.LoggingTimer;

public class ProviderToCSRMatrixWithTarget<FEATURE extends Number, TAG extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, TAG>>
		extends AbstractProviderToCSRMatrix<FEATURE, EXEMPLAR> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderToCSRMatrixWithTarget.class);
	private static final LoggingTimer LOGGING_TIMER = new LoggingTimer(LOGGER, 1000, "Creating CSR");
	private final List<Integer> columnIndexList = new ArrayList<>();
	private final List<Integer> rowIndexes = new ArrayList<>();
	private final List<Double> valuesList = new ArrayList<>();
	private final List<TAG> tags = new ArrayList<>();
	private final Function<EXEMPLAR, TAG> targetFunction;

	private boolean firstRow = true;
	private CSRWithTags<TAG> CSRWithTags;

	public ProviderToCSRMatrixWithTarget(ObservationProviderInterface<FEATURE, EXEMPLAR> provider,
			Function<EXEMPLAR, TAG> targetFunction) {
		super(provider);
		this.targetFunction = targetFunction;

	}

	public List<TAG> getTags() {
		return tags;
	}

	public CSRWithTags<TAG> getCSRWithTags() {
		return CSRWithTags;
	}

	@Override
	public void process() throws IOException {
		super.process();
		CSRWithTags = new CSRWithTags<>(coerce(rowIndexes), coerce(columnIndexList), valuesList.toArray(new Double[0]),
				tags);
	}

	@Override
	protected void processNextRow(EXEMPLAR exemplar) {
		super.processNextRow(exemplar);
		tags.add(targetFunction.apply(exemplar));
		LOGGING_TIMER.nextStep();
	}

	@Override
	protected void processNextRow(int[] columnIndexes, int rowIndex, Double[] values) {
		if (firstRow) {
			firstRow = false;
			rowIndexes.add(0);
		}
		rowIndexes.add(rowIndex);
		columnIndexList.addAll(Arrays.stream(columnIndexes).boxed().collect(Collectors.toList()));
		valuesList.addAll(Arrays.asList(values));
	}
}
