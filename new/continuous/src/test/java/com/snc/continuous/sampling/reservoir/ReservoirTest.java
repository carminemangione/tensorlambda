package com.mangione.continuous.sampling.reservoir;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class ReservoirTest {


	@Test
	public void oneCategoryOneItem() {
		Reservoir<Integer> reservoir = new Reservoir<>(100);
		Integer item = 10;
		reservoir.present(item, 0.0);
		List<Integer> items = reservoir.getItems();
		assertEquals(1, items.size());
		assertEquals(item, items.get(0));
	}

	@Test
	public void singleElementCategory() {
		Reservoir<Integer> reservoir = new Reservoir<>(100);
		reservoir.present(10, 0.01);
		assertEquals(10, (int)reservoir.getItems().get(0));
	}

	@Test
	public void reservoirTakesFirstNElements() {
		int sampleSize = 100;
		Reservoir<Integer> reservoir = new Reservoir<>(sampleSize);
		IntStream.range(0, sampleSize).boxed().forEach(i->reservoir.present(i, i));
		assertEquals(IntStream.range(0, sampleSize).boxed().collect(Collectors.toList()), reservoir.getItems());
	}

	@Test
	public void reservoirInitiallySortedByMaximalValue() {
		int sampleSize = 100;
		Reservoir<Integer> reservoirWithItemsInsertedInversely = new Reservoir<>(sampleSize);
		IntStream.range(0, sampleSize).boxed().forEach(i -> reservoirWithItemsInsertedInversely.present(sampleSize - i, sampleSize - i));
		assertEquals(IntStream.range(1, sampleSize + 1).boxed().collect(Collectors.toList()), reservoirWithItemsInsertedInversely.getItems());
	}

	@Test
	public void putElementInCorrectSpaceAndRemoveLast() {
		int sampleSize = 100;
		Reservoir<Integer> reservoir = new Reservoir<>(sampleSize);
		IntStream.range(0, sampleSize).boxed().forEach(i -> reservoir.present(i, i));
		reservoir.present(666, 50.01);
		assertEquals(666, (long)reservoir.getItems().get(51));
		assertEquals(sampleSize - 2, (long)reservoir.getItems().get(sampleSize - 1));
		assertEquals(sampleSize, reservoir.getItems().size());
	}
}