package com.search.indexer.rules;

import com.search.indexer.core.WordStatistics;
import com.search.indexer.model.IndexingResult;

/**
 * Rule that counts words starting with uppercase letters.
 *
 * @since 1.0
 */
public class UppercaseWordsRule implements IndexingRule {

  /**
   * Returns count of words starting with uppercase letters.
   *
   * @param statistics collected word data
   * @return result containing uppercase word count
   */
  @Override
  public IndexingResult process(WordStatistics statistics) {
    return new IndexingResult(
        "\nRULE : Uppercase Words Count\n",
        String.format("%nRESULT : Number of words starting with uppercase: %d",
            statistics.getUppercaseCount())
    );
  }
}