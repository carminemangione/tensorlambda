package com.mangione.continuous.stats;

public class Pair<R, S> {

	private final R first;
	private final R second;

	public Pair(R first, R second) {
		this.first = first;
		this.second = second;
	}

	public R getFirst() {
		return first;
	}

	public R getSecond() {
		return second;
	}
}
