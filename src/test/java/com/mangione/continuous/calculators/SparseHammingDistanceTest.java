package com.mangione.continuous.calculators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mangione.continuous.observations.sparse.SparseObservation;
import com.mangione.continuous.observations.sparse.SparseObservationBuilder;

public class SparseHammingDistanceTest {
	@Test
	public void distance() {
		SparseObservationBuilder<Integer> obs1 = new SparseObservationBuilder<>(5, 0);
		obs1.setFeature(1, 1);
		obs1.setFeature(2, 1);
		SparseObservationBuilder<Integer> obs2 = new SparseObservationBuilder<>(5, 0);
		obs2.setFeature(0, 1);
		obs2.setFeature(3, 1);
		assertEquals(4, new SparseHammingDistance<SparseObservation<Integer>>()
				.calculateDistance(obs1.build(Integer[]::new), obs2.build(Integer[]::new)));
	}

	@Test
	public void distanceAllZeroesFirst() {
		SparseObservationBuilder<Integer> obs1 = new SparseObservationBuilder<>(5, 0);
		SparseObservationBuilder<Integer> obs2 = new SparseObservationBuilder<>(5, 0);
		obs2.setFeature(0, 1);
		obs2.setFeature(3, 1);
		assertEquals(2, new SparseHammingDistance<SparseObservation<Integer>>().
				calculateDistance(obs1.build(Integer[]::new), obs2.build(Integer[]::new)));
	}

	@Test
	public void moreFirstThanSecond() {
		SparseObservationBuilder<Integer> obs1 = new SparseObservationBuilder<>(5, 0);
		obs1.setFeature(1, 1);
		obs1.setFeature(2, 1);
		obs1.setFeature(4, 1);
		SparseObservationBuilder<Integer> obs2 = new SparseObservationBuilder<>(5, 0);
		obs2.setFeature(0, 1);
		obs2.setFeature(3, 1);
		assertEquals(5, new SparseHammingDistance<SparseObservation<Integer>>().calculateDistance(obs1.build(Integer[]::new), obs2.build(Integer[]::new)));
	}

	@Test
	public void moreSecondThanFirst() {
		SparseObservationBuilder<Integer> obs1 = new SparseObservationBuilder<>(5, 0);
		obs1.setFeature(1, 1);
		obs1.setFeature(2, 1);
		SparseObservationBuilder<Integer> obs2 = new SparseObservationBuilder<>(5, 0);
		obs2.setFeature(0, 1);
		obs2.setFeature(3, 1);
		obs2.setFeature(4, 1);
		assertEquals(5, new SparseHammingDistance<SparseObservation<Integer>>().calculateDistance(obs1.build(Integer[]::new), obs2.build(Integer[]::new)));
	}

	@Test
	public void zerosHaveColumns() {
		SparseObservationBuilder<Integer> obs1 = new SparseObservationBuilder<>(5, 0);
		obs1.setFeature(1, 1);
		obs1.setFeature(2, 0);
		obs1.setFeature(3, 0);
		obs1.setFeature(4, 0);

		SparseObservationBuilder<Integer> obs2 = new SparseObservationBuilder<>(5, 0);
		obs2.setFeature(0, 1);
		obs2.setFeature(2, 0);
		obs2.setFeature(3, 1);
		obs2.setFeature(4, 1);
		assertEquals(4, new SparseHammingDistance<SparseObservation<Integer>>()
				.calculateDistance(obs1.build(Integer[]::new), obs2.build(Integer[]::new)));
	}
}