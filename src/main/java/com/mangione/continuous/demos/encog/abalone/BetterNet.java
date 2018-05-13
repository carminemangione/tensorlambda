package com.mangione.continuous.demos.encog.abalone;

import org.encog.ml.data.versatile.NormalizationHelper;

public class BetterNet {
    private final static String DATA_FILENAME = "src/main/resources/Abalone/abalone.data";
    private final static String EGA_FILENAME = "src/main/resources/Abalone/abalone.ega";
    private final static char DATA_FILE_DELIMITER = ',';

    private static String[] theInputHeadings = {"Sex", "Shell Length",
            "Shell Diameter", "Shell Height", "Total Abalone Weight",
            "Shucked Weight", "Viscera Weight", "Shell Weight", "Rings"};

    public NormalizationHelper helper = new NormalizationHelper();


}
