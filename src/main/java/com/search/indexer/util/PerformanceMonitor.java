package com.search.indexer.util;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors execution time and memory usage of operations.
 *
 * @since 1.0
 */
@Slf4j
public class PerformanceMonitor {

  private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);
  private final long startTime;
  private final Runtime runtime;
  private long endTime;

  /**
   * Creates monitor and starts timing.
   */
  public PerformanceMonitor() {
    this.startTime = System.nanoTime();
    this.runtime = Runtime.getRuntime();
  }

  /**
   * Stops timing.
   */
  public void stop() {
    this.endTime = System.nanoTime();
  }

  /**
   * @return elapsed time in seconds
   */
  public double getElapsedSeconds() {
    return (endTime - startTime) / 1_000_000_000.0;
  }

  /**
   * Prints current memory usage in MB.
   */
  @Generated
  public void printMemoryUsage() {
    if (logger.isDebugEnabled()) {
      long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
      logger.debug("Memory used: {}", usedMemory);
    }
  }

  /**
   * Prints execution time and memory metrics.
   */
  @Generated
  public void printPerformanceMetrics() {
    if (logger.isDebugEnabled()) {
      logger.debug("Performance Metrics");
      logger.debug("Processing time: {} seconds", getElapsedSeconds());
      printMemoryUsage();
    }
  }
}