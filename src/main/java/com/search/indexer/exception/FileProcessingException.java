package com.search.indexer.exception;

/**
 * Custom exception class for handling file processing errors in the indexer application. This
 * exception is thrown when there are issues during file reading, processing, or validation
 * operations.
 */
public class FileProcessingException extends RuntimeException {

  /**
   * Constructs a new FileProcessingException with the specified detail message.
   *
   * @param message the detail message
   */
  public FileProcessingException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileProcessingException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of the exception
   */
  public FileProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new FileProcessingException with the specified cause.
   *
   * @param cause the cause of the exception
   */
  public FileProcessingException(Throwable cause) {
    super(cause);
  }
}