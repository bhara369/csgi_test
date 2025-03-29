package com.search.indexer.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration loader for file indexer properties.
 */
public class IndexerConfig {

  private static final Properties properties = new Properties();

  static {
    loadProperties();
  }

  public IndexerConfig() {
    throw new UnsupportedOperationException(
        "IndexerConfig is a utility class and cannot be instantiated");
  }

  private static void loadProperties() {
    try (InputStream input = IndexerConfig.class.getClassLoader()
        .getResourceAsStream("application.properties")) {
      if (input == null) {
        throw new RuntimeException("Unable to find application.properties");
      }
      properties.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Error loading properties", e);
    }
  }


  public static int getBufferSize() {
    return Integer.parseInt(properties.getProperty("file.buffer.size", "8192"));
  }

  public static long getMaxFileSize() {
    return Long.parseLong(properties.getProperty("file.max.size", "104857600"));
  }

  public static int getMinWordLength() {
    return Integer.parseInt(properties.getProperty("word.min.length", "5"));
  }

  public static int getWordBuilderCapacity() {
    return Integer.parseInt(properties.getProperty("word.builder.capacity", "100"));
  }

  public static int getProgressLogInterval() {
    return Integer.parseInt(properties.getProperty("performance.log.interval", "10"));
  }
}