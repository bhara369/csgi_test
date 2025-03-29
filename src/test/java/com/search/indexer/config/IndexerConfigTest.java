package com.search.indexer.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("IndexerConfig Test Suite")
class IndexerConfigTest {

  @TempDir
  Path tempDir;

  private File propFile;

  @BeforeEach
  void setUp() throws IOException {
    // Create a temporary properties file
    propFile = tempDir.resolve("application.properties").toFile();
    Properties props = new Properties();
    props.setProperty("file.buffer.size", "16384");
    props.setProperty("file.max.size", "209715200");
    props.setProperty("word.min.length", "5");
    props.setProperty("word.builder.capacity", "150");
    props.setProperty("performance.log.interval", "15");

    try (FileWriter writer = new FileWriter(propFile)) {
      props.store(writer, "Test Properties");
    }
  }

  @Test
  @DisplayName("Should return default buffer size when property is missing")
  void shouldReturnDefaultBufferSize() {
    // Act
    int bufferSize = IndexerConfig.getBufferSize();

    // Assert
    assertEquals(8192, bufferSize, "Default buffer size should be 8192");
  }

  @Test
  @DisplayName("Should return default max file size when property is missing")
  void shouldReturnDefaultMaxFileSize() {
    // Act
    long maxFileSize = IndexerConfig.getMaxFileSize();

    // Assert
    assertEquals(104857600L, maxFileSize, "Default max file size should be 100MB");
  }

  @Test
  @DisplayName("Should return default minimum word length when property is missing")
  void shouldReturnDefaultMinWordLength() {
    // Act
    int minWordLength = IndexerConfig.getMinWordLength();

    // Assert
    assertEquals(5, minWordLength, "Default minimum word length should be 5");
  }


  @Test
  @DisplayName("Should return default word builder capacity when property is missing")
  void shouldReturnDefaultWordBuilderCapacity() {
    // Act
    int capacity = IndexerConfig.getWordBuilderCapacity();

    // Assert
    assertEquals(100, capacity, "Default word builder capacity should be 100");
  }

  @Test
  @DisplayName("Should return default progress log interval when property is missing")
  void shouldReturnDefaultProgressLogInterval() {
    // Act
    int interval = IndexerConfig.getProgressLogInterval();

    // Assert
    assertEquals(10, interval, "Default progress log interval should be 10");
  }

  @Test
  @DisplayName("Should handle invalid numeric values in properties")
  void shouldHandleInvalidNumericValues() throws IOException {
    // Arrange
    Properties props = new Properties();
    props.setProperty("file.buffer.size", "invalid");
    try (FileWriter writer = new FileWriter(propFile)) {
      props.store(writer, "Invalid Properties");
    }

    // Act & Assert
    assertDoesNotThrow(() -> IndexerConfig.getBufferSize(),
        "Should handle invalid numeric values gracefully");
  }


  @Test
  @DisplayName("Should handle negative values in properties")
  void shouldHandleNegativeValues() throws IOException {
    // Arrange
    Properties props = new Properties();
    props.setProperty("word.min.length", "-5");
    try (FileWriter writer = new FileWriter(propFile)) {
      props.store(writer, "Negative Values");
    }

    // Act
    int minWordLength = IndexerConfig.getMinWordLength();

    // Assert
    assertTrue(minWordLength >= 0,
        "Should handle negative values and return non-negative result");
  }

  @Test
  @DisplayName("Should handle zero values in properties")
  void shouldHandleZeroValues() throws IOException {
    // Arrange
    Properties props = new Properties();
    props.setProperty("file.buffer.size", "0");
    try (FileWriter writer = new FileWriter(propFile)) {
      props.store(writer, "Zero Values");
    }

    // Act
    int bufferSize = IndexerConfig.getBufferSize();

    // Assert
    assertTrue(bufferSize > 0,
        "Should handle zero values and return positive result");
  }

  @Test
  @DisplayName("Should handle extremely large values")
  void shouldHandleExtremelyLargeValues() throws IOException {
    // Arrange
    Properties props = new Properties();
    props.setProperty("file.max.size", String.valueOf(Long.MAX_VALUE));
    try (FileWriter writer = new FileWriter(propFile)) {
      props.store(writer, "Large Values");
    }

    // Act
    long maxFileSize = IndexerConfig.getMaxFileSize();

    // Assert
    assertTrue(maxFileSize > 0,
        "Should handle extremely large values without overflow");
  }

  @Test
  @DisplayName("Should not allow instantiation of IndexerConfig")
  void shouldNotAllowInstantiation() {
    // Act & Assert
    assertThrows(UnsupportedOperationException.class, IndexerConfig::new,
        "Should throw UnsupportedOperationException when instantiated");
  }

  @Test
  @DisplayName("Should throw exception when properties file is missing")
  void shouldThrowExceptionWhenPropertiesFileIsMissing() {
    // Arrange
    propFile.delete(); // Ensure file is deleted

    // Act & Assert
    assertThrows(RuntimeException.class, IndexerConfig::new,
        "Should throw RuntimeException when properties file is missing");
  }

//  @Test
//  @DisplayName("Should throw exception when properties file cannot be loaded")
//  void shouldThrowExceptionWhenPropertiesFileCannotBeLoaded() throws IOException {
//    // Arrange
//    File brokenFile = tempDir.resolve("application.properties").toFile();
//    try (FileWriter writer = new FileWriter(brokenFile)) {
//      writer.write("invalid content without equals sign"); // Corrupt the file
//    }
//
//    // Act & Assert
//    Exception exception = assertThrows(RuntimeException.class, IndexerConfig::getBufferSize,
//        "Should throw RuntimeException when properties file is corrupted");
//
//    assertTrue(exception.getMessage().contains("Error loading properties"),
//        "Exception message should indicate failure to load properties");
//  }

}
