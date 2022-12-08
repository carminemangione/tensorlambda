package com.mangione.continuous.observationproviders.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;

public class CsvObservationSpliteratorTest {

    private static final int NUM_LINES = 11;
    private File csvFile;
    private Function<File, BufferedReader> bufferedFunction;

    @Before
    public void setUp() throws Exception {
        csvFile = File.createTempFile("CsvObservationSpliteratorTest", "csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));
        for (int i = 0; i < NUM_LINES; i++) {
            bw.write("" + i);
            bw.newLine();
        }
        bw.close();
        bufferedFunction = x -> {
            try {
                return new BufferedReader(new FileReader(x));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Test
    public void spliteratorNoSplitSizeExact() {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), false, NUM_LINES, 2, bufferedFunction);
        assertEquals(NUM_LINES, spliterator.estimateSize());

        Set<Integer> collect = StreamSupport.stream(spliterator, false)
                .collect(Collectors.toSet());
        assertEquals(NUM_LINES, collect.size());
        assertTrue(collect.contains(0));
        assertTrue(collect.contains(NUM_LINES - 1));
    }

    @Test
    public void splitEstimatedSizeExactTwoThreads() throws ExecutionException, InterruptedException {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), false, NUM_LINES, 2, bufferedFunction);

        List<Integer> collect = new ForkJoinPool(2).submit(() ->
                StreamSupport.stream(spliterator, true)
                        .collect(Collectors.toList())).get();

        assertEquals(NUM_LINES, collect.size());
        assertTrue(collect.contains(0));
        assertTrue(collect.contains(NUM_LINES - 1));
    }

    @Test
    public void splitEstimatedSizeTooSmall() throws Exception {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), false, NUM_LINES / 4, 2, bufferedFunction);

        List<Integer> collect = new ForkJoinPool(2).submit(() ->
                StreamSupport.stream(spliterator, true)
                        .collect(Collectors.toList())).get();

        assertEquals(NUM_LINES, collect.size());
        assertTrue(collect.contains(0));
        assertTrue(collect.contains(NUM_LINES - 1));
    }

    @Test
    public void splitEstimatedSizeTooLarge() throws ExecutionException, InterruptedException {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), false, NUM_LINES * 4, 2, bufferedFunction);

        List<Integer> collect = new ForkJoinPool(2).submit(() ->
                StreamSupport.stream(spliterator, true)
                        .collect(Collectors.toList())).get();

        assertEquals(NUM_LINES, collect.size());
        assertTrue(collect.contains(0));
        assertTrue(collect.contains(NUM_LINES - 1));
    }

    @Test
    public void trySplitTooSmallReturnsNull() {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), false, NUM_LINES * 4, NUM_LINES, bufferedFunction);

        Spliterator<Integer> currentSplit = spliterator.trySplit();
        assertNotNull("Double the minimum batch size", currentSplit);

        currentSplit = currentSplit.trySplit();
        assertNotNull("The minimum batch size", currentSplit);

        assertNull("Half batch size should return null", currentSplit.trySplit());
    }

    @Test
    public void csvHeaderDoesNotMessUpSplit() throws Exception {
        CsvObservationSpliterator<Integer> spliterator = new CsvObservationSpliterator<>(csvFile, " ",
                x -> Integer.parseInt(x[0]), true, NUM_LINES / 4, 2, bufferedFunction);

        List<Integer> collect = new ForkJoinPool(2).submit(() ->
                StreamSupport.stream(spliterator, true)
                        .collect(Collectors.toList())).get();

        assertEquals(NUM_LINES - 1, collect.size());
        assertFalse(collect.contains(0));
        IntStream.range(1, NUM_LINES)
                .forEach(line -> assertTrue(collect.contains(line)));
    }
}