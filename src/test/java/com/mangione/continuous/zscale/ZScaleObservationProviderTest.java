package com.mangione.continuous.zscale;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZScaleObservationProviderTest {

    @Test
    public void oneColumnZScaled () throws Exception {
        double[] column = {3, 5, 7, 69, 27};
        double[][] singleColumnObservation = new double[column.length][1];
        for (int i = 0; i < singleColumnObservation.length; i++) {
            singleColumnObservation[i][0] = column[i];
        }
        ColumnZScale czs = new ColumnZScale(column);
        ArrayObservationProvider<Observation> aop = new ArrayObservationProvider<>(singleColumnObservation, new ObservationFactory());
        ZScaleObservationProvider<Observation> observationProvider = new ZScaleObservationProvider<>(aop, new ObservationFactory(), false);

        validateSingleColumn(column, czs, observationProvider);
        assertFalse(observationProvider.hasNext());
    }

    @Test
    public void twoColumnsZScaled () throws Exception {
        double[] column1 = {3, 5, 7, 69, 27};
        double[] column2 = {100, 367, 7, 1811, 2};
        double[][] twoColumnObservations = new double[column1.length][2];
        for (int i = 0; i < twoColumnObservations.length; i++) {
            twoColumnObservations[i][0] = column1[i];
            twoColumnObservations[i][1] = column2[i];
        }
        ColumnZScale czs1 = new ColumnZScale(column1);
        ColumnZScale czs2 = new ColumnZScale(column2);
        ArrayObservationProvider<Observation> aop = new ArrayObservationProvider<>(twoColumnObservations, new ObservationFactory());
        ZScaleObservationProvider<Observation> observationProvider = new ZScaleObservationProvider<>(aop, new ObservationFactory(), false);

        for (int i = 0; i < column1.length; i++) {
            assertTrue(observationProvider.hasNext());
            final double[] features = observationProvider.next().getFeatures();
            assertEquals(2, features.length);
            assertEquals(czs1.zscale(column1[i]), features[0], 0.0);
            assertEquals(czs2.zscale(column2[i]), features[1], 0.0);
        }
        assertFalse(observationProvider.hasNext());
    }

    @Test
    public void lastColumnIsLabel () throws Exception {
        double[] column1 = {3, 5, 7, 69, 27};
        double[] column2 = {100, 367, 7, 1811, 2};
        double[][] twoColumnObservations = new double[column1.length][2];
        for (int i = 0; i < twoColumnObservations.length; i++) {
            twoColumnObservations[i][0] = column1[i];
            twoColumnObservations[i][1] = column2[i];
        }
        ColumnZScale czs1 = new ColumnZScale(column1);
        ArrayObservationProvider<Observation> aop = new ArrayObservationProvider<>(twoColumnObservations, new ObservationFactory());
        ZScaleObservationProvider<DiscreteExemplar> observationProvider = new ZScaleObservationProvider<>(aop, new DiscreteExemplarFactory(), true);


        validateSingleColumn(column1, czs1, observationProvider);
        assertFalse(observationProvider.hasNext());
    }



    private void validateSingleColumn(double[] column1, ColumnZScale czs1,
            ZScaleObservationProvider observationProvider) throws IOException {
        for (double aColumn1 : column1) {
            assertTrue(observationProvider.hasNext());
            final double[] features = observationProvider.next().getFeatures();
            assertEquals(1, features.length);
            assertEquals(czs1.zscale(aColumn1), features[0], 0.0);
        }
    }

}