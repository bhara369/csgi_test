package com.search.indexer.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TextCleaner Tests")
class TextCleanerTest {

  private static Stream<Arguments> provideHtmlTagFormats() {
    return Stream.of(
        // Simple tags
        Arguments.of("<div>Content</div>", "Content"),
        Arguments.of("<span>Text</span>", "Text"),

        // Nested tags
        Arguments.of("<div><p>Nested content</p></div>", "Nested content"),
        Arguments.of("<div><span><p>Deep nested</p></span></div>", "Deep nested"),

        // Tags with attributes
        Arguments.of("<div class='test'>With attributes</div>", "With attributes"),
        Arguments.of("<p style='color: red;'>Styled text</p>", "Styled text"),
        Arguments.of("<a href='http://example.com'>Link text</a>", "Link text"),

        // Self-closing tags
        Arguments.of("Before<br/>After", "BeforeAfter"),
        Arguments.of("Text<img src='image.jpg'/>More text", "TextMore text"),

        // Mixed content
        Arguments.of("<div>Start</div>Middle<p>End</p>", "StartMiddleEnd"),

        // Special characters in attributes
        Arguments.of("<div title=\"Quote's test\">Content</div>", "Content"),
        Arguments.of("<div data-test='<>'>Content</div>", "Content")
    );
  }

  private static Stream<Arguments> provideSpecialHtmlContent() {
    return Stream.of(
        // HTML Comments
        Arguments.of("<!-- comment -->Content", "Content"),
        Arguments.of("Before<!-- comment -->After", "BeforeAfter"),

        // CDATA sections
        Arguments.of("<![CDATA[Some content]]>", ""),

        // Script tags
        Arguments.of("<script>var x = 10;</script>Text", "Text"),

        // Style tags
        Arguments.of("<style>.class { color: red; }</style>Content", "Content"),

        // Special characters
        Arguments.of("<div>&nbsp;&lt;&gt;</div>", "&nbsp;&lt;&gt;"),

        // Mixed case tags
        Arguments.of("<DiV>Mixed case</dIv>", "Mixed case")
    );
  }

  @Test
  @DisplayName("Should remove simple HTML tags")
  void shouldRemoveSimpleHtmlTags() {
    // Arrange
    String input = "<p>Hello World</p>";

    // Act
    String result = TextCleaner.removeHtmlTags(input);

    // Assert
    assertThat(result).isEqualTo("Hello World");
  }


  @ParameterizedTest(name = "Should handle whitespace: {0}")
  @DisplayName("Should preserve whitespace")
  @ValueSource(strings = {
      "<p>  Spaces  </p>",
      "<div>\tTab\t</div>",
      "<span>\nNewline\n</span>",
      "<p>  Multiple     Spaces  </p>"
  })
  void shouldPreserveWhitespace(String input) {
    // Act
    String result = TextCleaner.removeHtmlTags(input);

    // Assert
    assertThat(result)
        .containsWhitespaces()
        .isEqualTo(input.replaceAll("<[^>]*>", ""));
  }

  @Test
  @DisplayName("Should handle text without HTML tags")
  void shouldHandleTextWithoutHtmlTags() {
    // Arrange
    String input = "Plain text without any tags";

    // Act
    String result = TextCleaner.removeHtmlTags(input);

    // Assert
    assertThat(result).isEqualTo(input);
  }


  @Test
  @DisplayName("Should handle malformed HTML")
  void shouldHandleMalformedHtml() {
    // Arrange
    String[] malformedInputs = {
        "<unclosed>Text",
        "Text</unopened>",
        "<tag<nested>Text</nested>tag>",
        ">Text<",
        "Text < with > brackets"
    };

    // Act & Assert
    for (String input : malformedInputs) {
      String result = TextCleaner.removeHtmlTags(input);
      assertThat(result).isNotEmpty();
    }
  }

  @Test
  @DisplayName("Should throw NullPointerException for null input")
  void shouldThrowExceptionForNullInput() {
    // Act & Assert
    assertThatThrownBy(() -> TextCleaner.removeHtmlTags(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should handle empty input")
  void shouldHandleEmptyInput() {
    // Arrange
    String input = "";

    // Act
    String result = TextCleaner.removeHtmlTags(input);

    // Assert
    assertThat(result)
        .isNotNull()
        .isEmpty();
  }

  @Test
  @DisplayName("Should handle very large input")
  void shouldHandleVeryLargeInput() {
    // Arrange
    StringBuilder largeInput = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      largeInput.append("<div>Content ").append(i).append("</div>");
    }

    // Act
    String result = TextCleaner.removeHtmlTags(largeInput.toString());

    // Assert
    assertThat(result)
        .doesNotContain("<div>", "</div>")
        .contains("Content 0")
        .contains("Content 999");
  }

  @Test
  @DisplayName("Should preserve Unicode characters")
  void shouldPreserveUnicodeCharacters() {
    // Arrange
    String input = "<div>Hello 世界</div><p>こんにちは</p>";

    // Act
    String result = TextCleaner.removeHtmlTags(input);

    // Assert
    assertThat(result)
        .isEqualTo("Hello 世界こんにちは")
        .containsOnlyOnce("世界")
        .containsOnlyOnce("こんにちは");
  }
}
