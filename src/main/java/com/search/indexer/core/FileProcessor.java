package com.search.indexer.core;

import com.search.indexer.config.IndexerConfig;
import com.search.indexer.exception.FileProcessingException;
import com.search.indexer.rules.IndexingRule;
import com.search.indexer.rules.LongWordsRule;
import com.search.indexer.rules.UppercaseWordsRule;
import com.search.indexer.util.PerformanceMonitor;
import com.search.indexer.util.TextCleaner;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileProcessor is responsible for processing files and applying indexing rules to the words in the
 * files.
 */
@Slf4j
public class FileProcessor {

  private static final long MAX_FILE_SIZE = IndexerConfig.getMaxFileSize();
  private static final int PROGRESS_LOG_INTERVAL = IndexerConfig.getProgressLogInterval();
  private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);
  /**
   * List of indexing rules to be applied to the processed files
   */// 100MB
  private final List<IndexingRule> rules;


  /**
   * Constructs a new FileProcessor with default indexing rules. Default rules include:
   * <ul>
   *   <li>UppercaseWordsRule - counts words starting with uppercase letters</li>
   *   <li>LongWordsRule - identifies words exceeding a specified length</li>
   * </ul>
   */
  public FileProcessor() {
    rules = new ArrayList<>();
    rules.add(new UppercaseWordsRule());
    rules.add(new LongWordsRule());
  }

  /**
   * Processes a list of files applying all configured indexing rules. Tracks progress and monitors
   * performance during processing.
   *
   * @param filePaths List of paths to the files to be processed
   * @throws NullPointerException     if filePaths is null
   * @throws IllegalArgumentException if filePaths is empty
   * @throws FileProcessingException  if there are errors during file processing
   * @throws SecurityException        if file validation fails (size/type)
   */
  public void processFiles(List<Path> filePaths) {
    Objects.requireNonNull(filePaths, "filePaths cannot be null");
    if (filePaths.isEmpty()) {
      throw new IllegalArgumentException("filePaths cannot be empty");
    }

    PerformanceMonitor monitor = new PerformanceMonitor();

    try {
      int totalFiles = filePaths.size();
      int processedFiles = 0;

      for (Path filePath : filePaths) {
        processFileStream(filePath);
        processedFiles++;
        printProgress(processedFiles, totalFiles);
      }

    } finally {
      monitor.stop();
      monitor.printPerformanceMetrics();
    }
  }

  /**
   * Logs the processing progress at specified intervals. Progress is logged at the start,
   * completion, and at every PROGRESS_LOG_INTERVAL percentage.
   *
   * @param processed number of files processed so far
   * @param total     total number of files to process
   */
  private void printProgress(int processed, int total) {
    if (logger.isDebugEnabled()) {  // Only calculate if logging will actually occur
      double percentage = (processed * 100.0) / total;

      if (processed == 1 || processed == total || percentage % PROGRESS_LOG_INTERVAL == 0) {
        String formattedPercentage = String.format("%.1f", percentage);
        logger.debug("Progress: {}/{} files processed ({}%)",
            processed, total, formattedPercentage);
      }
    }
  }

  /**
   * Processes a single file, applying all configured indexing rules. Each word in the file is
   * processed according to the rules.
   *
   * @param filePath path to the file to be processed
   * @throws IOException             if there are issues reading the file
   * @throws FileProcessingException if processing fails
   */
  private void processFileStream(Path filePath) {
    try {
      validateFile(filePath);
      try (BufferedReader reader = Files.newBufferedReader(filePath)) {
        List<RuleAccumulator> accumulators = rules.stream()
            .map(RuleAccumulator::new)
            .collect(Collectors.toList());

        String line;
        StringBuilder wordBuilder = new StringBuilder(100);

        //done : remove this

        while ((line = reader.readLine()) != null) {
          processLine(line, wordBuilder, accumulators);
        }

        for (RuleAccumulator accumulator : accumulators) {
          logger.info("\nFile Name: {}\n {}\n", filePath.getFileName(),
              accumulator.getResult());
        }
      }
    } catch (IOException e) {
      logger.error("Error processing file {}: {}", filePath, e.getMessage(), e);
      throw new FileProcessingException("Failed to process file: " + filePath, e);
    }
  }

  /**
   * Processes a single line of text, breaking it into words and applying rules. Words are
   * identified by whitespace boundaries.
   *
   * @param line         the line of text to process
   * @param wordBuilder  StringBuilder used for word construction
   * @param accumulators list of rule accumulators to apply to each word
   */
  private void processLine(String line, StringBuilder wordBuilder,
      List<RuleAccumulator> accumulators) {
    String cleanedLine = TextCleaner.removeHtmlTags(line);
    int len = cleanedLine.length();

    for (int i = 0; i < len; i++) {
      char c = cleanedLine.charAt(i);

      if (Character.isWhitespace(c)) {
        if (!wordBuilder.isEmpty()) {
          String word = wordBuilder.toString();
          for (RuleAccumulator accumulator : accumulators) {
            accumulator.processWord(word);
          }
          wordBuilder.setLength(0);
        }
      } else {
        wordBuilder.append(c);
      }
    }

    if (!wordBuilder.isEmpty()) {
      String word = wordBuilder.toString();
      for (RuleAccumulator accumulator : accumulators) {
        accumulator.processWord(word);
      }
      wordBuilder.setLength(0);
    }
  }

  /**
   * Validates a file before processing. Checks if:
   * <ul>
   *   <li>The file exists and is a regular file</li>
   *   <li>The file size is within the allowed limit</li>
   * </ul>
   *
   * @param filePath path to the file to validate
   * @throws SecurityException if validation fails
   * @throws IOException       if file attributes cannot be read
   */
  private void validateFile(Path filePath) throws IOException {
    if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
      throw new SecurityException("Not a regular file: " + filePath);
    }
    if (Files.size(filePath) > MAX_FILE_SIZE) {
      throw new SecurityException("File too large: " + filePath);
    }
  }
}