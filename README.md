# File Indexer

A Java application that processes files to count uppercase words and identify long words.

## Features
- Processes multiple files concurrently
- Counts uppercase words
- Identifies long words
- Performance monitoring
- HTML tag cleaning

## Requirements
- Java 17 or higher
- Maven 3.6 or higher

## Building the Project
```bash
mvn clean package
```

## Running the Application
```bash
java -jar target/file-indexer-1.0-SNAPSHOT-jar-with-dependencies.jar <file1> <file2> ...
```

## Running Tests
```bash
mvn clean test
```

## Code Coverage
JaCoCo coverage reports can be found in `target/site/jacoco/index.html` after running tests.

## Project Structure
- `src/main/java/com/search/indexer/` - Main source code
- `src/test/java/com/search/indexer/` - Test cases
- `src/main/resources/` - Configuration files

## Classes
- `IndexingApplication` - Main application class
- `FileProcessor` - Handles file processing
- `RuleAccumulator` - Accumulates rule results
- `WordStatistics` - Tracks word statistics
- `TextCleaner` - Cleans HTML tags
- `PerformanceMonitor` - Monitors performance
