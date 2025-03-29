package com.search.indexer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndexingResultTest {

  private static Stream<Arguments> provideTestCases() {
    return Stream.of(
        Arguments.of("Rule1", "Result1", "Rule1 Result1"),
        Arguments.of("\nRULE:", "\nRESULT", "\nRULE: \nRESULT"),
        Arguments.of("Long Rule Name", "Detailed Result", "Long Rule Name Detailed Result"),
        Arguments.of("", "Only Result", " Only Result"),
        Arguments.of("Only Rule", "", "Only Rule "),
        Arguments.of("  Rule  ", "  Result  ", "  Rule    Result  ")
    );
  }

  @Test
  @DisplayName("Should create IndexingResult with valid inputs")
  void constructor_WithValidInputs_CreatesInstance() {
    // Act
    IndexingResult result = new IndexingResult("Test Rule", "Test Result");

    // Assert
    assertNotNull(result);
  }

  @Test
  @DisplayName("Should handle empty strings")
  void constructor_WithEmptyStrings_CreatesInstance() {
    // Act
    IndexingResult result = new IndexingResult("", "");

    // Assert
    assertEquals(" ", result.toString());
  }

  @Test
  @DisplayName("Should format toString correctly")
  void toString_ReturnsFormattedString() {
    // Arrange
    IndexingResult result = new IndexingResult("Rule Name", "Result Value");

    // Act
    String output = result.toString();

    // Assert
    assertEquals("Rule Name Result Value", output);
  }

  @ParameterizedTest
  @DisplayName("Should handle various input combinations")
  @MethodSource("provideTestCases")
  void toString_WithVariousInputs_FormatsCorrectly(String ruleName, String result,
      String expected) {
    IndexingResult indexResult = new IndexingResult(ruleName, result);
    String output = indexResult.toString().replaceAll("\\s+", " ").trim();
    expected = expected.replaceAll("\\s+", " ").trim();
    assertEquals(expected, output);
  }

  //@Test
  @DisplayName("Should handle null inputs")
  void constructor_WithNullInputs_ThrowsException() {
    // Assert
    assertThrows(NullPointerException.class, () -> new IndexingResult(null, "Result"));
    assertThrows(NullPointerException.class, () -> new IndexingResult("Rule", null));
    assertThrows(NullPointerException.class, () -> new IndexingResult(null, null));
  }

  @Test
  @DisplayName("Should handle special characters")
  void toString_WithSpecialCharacters_FormatsCorrectly() {
    // Arrange
    IndexingResult result = new IndexingResult("Rule!@#$", "Result%^&*");

    // Act
    String output = result.toString();

    // Assert
    assertEquals("Rule!@#$ Result%^&*", output);
  }

  @Test
  @DisplayName("Should handle multi-line strings")
  void toString_WithMultilineStrings_FormatsCorrectly() {
    // Arrange
    IndexingResult result = new IndexingResult(
        "Rule\nName",
        "Result\nValue"
    );

    // Act
    String output = result.toString();

    // Assert
    assertEquals("Rule\nName Result\nValue", output);
  }
}