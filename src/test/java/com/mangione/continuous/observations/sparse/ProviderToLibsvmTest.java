package com.mangione.continuous.observations.sparse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;

public class ProviderToLibsvmTest {
	private File file;
	private List<SparseExemplar<Integer, Integer>> sparseExemplars;
	@Before
	public void setUp() throws Exception {
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

		file = File.createTempFile("ProviderToLibsvmTest", "libsvm");
		file.deleteOnExit();
	}

	@Test
	public void providerToLibsvm() throws Exception{

		new ProviderToLibsvm<>(new ListObservationProvider<>(sparseExemplars), file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		assertEquals("15 0:1 2:2", br.readLine());
		assertEquals("16 2:3", br.readLine());
		assertEquals("17 0:4 1:5 2:6", br.readLine());
		assertNull(br.readLine());
	}
}