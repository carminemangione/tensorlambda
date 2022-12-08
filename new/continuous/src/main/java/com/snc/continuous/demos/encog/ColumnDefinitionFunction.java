package com.mangione.continuous.demos.encog;

import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ColumnDefinitionFunction extends BiFunction<String,VersatileMLDataSet,ColumnDefinition> {
}
