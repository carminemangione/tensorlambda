package com.mangione.continuous.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

public class LoggingTimer {
	private final StopWatch stopWatch;
	private final Logger logger;
	private final long loggingInterval;
	private final String logMessage;
	private final AtomicInteger numProcessed = new AtomicInteger(1);

	public LoggingTimer(Logger logger, long loggingInterval, String logMessage) {
		this.logger = logger;
		this.loggingInterval = loggingInterval;
		this.logMessage = logMessage;
		stopWatch = new StopWatch();
		stopWatch.start();
	}

	public double getCurrentRate() {
		long seconds = stopWatch.getSplitTime() / 1000;
		return (double) numProcessed.get() / seconds;
	}

	public void nextStep() {
		if (numProcessed.getAndIncrement() % loggingInterval == 0) {
			stopWatch.split();
			double currentRate = getCurrentRate();
			if (currentRate > 0) {
				logger.info(String.format("Processed %d %s at %.4f per second",
						numProcessed.get() - 1, logMessage, currentRate));
			} else {
				logger.info(String.format("Processed %d %s",
						numProcessed.get(),
						logMessage));
			}
		}
	}
}
