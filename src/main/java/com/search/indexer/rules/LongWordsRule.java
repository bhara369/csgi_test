package com.search.indexer.rules;

import com.search.indexer.config.IndexerConfig;
import com.search.indexer.core.WordStatistics;
import com.search.indexer.model.IndexingResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rule that identifies and sorts words exceeding minimum length.
 *
 * @since 1.0
 */
public class LongWordsRule implements IndexingRule {

  //todo: take this value from application props using @Value
  /**
   * Minimum length threshold for long words
   */
  private static final int MIN_WORD_LENGTH = IndexerConfig.getMinWordLength();

  /**
   * Creates sorted list of words longer than {@value MIN_WORD_LENGTH} characters.
   *
   * @param statistics collected word data
   * @return result containing sorted list of long words
   */
  @Override
  public IndexingResult process(WordStatistics statistics) {
    List<String> sortedLongWords = new ArrayList<>(statistics.getLongWords());
    Collections.sort(sortedLongWords); // Sort only once at the end

    return new IndexingResult(
        "\nRULE : Long Words List\n",
        String.format("%nRESULT : Words longer than %d characters: %s",
            MIN_WORD_LENGTH,
            String.join(", ", sortedLongWords))
    );
  }
}