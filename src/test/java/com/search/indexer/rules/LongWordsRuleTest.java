package com.search.indexer.rules;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.search.indexer.core.WordStatistics;
import com.search.indexer.model.IndexingResult;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LongWordsRuleTest {

  private LongWordsRule rule;
  private WordStatistics statistics;

  @BeforeEach
  void setUp() {
    rule = new LongWordsRule();
    statistics = mock(WordStatistics.class);
  }

  @Test
  @DisplayName("Should return empty list when no long words exist")
  void process_NoLongWords_ReturnsEmptyList() {
    // Arrange
    Set<String> emptySet = new HashSet<>();
    when(statistics.getLongWords()).thenReturn(emptySet);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("Words longer than"));
    assertTrue(result.toString().endsWith(": "));
    verify(statistics).getLongWords();
  }

  @Test
  @DisplayName("Should return single word in sorted order")
  void process_SingleLongWord_ReturnsSingleWord() {
    // Arrange
    Set<String> singleWord = new HashSet<>();
    singleWord.add("Testing");
    when(statistics.getLongWords()).thenReturn(singleWord);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("Testing"));
    verify(statistics).getLongWords();
  }

  @Test
  @DisplayName("Should return multiple words in sorted order")
  void process_MultipleLongWords_ReturnsSortedList() {
    // Arrange
    Set<String> words = new HashSet<>();
    words.add("Zebra");
    words.add("Apple");
    words.add("Banana");
    when(statistics.getLongWords()).thenReturn(words);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    String resultStr = result.toString();
    assertTrue(resultStr.contains("Apple, Banana, Zebra"));
    verify(statistics).getLongWords();
  }

  @Test
  @DisplayName("Should include correct rule name in result")
  void process_VerifyRuleName() {
    // Arrange
    when(statistics.getLongWords()).thenReturn(new HashSet<>());

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertTrue(result.toString().contains("RULE : Long Words List"));
  }

  @Test
  @DisplayName("Should handle words with special characters")
  void process_SpecialCharacters_HandlesCorrectly() {
    // Arrange
    Set<String> words = new HashSet<>();
    words.add("Test-Case");
    words.add("Hello_World");
    when(statistics.getLongWords()).thenReturn(words);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    String resultStr = result.toString();
    assertTrue(resultStr.contains("Hello_World, Test-Case"));
    verify(statistics).getLongWords();
  }

  @Test
  @DisplayName("Should handle large number of words")
  void process_LargeWordList_HandlesCorrectly() {
    // Arrange
    Set<String> words = new HashSet<>();
    for (int i = 0; i < 1000; i++) {
      words.add("Word" + String.format("%03d", i));
    }
    when(statistics.getLongWords()).thenReturn(words);

    // Act
    IndexingResult result = rule.process(statistics);

    // Assert
    assertNotNull(result);
    assertTrue(result.toString().contains("Word000"));
    assertTrue(result.toString().contains("Word999"));
    verify(statistics).getLongWords();
  }
}