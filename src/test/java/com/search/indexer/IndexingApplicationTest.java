package com.search.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.search.indexer.core.FileProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndexingApplicationTest {

  @Mock
  private FileProcessor mockFileProcessor;

  @Test
  @DisplayName("Should throw exception when no arguments are provided")
  void shouldThrowExceptionWhenNoArgumentsProvided() {
    Exception exception = assertThrows(Exception.class, () -> {
      IndexingApplication.main(new String[] {});
    });

    assertEquals("Please provide at least one file path as argument", exception.getMessage());
  }

  @Test
  @DisplayName("Should process files when valid paths are provided")
  void shouldProcessFilesWhenValidPathsProvided() throws Exception {
    IndexingApplication.setProcessor(mockFileProcessor);
    // Mocking the FileProcessor to avoid actual file operations
    doNothing().when(mockFileProcessor).processFiles(anyList());

    // Execute the main method with test file paths
    String[] args =
        {"src/test/resources/sample-files/test1.txt", "src/test/resources/sample-files/test2.txt"};
    IndexingApplication.main(args);

    // Verify that processFiles was called once
    verify(mockFileProcessor, times(1)).processFiles(anyList());
  }
}