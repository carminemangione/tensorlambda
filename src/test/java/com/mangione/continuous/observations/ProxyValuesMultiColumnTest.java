package com.mangione.continuous.observations;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ProxyValuesMultiColumnTest {
    @Test
    public void balloonTest() throws IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/adult+stretch.data");
        ProxyValuesMultiColumn pv = new ProxyValuesMultiColumn(file);
        assertTrue(pv.contains(0, "PURPLE"));
        assertEquals(Integer.valueOf(3), pv.getIndex(1, "LARGE"));
        assertEquals(Integer.valueOf(8), pv.getIndex(4, "T"));
        assertSame(2, pv.size(4));
        assertSame(2, pv.size(0));
        assertSame(2, pv.size(1));
        assertSame(2, pv.size(2));
        assertFalse(pv.contains(2, "fish"));
        assertSame(10, pv.getNumLevels());




    }

    @Test
    public void tumorTest() throws IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/primary-tumor.data");
        ProxyValuesMultiColumn pv = new ProxyValuesMultiColumn(file);
        assertSame(21, pv.size(0));
        assertSame(2, pv.size(16));
        assertFalse(pv.contains(0, "9"));
        pv.addPair(0, 374, "9");
        assertTrue(pv.contains(0, "9"));


    }

}
