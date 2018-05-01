package com.mangione.continuous.zscale;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.*;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZScaleObservationProviderTest {

    @Test
    public void oneColumnZScaled () throws Exception {
        Double[] column = {3d, 5d, 7d, 69d, 27d};
        Double[][] singleColumnObservation = new Double[column.length][1];
        for (int i = 0; i < singleColumnObservation.length; i++) {
            singleColumnObservation[i][0] = column[i];
        }
        ColumnZScale czs = new ColumnZScale(ArrayUtils.toPrimitive(column));
        ArrayObservationProvider<Double, ObservationInterface<Double>> aop =
                new ArrayObservationProvider<>(
                        singleColumnObservation, new DoubleObservationFactory<>());

        ZScaleObservationProvider<ObservationInterface<Double>> observationProvider = new ZScaleObservationProvider<>(
                aop, new DoubleObservationFactory<>());

        validateSingleColumn(column, czs, observationProvider);
        assertFalse(observationProvider.hasNext());
    }

    @Test
    public void twoColumnsZScaled () {
        Double[] column1 = {3d, 5d, 7d, 69d, 27d};
        Double[] column2 = {100d, 367d, 7d, 1811d, 2d};
        Double[][] twoColumnObservations = new Double[column1.length][2];
        for (int i = 0; i < twoColumnObservations.length; i++) {
            twoColumnObservations[i][0] = column1[i];
            twoColumnObservations[i][1] = column2[i];
        }
        ColumnZScale czs1 = new ColumnZScale(ArrayUtils.toPrimitive(column1));
        ColumnZScale czs2 = new ColumnZScale(ArrayUtils.toPrimitive(column2));
        ArrayObservationProvider<Double, ObservationInterface<Double>> aop = new ArrayObservationProvider<>(
                twoColumnObservations, new DoubleObservationFactory<>());
        ZScaleObservationProvider<ObservationInterface<Double>> observationProvider = new ZScaleObservationProvider<>(
                aop, new DoubleObservationFactory<>());

        for (int i = 0; i < column1.length; i++) {
            assertTrue(observationProvider.hasNext());
            final Double[] features = observationProvider.next().getFeatures();
            assertEquals(2, features.length);
            assertEquals(czs1.zscale(column1[i]), features[0], 0.0);
            assertEquals(czs2.zscale(column2[i]), features[1], 0.0);
        }
        assertFalse(observationProvider.hasNext());
    }



    private void validateSingleColumn(Double[] column1, ColumnZScale czs1,
            ZScaleObservationProvider<ObservationInterface<Double>> observationProvider)  {
        for (double aColumn1 : column1) {
            assertTrue(observationProvider.hasNext());
            final Double[] features = observationProvider.next().getFeatures();
            assertEquals(1, features.length);
            assertEquals(czs1.zscale(aColumn1), features[0], 0.0);
        }
    }

}