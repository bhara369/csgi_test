package com.search.indexer.rules;


import com.search.indexer.core.WordStatistics;
import com.search.indexer.model.IndexingResult;

/**
 * Interface for text indexing rules that analyze word statistics.
 *
 * @since 1.0
 */
public interface IndexingRule {

  /**
   * Processes word statistics and returns analysis result.
   *
   * @param statistics collected word statistics
   * @return analysis result
   */
  IndexingResult process(WordStatistics statistics);
}