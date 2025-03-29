#!/bin/bash

# create-bundle.sh
echo "Creating Git bundle for File Indexer project..."

# Initialize Git if not already initialized
if [ ! -d .git ]; then
    git init
    echo "Initialized Git repository"
fi

# Create/Update .gitignore
cat > .gitignore << 'EOF'
target/
.idea/
*.iml
.settings/
.project
.classpath
.vscode/
*.class
*.log
dependency-reduced-pom.xml
.DS_Store
EOF

# Create README.md
cat > README.md << 'EOF'
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

## Author
[Your Name]

## License
This project is licensed under the MIT License - see the LICENSE file for details
EOF

# Create LICENSE file
cat > LICENSE << 'EOF'
MIT License

Copyright (c) 2024 [Your Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
EOF

# Add all files
git add .

# Commit changes
git commit -m "Initial commit: File Indexer implementation

- Added main application classes
- Added test cases
- Added documentation
- Added build configuration
- Added license"

# Create the bundle
git bundle create file-indexer.bundle HEAD master

# Create verification script
cat > verify-bundle.sh << 'EOF'
#!/bin/bash

echo "Verifying Git bundle..."
git bundle verify file-indexer.bundle

if [ $? -eq 0 ]; then
    echo "Bundle verification successful!"
    echo "To clone this repository:"
    echo "git clone file-indexer.bundle file-indexer"
    echo
    echo "Or to pull into an existing repository:"
    echo "git pull file-indexer.bundle master"
else
    echo "Bundle verification failed!"
fi
EOF

# Make verification script executable
chmod +x verify-bundle.sh

echo "Git bundle created successfully as 'file-indexer.bundle'"
echo "Use verify-bundle.sh to verify the bundle"
echo
echo "To clone the repository from the bundle:"
echo "git clone file-indexer.bundle file-indexer"
echo
echo "Or to pull into an existing repository:"
echo "git pull file-indexer.bundle master"