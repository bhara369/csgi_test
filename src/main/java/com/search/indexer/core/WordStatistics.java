package com.search.indexer.core;


import com.search.indexer.config.IndexerConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for collecting statistics about words processed during indexing. It
 * tracks the number of uppercase words and stores long words (greater than 5 characters).
 */
public class WordStatistics {

  /**
   * Initial capacity for the long words set to minimize resizing operations
   */
  private static final int INITIAL_SET_CAPACITY = 1000;
  private static final long MIN_WORD_LENGTH = IndexerConfig.getMinWordLength();
  private final Set<String> longWords;
  private int uppercaseCount;

  public WordStatistics() {
    this.uppercaseCount = 0;
    // Initialize with expected capacity to avoid resizing
    this.longWords = new HashSet<>(INITIAL_SET_CAPACITY);
  }

  public void processWord(String word) {
    if (!word.isEmpty()) {
      if (Character.isUpperCase(word.charAt(0))) {
        uppercaseCount++;
      }
      if (word.length() > MIN_WORD_LENGTH) {
        longWords.add(word);
      }
    }
  }

  public int getUppercaseCount() {
    return uppercaseCount;
  }

  public Set<String> getLongWords() {
    return longWords;
  }
}