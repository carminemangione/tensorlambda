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
        assertEquals(Integer.valueOf(0), pv.getIndex(0, "YELLOW"));
        assertEquals((Integer.valueOf(0)), pv.getIndex(3, "ADULT"));


    }

}
