package com.search.indexer.model;


/**
 * Represents the result of an indexing rule application. Immutable class containing the rule name
 * and its analysis result.
 *
 * @since 1.0
 */
public class IndexingResult {

  /**
   * Rule name
   */
  private final String ruleName;

  /**
   * Analysis result
   */
  private final String result;

  /**
   * Creates a new result with rule name and analysis.
   */
  public IndexingResult(String ruleName, String result) {
    this.ruleName = ruleName;
    this.result = result;
  }

  /**
   * @return formatted string as [ruleName] result
   */
  @Override
  public String toString() {
    return String.format("%s %s", ruleName, result);
  }
}