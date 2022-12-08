package com.mangione.continuous.sampling.reservoir;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

javax.annotation.Nonnull;

class Reservoir<R> {
	private final TreeSet<Element> elements = new TreeSet<>();

	private final double sampleSize;

	Reservoir(int sampleSize) {
		this.sampleSize = sampleSize;
	}

	void present(R item, double v) {
		Element element = new Element(item, v);
		elements.add(element);
		if (elements.size() > sampleSize) {
			Element last = elements.last();
			elements.remove(last);
		}
	}

	List<R> getItems() {
		return elements.stream().map(Element::getItem).collect(Collectors.toList());
	}

	double getLastValue() {
		return elements.last().getValue();
	}

	private class Element implements Comparable<Element> {
		private final R item;
		private final double value;


		private Element(R item, double value) {
			this.item = item;
			this.value = value;
		}

		R getItem() {
			return item;
		}

		public double getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Element{" +
					"item=" + item +
					", value=" + value +
					'}';
		}

		@Override
		public int compareTo(@NotNull Reservoir<R>.Element o) {
			return Double.compare(value, o.value);
		}

	}
}
