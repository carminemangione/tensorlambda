package com.mangione.continuous.demos.encog;

import org.encog.ml.data.versatile.sources.VersatileDataSource;

public interface NamedVersatileDataSource extends VersatileDataSource {
    public String[] columnNames();
}
