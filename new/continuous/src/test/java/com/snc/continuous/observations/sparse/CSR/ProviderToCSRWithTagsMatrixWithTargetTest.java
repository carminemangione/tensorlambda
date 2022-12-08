package com.mangione.continuous.observations.sparse.CSR;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;
import com.mangione.continuous.util.coersion.CoerceToDoubleArray;
import com.mangione.continuous.util.coersion.CoerceToIntArray;
import com.mangione.continuous.util.coersion.CoerceToLongArray;

public class ProviderToCSRWithTagsMatrixWithTargetTest {
	@Test
	public void withTargetsTest() throws Exception {
		List<SparseExemplar<Integer, Integer>> sparseExemplars = new ArrayList<>();
		SparseExemplarBuilder<Integer, Integer> sparseExemplar = new SparseExemplarBuilder<>(7, 0, 15);
		sparseExemplar.setFeature(0, 1);
		sparseExemplar.setFeature(2, 2);
		sparseExemplars.add(sparseExemplar.build(Integer[]::new));
		sparseExemplar = new SparseExemplarBuilder<>(7, 0, 16);
		sparseExemplar.setFeature(2, 3);
		sparseExemplars.add(sparseExemplar.build(Integer[]::new));
		sparseExemplar = new SparseExemplarBuilder<>(7, 0, 17);
		sparseExemplar.setFeature(0, 4);
		sparseExemplar.setFeature(1, 5);
		sparseExemplar.setFeature(2, 6);
		sparseExemplars.add(sparseExemplar.build(Integer[]::new));

		ProviderToCSRMatrixWithTarget<Integer, Integer, SparseExemplar<Integer, Integer>> csr = new ProviderToCSRMatrixWithTarget<>(new ListObservationProvider<>(sparseExemplars),
				ExemplarInterface::getLabel);
		csr.process();

		assertArrayEquals(new double[]{1, 2, 3, 4, 5, 6}, CoerceToDoubleArray.coerce(csr.getCSRWithTags().getValues()), 0);
		assertArrayEquals(new long[] {0, 2, 3, 6}, CoerceToLongArray.coerce(csr.getCSRWithTags().getRows()));
		assertArrayEquals(new int[] {0, 2, 2,  0, 1, 2}, csr.getCSRWithTags().getColumns());
		assertArrayEquals(new int[] {15, 16, 17}, CoerceToIntArray.coerce(csr.getTags()));
	}
}