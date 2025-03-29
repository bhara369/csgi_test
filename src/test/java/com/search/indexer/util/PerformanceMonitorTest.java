package com.search.indexer.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PerformanceMonitor Test Suite")
class PerformanceMonitorTest {

  private PerformanceMonitor monitor;

  @BeforeEach
  void setUp() {
    monitor = new PerformanceMonitor();
  }

  @Test
  @DisplayName("Should measure elapsed time correctly")
  void shouldMeasureElapsedTimeCorrectly() throws InterruptedException {
    // Arrange
    Thread.sleep(100); // Sleep for 100ms

    // Act
    monitor.stop();
    double elapsedSeconds = monitor.getElapsedSeconds();

    // Assert
    assertTrue(elapsedSeconds >= 0.1, "Elapsed time should be at least 0.1 seconds");
    assertTrue(elapsedSeconds < 0.2, "Elapsed time should be less than 0.2 seconds");
  }

  @Test
  @DisplayName("Should return zero elapsed time when stopped immediately")
  void shouldReturnMinimalElapsedTimeWhenStoppedImmediately() {
    // Act
    monitor.stop();
    double elapsedSeconds = monitor.getElapsedSeconds();

    // Assert
    assertTrue(elapsedSeconds >= 0.0, "Elapsed time should not be negative");
    assertTrue(elapsedSeconds < 0.1, "Elapsed time should be very small");
  }

  @Test
  @DisplayName("Should calculate elapsed time in correct units")
  void shouldCalculateElapsedTimeInCorrectUnits() throws InterruptedException {
    // Arrange
    Thread.sleep(1000); // Sleep for 1 second

    // Act
    monitor.stop();
    double elapsedSeconds = monitor.getElapsedSeconds();

    // Assert
    assertTrue(elapsedSeconds >= 1.0, "Elapsed time should be at least 1 second");
    assertTrue(elapsedSeconds < 1.1, "Elapsed time should be close to 1 second");
  }

  @Test
  @DisplayName("Should not throw exception when printing memory usage")
  void shouldNotThrowExceptionWhenPrintingMemoryUsage() {
    // Act & Assert
    assertDoesNotThrow(() -> monitor.printMemoryUsage(),
        "Printing memory usage should not throw exception");
  }

  @Test
  @DisplayName("Should not throw exception when printing performance metrics")
  void shouldNotThrowExceptionWhenPrintingPerformanceMetrics() {
    // Arrange
    monitor.stop();

    // Act & Assert
    assertDoesNotThrow(() -> monitor.printPerformanceMetrics(),
        "Printing performance metrics should not throw exception");
  }


  @Test
  @DisplayName("Should measure memory usage greater than zero")
  void shouldMeasureMemoryUsageGreaterThanZero() {
    // Arrange
    Runtime runtime = Runtime.getRuntime();

    // Act
    long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;

    // Assert
    assertTrue(usedMemory > 0, "Used memory should be greater than 0 MB");
  }

  @Test
  @DisplayName("Should handle performance metrics before stop")
  void shouldHandlePerformanceMetricsBeforeStop() {
    // Act & Assert
    assertDoesNotThrow(() -> monitor.printPerformanceMetrics(),
        "Should handle printing metrics before stop");
  }

}