package com.mangione.continuous.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

public class LoggingTimer {
	private final StopWatch stopWatch;
	private final Logger logger;
	private final long loggingInterval;
	private final String logMessage;
	private int numProcessed;

	public LoggingTimer(Logger logger, long loggingInterval, String logMessage) {
		this.logger = logger;
		this.loggingInterval = loggingInterval;
		this.logMessage = logMessage;
		stopWatch = new StopWatch();
		stopWatch.start();
	}

	public void nextStep() {
		if (++numProcessed % loggingInterval == 0) {
			stopWatch.split();
			long numberOfSeconds = stopWatch.getSplitTime() / 1000;
			if (numberOfSeconds > 0)
				logger.info(String.format("Processed %d %s at %d per second", numProcessed, logMessage, numProcessed / numberOfSeconds));
		}
	}
}
