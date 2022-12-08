package com.mangione.continuous.encodings;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import com.mangione.continuous.encodings.ProxyValuesMultiCategory;

public class ProxyValuesMultiCategoryTest {
    @Test
    @Ignore
    public void balloonTest() throws IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/adult+stretch.data");
        ProxyValuesMultiCategory pv = new ProxyValuesMultiCategory(file);
        assertTrue(pv.contains(0, "PURPLE"));
        assertEquals(Integer.valueOf(0), pv.getIndex(0, "YELLOW"));
        assertEquals(Integer.valueOf(0), pv.getIndex(3, "ADULT"));
        assertSame(2, pv.size(4));
        assertSame(2, pv.size(0));
        assertSame(2, pv.size(1));
        assertSame(2, pv.size(2));
        assertFalse(pv.contains(2, "fish"));
        assertSame(10, pv.getNumLevels());
    }

    @Test
    @Ignore
    public void tumorTest() throws IOException {
        File file = new File("/Users/aditya.yellumahanti/Downloads/primary-tumor.data");
        ProxyValuesMultiCategory pv = new ProxyValuesMultiCategory(file);
        assertSame(21, pv.size(0));
        assertSame(2, pv.size(16));
        assertFalse(pv.contains(0, "9"));
        pv.addPair(0, 374, "9");
        assertTrue(pv.contains(0, "9"));


    }

}
