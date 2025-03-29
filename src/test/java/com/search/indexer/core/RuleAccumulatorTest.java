package com.search.indexer.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.search.indexer.model.IndexingResult;
import com.search.indexer.rules.IndexingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RuleAccumulatorTest {

  @Mock
  private IndexingRule mockRule;

  private RuleAccumulator ruleAccumulator;

  @BeforeEach
  void setUp() {
    ruleAccumulator = new RuleAccumulator(mockRule);
  }

  @Test
  @DisplayName("Should create RuleAccumulator with provided rule")
  void shouldCreateRuleAccumulatorWithProvidedRule() {
    // Act & Assert
    assertThat(ruleAccumulator)
        .isNotNull()
        .withFailMessage("RuleAccumulator should be created successfully");
  }

  @Test
  @DisplayName("Should throw NullPointerException when created with null rule")
  void shouldThrowExceptionWhenCreatedWithNullRule() {
    // Act & Assert
    assertThatThrownBy(() -> new RuleAccumulator(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("IndexingRule cannot be null")
        .withFailMessage(
            "Should throw NullPointerException with appropriate message for null rule");
  }

  @ParameterizedTest
  @ValueSource(strings = {"Hello", "World", "Test", "Example"})
  @DisplayName("Should process valid words successfully")
  void shouldProcessValidWords(String word) {
    // Arrange
    IndexingResult mockResult = mock(IndexingResult.class);
    when(mockRule.process(any(WordStatistics.class))).thenReturn(mockResult);

    // Act
    ruleAccumulator.processWord(word);
    IndexingResult result = ruleAccumulator.getResult();

    // Assert
    assertThat(result).isNotNull();
    verify(mockRule).process(any(WordStatistics.class));
  }


  @Test
  @DisplayName("Should accumulate multiple words correctly")
  void shouldAccumulateMultipleWordsCorrectly() {
    // Arrange
    IndexingResult mockResult = mock(IndexingResult.class);
    when(mockRule.process(any(WordStatistics.class))).thenReturn(mockResult);

    // Act
    ruleAccumulator.processWord("First");
    ruleAccumulator.processWord("Second");
    ruleAccumulator.processWord("Third");
    IndexingResult result = ruleAccumulator.getResult();

    // Assert
    assertThat(result).isNotNull();
    verify(mockRule, times(1)).process(any(WordStatistics.class));
  }

  @Test
  @DisplayName("Should handle rule processing exception")
  void shouldHandleRuleProcessingException() {
    // Arrange
    when(mockRule.process(any(WordStatistics.class)))
        .thenThrow(new RuntimeException("Processing failed"));

    // Act & Assert
    assertThatThrownBy(() -> ruleAccumulator.getResult())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Processing failed");
  }

  @Test
  @DisplayName("Should maintain word statistics state between calls")
  void shouldMaintainWordStatisticsBetweenCalls() {
    // Arrange
    IndexingResult mockResult = mock(IndexingResult.class);
    when(mockRule.process(any(WordStatistics.class))).thenReturn(mockResult);

    // Act
    ruleAccumulator.processWord("First");
    ruleAccumulator.processWord("Second");
    IndexingResult result = ruleAccumulator.getResult();

    // Assert
    assertThat(result).isNotNull();
    verify(mockRule, times(1)).process(any(WordStatistics.class));
  }
}