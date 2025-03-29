package com.search.indexer.core;

import java.util.Set;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("WordStatistics Test Suite")
class WordStatisticsTest {

  private WordStatistics wordStatistics;

  private static Stream<Arguments> provideMixedCaseAndLengthWords() {
    return Stream.of(
        Arguments.of("Programming", true, true),   // uppercase and long
        Arguments.of("code", false, false),        // lowercase and short
        Arguments.of("Testing", true, true),       // uppercase and long
        Arguments.of("small", false, false),       // lowercase and short
        Arguments.of("CAPITAL", true, true),       // uppercase and long
        Arguments.of("Ab", true, false)            // uppercase and short
    );
  }

  @BeforeEach
  void setUp() {
    wordStatistics = new WordStatistics();
  }

  @Test
  @DisplayName("Should initialize with zero uppercase count and empty long words set")
  void shouldInitializeWithDefaultValues() {
    // Arrange - done in setUp

    // Act - object already created

    // Assert
    Assertions.assertThat(wordStatistics.getUppercaseCount())
        .as("Initial uppercase count should be zero")
        .isZero();

    Assertions.assertThat(wordStatistics.getLongWords())
        .as("Initial long words set should be empty")
        .isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"Hello", "World", "Test", "Java"})
  @DisplayName("Should increment uppercase count for words starting with uppercase letter")
  void shouldIncrementUppercaseCountForUppercaseWords(String word) {
    // Arrange - done in setUp

    // Act
    wordStatistics.processWord(word);

    // Assert
    Assertions.assertThat(wordStatistics.getUppercaseCount())
        .as("Uppercase count should be incremented for word: %s", word)
        .isEqualTo(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"hello", "world", "test", "java"})
  @DisplayName("Should not increment uppercase count for words starting with lowercase letter")
  void shouldNotIncrementUppercaseCountForLowercaseWords(String word) {
    // Arrange - done in setUp

    // Act
    wordStatistics.processWord(word);

    // Assert
    Assertions.assertThat(wordStatistics.getUppercaseCount())
        .as("Uppercase count should not be incremented for word: %s", word)
        .isZero();
  }

  @ParameterizedTest
  @ValueSource(strings = {"Programming", "Development", "Testing"})
  @DisplayName("Should add words longer than 5 characters to long words set")
  void shouldAddWordsLongerThanFiveCharsToLongWords(String word) {
    // Arrange - done in setUp

    // Act
    wordStatistics.processWord(word);

    // Assert
    Assertions.assertThat(wordStatistics.getLongWords())
        .as("Long words set should contain word: %s", word)
        .contains(word);
  }

  @ParameterizedTest
  @ValueSource(strings = {"code", "test", "java"})
  @DisplayName("Should not add words with 5 or fewer characters to long words set")
  void shouldNotAddShortWordsToLongWords(String word) {
    // Arrange - done in setUp

    // Act
    wordStatistics.processWord(word);

    // Assert
    Assertions.assertThat(wordStatistics.getLongWords())
        .as("Long words set should not contain short word: %s", word)
        .doesNotContain(word);
  }

  @ParameterizedTest
  @MethodSource("provideMixedCaseAndLengthWords")
  @DisplayName("Should correctly process words with mixed case and length characteristics")
  void shouldProcessMixedCaseAndLengthWords(String word, boolean shouldBeUppercase,
      boolean shouldBeLong) {
    // Arrange - done in setUp

    // Act
    wordStatistics.processWord(word);

    // Assert
    Assertions.assertThat(wordStatistics.getUppercaseCount())
        .as("Uppercase count should be %d for word: %s",
            shouldBeUppercase ? 1 : 0, word)
        .isEqualTo(shouldBeUppercase ? 1 : 0);

    Assertions.assertThat(wordStatistics.getLongWords().contains(word))
        .as("Long words set %s contain word: %s",
            shouldBeLong ? "should" : "should not", word)
        .isEqualTo(shouldBeLong);
  }

  @Test
  @DisplayName("Should maintain unique long words in set")
  void shouldMaintainUniqueLongWords() {
    // Arrange
    String longWord = "Testing";

    // Act
    wordStatistics.processWord(longWord);
    wordStatistics.processWord(longWord); // Add same word twice

    // Assert
    Assertions.assertThat(wordStatistics.getLongWords())
        .as("Long words set should contain only one instance of duplicate word")
        .hasSize(1)
        .contains(longWord);
  }

  @Test
  @DisplayName("Should handle multiple word processing correctly")
  void shouldHandleMultipleWordProcessing() {
    // Arrange
    String[] words = {"Hello", "world", "Programming", "test", "Development"};
    int expectedUppercase = 3; // Hello, Programming, Development
    Set<String> expectedLongWords = Set.of("Programming", "Development");

    // Act
    for (String word : words) {
      wordStatistics.processWord(word);
    }

    // Assert
    Assertions.assertThat(wordStatistics.getUppercaseCount())
        .as("Should count correct number of uppercase words")
        .isEqualTo(expectedUppercase);

    Assertions.assertThat(wordStatistics.getLongWords())
        .as("Should contain correct set of long words")
        .containsExactlyInAnyOrderElementsOf(expectedLongWords);
  }
}