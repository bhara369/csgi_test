package com.search.indexer.rules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.search.indexer.core.WordStatistics;
import com.search.indexer.model.IndexingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UppercaseWordsRuleTest {

  private UppercaseWordsRule rule;
  private WordStatistics statistics;

  @BeforeEach
  void setUp() {
    rule = new UppercaseWordsRule();
    statistics = mock(WordStatistics.class);
  }

  @Test
  @DisplayName("Should count zero uppercase words when none exist")
  void process_NoUppercaseWords_ReturnsZeroCount() {
    // Arrange
    when(statistics.getUppercaseCount()).thenReturn(0);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("Number of words starting with uppercase: 0"));
    verify(statistics).getUppercaseCount();
  }

  @Test
  @DisplayName("Should count single uppercase word")
  void process_SingleUppercaseWord_ReturnsOneCount() {
    // Arrange
    when(statistics.getUppercaseCount()).thenReturn(1);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("Number of words starting with uppercase: 1"));
    verify(statistics).getUppercaseCount();
  }

  @Test
  @DisplayName("Should count multiple uppercase words")
  void process_MultipleUppercaseWords_ReturnsCorrectCount() {
    // Arrange
    when(statistics.getUppercaseCount()).thenReturn(5);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("Number of words starting with uppercase: 5"));
    verify(statistics).getUppercaseCount();
  }

  @Test
  @DisplayName("Should include correct rule name in result")
  void process_VerifyRuleName() {
    // Arrange
    when(statistics.getUppercaseCount()).thenReturn(0);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("RULE : Uppercase Words Count"));
  }

  @Test
  @DisplayName("Should handle maximum integer value")
  void process_MaxIntegerValue_HandlesCorrectly() {
    // Arrange
    when(statistics.getUppercaseCount()).thenReturn(Integer.MAX_VALUE);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString()
        .contains("Number of words starting with uppercase: " + Integer.MAX_VALUE));
    verify(statistics).getUppercaseCount();
  }
}