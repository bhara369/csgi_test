package com.search.indexer.core;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import com.search.indexer.exception.FileProcessingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("FileProcessor Test Suite")
class FileProcessorTest {

  @TempDir
  Path tempDir;

  private FileProcessor fileProcessor;

  private static Stream<Arguments> provideFileContents() {
    return Stream.of(
        Arguments.of("Simple text content", "simple.txt"),
        Arguments.of("<html>HTML content</html>", "test.html"),
        Arguments.of("Mixed\nLine\nContent", "multiline.txt"),
        Arguments.of("Special!@#$%^&* chars", "special.txt")
    );
  }

  @BeforeEach
  void setUp() {
    fileProcessor = new FileProcessor();
  }

  @Test
  @DisplayName("Should throw FileProcessingException when file is not readable")
  void shouldThrowFileProcessingExceptionWhenFileIsNotReadable() throws IOException {
    // Arrange
    Path file = createTestFile("unreadable.txt", "test content");
    makeFileUnreadable(file);

    // Act & Assert
    assertThatThrownBy(() -> fileProcessor.processFiles(List.of(file)))
        .isInstanceOf(FileProcessingException.class)
        .hasMessageContaining("Failed to process file");
  }

  @Test
  @DisplayName("Should throw SecurityException when file does not exist")
  void shouldThrowSecurityExceptionWhenFileDoesNotExist() {
    // Arrange
    Path nonExistentFile = tempDir.resolve("nonexistent.txt");

    // Act & Assert
    assertThatThrownBy(() -> fileProcessor.processFiles(List.of(nonExistentFile)))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("Not a regular file");
  }

  @Test
  @DisplayName("Should process valid file successfully")
  void shouldProcessValidFileSuccessfully() throws IOException {
    // Arrange
    Path validFile = createTestFile("valid.txt", "Test content");

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(validFile)));
  }

  @Test
  @DisplayName("Should process multiple files successfully")
  void shouldProcessMultipleFilesSuccessfully() throws IOException {
    // Arrange
    List<Path> files = new ArrayList<>();
    files.add(createTestFile("file1.txt", "Content 1"));
    files.add(createTestFile("file2.txt", "Content 2"));

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(files));
  }

  @Test
  @DisplayName("Should handle empty file")
  void shouldHandleEmptyFile() throws IOException {
    // Arrange
    Path emptyFile = createTestFile("empty.txt", "");

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(emptyFile)));
  }

  @ParameterizedTest
  @MethodSource("provideFileContents")
  @DisplayName("Should process different file contents correctly")
  void shouldProcessDifferentFileContentsCorrectly(String content, String fileName)
      throws IOException {
    // Arrange
    Path testFile = createTestFile(fileName, content);

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(testFile)));
  }

  //@Test
  @DisplayName("Should throw SecurityException for oversized file")
  void shouldThrowSecurityExceptionForOversizedFile() throws IOException {
    // Arrange
    Path largeFile = createLargeFile("large.txt");

    // Act & Assert
    assertThatThrownBy(() -> fileProcessor.processFiles(List.of(largeFile)))
        .isInstanceOf(SecurityException.class)
        .hasMessageContaining("File too large");
  }

  // Helper methods
  private Path createTestFile(String fileName, String content) throws IOException {
    Path file = tempDir.resolve(fileName);
    Files.writeString(file, content);
    return file;
  }

  private Path createLargeFile(String fileName) throws IOException {
    Path file = tempDir.resolve(fileName);
    // Create a file larger than MAX_FILE_SIZE
    StringBuilder content = new StringBuilder();
    for (int i = 0; i < 1_000_000; i++) {
      content.append("Large file content line ").append(i).append("\n");
    }
    Files.writeString(file, content.toString());
    return file;
  }

  private void makeFileUnreadable(Path file) throws IOException {
    Set<PosixFilePermission> permissions = new HashSet<>();
    // Remove read permission
    Files.setPosixFilePermissions(file, permissions);
  }

  @Test
  @DisplayName("Should handle whitespace-only content")
  void shouldHandleWhitespaceOnlyContent() throws IOException {
    // Arrange
    Path whitespaceFile = createTestFile("whitespace.txt", "   \n  \t  \n");

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(whitespaceFile)));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException for empty file list")
  void shouldThrowIllegalArgumentExceptionForEmptyFileList() {
    // Act & Assert
    assertThatThrownBy(() -> fileProcessor.processFiles(new ArrayList<>()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("filePaths cannot be empty");
  }

  @Test
  @DisplayName("Should throw NullPointerException for null file list")
  void shouldThrowNullPointerExceptionForNullFileList() {
    // Act & Assert
    assertThatThrownBy(() -> fileProcessor.processFiles(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("filePaths cannot be null");
  }

  @Test
  @DisplayName("Should process file with mixed case words")
  void shouldProcessFileWithMixedCaseWords() throws IOException {
    // Arrange
    Path file = createTestFile("mixed.txt",
        "UPPERCASE\nlowercase\nMixedCase\nPascalCase");

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(file)));
  }

  @Test
  @DisplayName("Should process file with special characters")
  void shouldProcessFileWithSpecialCharacters() throws IOException {
    // Arrange
    Path file = createTestFile("special.txt",
        "Special!@#$%^&*\nCharacters\nTest");

    // Act & Assert
    assertThatNoException()
        .isThrownBy(() -> fileProcessor.processFiles(List.of(file)));
  }
}