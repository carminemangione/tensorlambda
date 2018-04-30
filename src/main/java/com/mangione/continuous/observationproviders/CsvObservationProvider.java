package com.mangione.continuous.observationproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationInterface;

public class CsvObservationProvider implements ObservationProviderInterface<String, ObservationInterface<String>> {

    private final File file;
    private BufferedReader bufferedReader;


    public CsvObservationProvider(File file) throws FileNotFoundException {
        this.file = file;
        this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    @Override
    public boolean hasNext() {
        boolean ready = false;
        try {
            if (bufferedReader != null) {
                ready = bufferedReader.ready();
                if (!ready) {
                    reachedEndCloseFileAndClearReader();
                }
            }
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        return ready;
    }

    @Override
    public ObservationInterface<String> next() {
        String[] nextLine;
        try {
            nextLine = bufferedReader.readLine().split(",");
        } catch (IOException e) {
            throw new ProviderException(e);
        }

        return create(nextLine);
    }

    @Override
    public void reset()  {
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public long getNumberOfLines()  {
        long numberOfLines = 0;
        try {
            while (bufferedReader.ready()) {
                numberOfLines++;
                bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        reset();
        return numberOfLines;
    }

	@Override
	public ObservationInterface<String> create(String[] data) {
		return new Observation<>(data);
	}

	private void reachedEndCloseFileAndClearReader() throws IOException {
        bufferedReader.close();
        bufferedReader = null;
    }
}

