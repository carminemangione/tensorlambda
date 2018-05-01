package com.mangione.continuous.observationproviders;

@FunctionalInterface
public interface ArraySupplier<E> {
	E[] get(int length);
}