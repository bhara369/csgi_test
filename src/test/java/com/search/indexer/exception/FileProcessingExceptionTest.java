package com.search.indexer.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link FileProcessingException}. Ensures correct behavior for all constructors.
 */
class FileProcessingExceptionTest {

  @Test
  @DisplayName("Should create exception with message")
  void shouldCreateExceptionWithMessage() {
    // Arrange
    String message = "File processing error occurred";

    // Act
    FileProcessingException exception = new FileProcessingException(message);

    // Assert
    Assertions.assertThat(exception)
        .isInstanceOf(RuntimeException.class)
        .hasMessage(message)
        .withFailMessage("Expected exception message to be '%s'", message);
  }

  @Test
  @DisplayName("Should create exception with message and cause")
  void shouldCreateExceptionWithMessageAndCause() {
    // Arrange
    String message = "File not found";
    Throwable cause = new NullPointerException("Null file reference");

    // Act
    FileProcessingException exception = new FileProcessingException(message, cause);

    // Assert
    Assertions.assertThat(exception)
        .isInstanceOf(RuntimeException.class)
        .hasMessage(message)
        .hasCause(cause)
        .withFailMessage("Expected message to be '%s' and cause to be '%s'", message, cause);
  }

  @Test
  @DisplayName("Should create exception with cause only")
  void shouldCreateExceptionWithCauseOnly() {
    // Arrange
    Throwable cause = new IllegalArgumentException("Invalid argument");

    // Act
    FileProcessingException exception = new FileProcessingException(cause);

    // Assert
    Assertions.assertThat(exception)
        .isInstanceOf(RuntimeException.class)
        .hasCause(cause)
        .withFailMessage("Expected cause to be '%s'", cause);
  }

  @Test
  @DisplayName("Should handle null message gracefully")
  void shouldHandleNullMessage() {
    // Act
    FileProcessingException exception = new FileProcessingException((String) null);

    // Assert
    Assertions.assertThat(exception)
        .isInstanceOf(RuntimeException.class)
        .hasMessage(null)
        .withFailMessage("Expected message to be null");
  }

  @Test
  @DisplayName("Should handle null cause gracefully")
  void shouldHandleNullCause() {
    // Act
    FileProcessingException exception = new FileProcessingException((Throwable) null);

    // Assert
    Assertions.assertThat(exception)
        .isInstanceOf(RuntimeException.class)
        .hasCause(null)
        .withFailMessage("Expected cause to be null");
  }
}