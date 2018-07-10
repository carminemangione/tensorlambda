package com.mangione.continuous.observationproviders;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.junit.Test;

import com.mangione.continuous.observations.StringObservationFactory;

public class RowFilteringObservationProviderTest {


	@Test
	public void testRowFiltering() throws FileNotFoundException {
		CsvObservationProvider ob = new CsvObservationProvider(new File("src/test/resource/hi.csv"), new StringObservationFactory(), false);
		RowFilteringObservationProvider rfop = new RowFilteringObservationProvider(ob);
		Iterator it = rfop.iterator();
		int counter = 0;
		while(it.hasNext()){
			counter++;
			if(null == it.next())
				counter--;
		}

		assertEquals(1, counter);
	}


}