# Simple File Parser
A smart document analyzer.

## How to run

1. Clone the repository
2. Build the project with Maven:
```
mvn clean install
```
3. You have three options for running the application:
   
  * Using Maven exec plugin:
```
mvn clean compile exec:java
```
  * Building and running the JAR:
```
mvn clean package

java -jar target/simple-file-parser-1.0-SNAPSHOT.jar
```

  * Or press Run if your IDE allows it.

## Usage

* Place any '.txt' files in the 'unprocessed' folder.
  
###  The system will automatically

* Detect new files
* Process files using configured analyzers
* Generate analytics result
* Move processed files to the 'unprocessed' directory

## Technologies && Dependencies

* This project was built using Java 17 and Maven for dependency management

* Other dependencies:
  - SLF4J/Logback - Logging framework
  - Apache Commons IO - File operations utilities
  - JUnit5 Jupiter - Testing framework
  - Mockito - Mocking framework for tests
 
### Analytics provided
- Word count
- Dot count
- Most used word and its frequency
- File metadata including parsing time

### Demo

 <img src="https://github.com/user-attachments/assets/06740fe4-bfc1-4411-968a-53ff86449a45" alt="Demo SFP1" width="800"/>


