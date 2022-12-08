package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;

public class AbstractProviderToLibsvmTest {
	private List<SparseExemplar<Integer, Integer>> sparseExemplars;
	private final List<String> encodedLines = new ArrayList<>();

	@Before
	public void setUp() {
		sparseExemplars = new ArrayList<>();
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
	}

	@Test
	public void withAfterTag() throws Exception {
		new PoopProviderToLibsvm().processProvider(new ListObservationProvider<>(sparseExemplars),
				ExemplarInterface::getLabel);

		assertEquals(3, encodedLines.size());
		assertEquals("15poop 0:1 2:2", encodedLines.get(0));
		assertEquals("16poop 2:3", encodedLines.get(1));
		assertEquals("17poop 0:4 1:5 2:6", encodedLines.get(2));

	}

	@Test
	public void nullTagMakesTagEmpty() throws Exception {
		new PoopProviderToLibsvm().processProvider(new ListObservationProvider<>(sparseExemplars), ex->null);

		assertEquals(3, encodedLines.size());
		assertEquals("poop 0:1 2:2", encodedLines.get(0));
		assertEquals("poop 2:3", encodedLines.get(1));
		assertEquals("poop 0:4 1:5 2:6", encodedLines.get(2));
	}


	private class PoopProviderToLibsvm extends AbstractProviderToLibsvm<SparseExemplar<Integer, Integer>> {
		@Override
		protected String afterTag(SparseExemplar<Integer, Integer> exemplar) {
			return "poop ";
		}

		@Override
		protected void processNextLine(String nextLine) {
			encodedLines.add(nextLine);
		}
	}
}