package com.search.indexer;

import com.search.indexer.core.FileProcessor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Example usage showing performance monitoring
@Slf4j
public class IndexingApplication {

  /**
   * Main method to start the file indexing application.
   *
   * @param args Command line arguments representing file paths to be indexed.
   */
  private static final Logger logger = LoggerFactory.getLogger(IndexingApplication.class);

  private static FileProcessor processor = new FileProcessor();

  public static void setProcessor(FileProcessor processor) {
    IndexingApplication.processor = processor;
  }

  // Example usage showing performance monitoring
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw new Exception("Please provide at least one file path as argument");
    }

    logger.debug("File Indexer Starting...");
    logger.debug("Number of files to process: {}", args.length);

    List<Path> filePaths = Arrays.stream(args)
        .map(Paths::get)
        .collect(Collectors.toList());

    processor.processFiles(filePaths);

  }
}