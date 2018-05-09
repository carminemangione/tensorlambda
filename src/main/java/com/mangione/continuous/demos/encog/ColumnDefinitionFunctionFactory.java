package com.mangione.continuous.demos.encog;

import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;

public class ColumnDefinitionFunctionFactory {
    public static ColumnDefinitionFunction createNamed(final String[] names){
        return new ColumnDefinitionFunction() {
            @Override
            public ColumnDefinition apply(String s, VersatileMLDataSet mlDataPairs) {
                ColumnDefinition def = mlDataPairs.defineSourceColumn(s,ColumnType.nominal);
                def.defineClass(names);
                return def;
            }
        };
    }

    public static ColumnDefinitionFunction createContinuous(){
        return new ColumnDefinitionFunction() {
            @Override
            public ColumnDefinition apply(String s, VersatileMLDataSet mlDataPairs) {
                ColumnDefinition def = mlDataPairs.defineSourceColumn(s,ColumnType.continuous);
                return def;
            }
        };
    }
}
