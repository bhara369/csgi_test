package com.search.indexer.core;

import com.search.indexer.model.IndexingResult;
import com.search.indexer.rules.IndexingRule;
import java.util.Objects;

/**
 * Accumulates statistics for a specific indexing rule during file processing. This class acts as a
 * bridge between word processing and rule application, maintaining statistics for each word
 * encountered and applying the rule to generate final results.
 */
public class RuleAccumulator {

  /**
   * The rule to be applied to the accumulated statistics
   */
  private final IndexingRule rule;
  /**
   * Statistics collector for processed words
   */
  private final WordStatistics statistics;

  public RuleAccumulator(IndexingRule rule) {
    this.rule = Objects.requireNonNull(rule, "IndexingRule cannot be null");
    this.statistics = new WordStatistics();
  }

  /**
   * Processes a single word and updates the internal statistics. This method delegates the actual
   * word processing to the {@link WordStatistics} instance while maintaining the rule context.
   *
   * @param word the word to be processed
   */
  public void processWord(String word) {
    statistics.processWord(word);
  }

  /**
   * Generates the final result by applying the rule to the accumulated statistics. This method
   * triggers the rule processing and returns the final indexing result.
   */
  public IndexingResult getResult() {
    return rule.process(statistics);
  }
}