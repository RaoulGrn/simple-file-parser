package org.raoulgrn.core.monitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.raoulgrn.core.Analytics;
import org.raoulgrn.core.interfaces.DataSource;
import org.raoulgrn.core.interfaces.FileType;
import org.raoulgrn.core.models.AnalyticsResult;
import org.raoulgrn.source.FileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);
    private final Analytics analytics;
    private final FileType fileType;
    private final Path processedDirectory;
    private final ConcurrentHashMap<String, Boolean> processingFiles;

    public FileProcessor(Analytics analytics, FileType fileType, Path processedDirectory) {
        this.analytics = analytics;
        this.fileType = fileType;
        this.processedDirectory = processedDirectory;
        this.processingFiles = new ConcurrentHashMap<>();
    }

    public void processFile(Path file) {
        String fileName = file.getFileName().toString();
        
     
        if (processingFiles.putIfAbsent(fileName, Boolean.TRUE) != null) {
            return;
        }

        try {
            if (file.getParent().equals(processedDirectory)) {
                return;
            }

            if (!Files.exists(file)) {
                return;
            }

            logger.info("Processing file: {}", fileName);

            DataSource source = new FileSource(fileType, file.toString());
            AnalyticsResult result = analytics.processData(source);

            logResults(file, result, source);
            
            if (Files.exists(file)) {
                moveToProcessed(file);
            }

        } catch (IOException e) {
            logger.error("Error processing file {}: {}", fileName, e.getMessage());
        } finally {
            processingFiles.remove(fileName);
        }
    }

    private void logResults(Path file, AnalyticsResult result, DataSource source) {
        logger.info("Results for {}:", file.getFileName());
        logger.info("  Words: {}", result.getResult().get("WordCountProcessor"));
        logger.info("  Dots: {}", result.getResult().get("DotCountProcessor"));

        Map<String, Object> mostUsed = (Map<String, Object>) result.getResult().get("MostUsedWordProcessor");
        logger.info("  Most used word: '{}' ({} times)",
                mostUsed.get("word"), mostUsed.get("count"));

        logger.info(" File metadata:");
        Map<String, Object> metadata = source.getMetadata();
        metadata.forEach((k, v) -> logger.info("  {}: {}", k, v));
    }

    private void moveToProcessed(Path file) throws IOException {
        Path targetPath = processedDirectory.resolve(file.getFileName());
        Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File moved to processed directory");
    }

    public boolean shouldProcess(Path file) {
        return Files.isRegularFile(file) &&
                fileType.validateFile(file.toString()) &&
                !isInProcessedDirectory(file);
    }

    private boolean isInProcessedDirectory(Path file) {
        try {
            return file.toRealPath().startsWith(processedDirectory.toRealPath());
        } catch (IOException e) {
            logger.error("Error resolving path: {}", e.getMessage());
            return false;
        }
    }
} 