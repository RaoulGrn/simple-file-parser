package org.raoulgrn.core.monitor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.raoulgrn.core.Analytics;
import org.raoulgrn.core.interfaces.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monitor {
    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);

    private final Path directory;
    private final Path processedDirectory;
    private final FileProcessor fileProcessor;
    private final DirectoryWatcher directoryWatcher;

    public Monitor(Analytics analytics, FileType fileType) throws IOException {
        this.directory = Paths.get("src", "main", "resources", "unprocessed");
        this.processedDirectory = Paths.get("src", "main", "resources", "unprocessed", "processed");
        
      
        initializeDirectories();
        
        this.fileProcessor = new FileProcessor(analytics, fileType, processedDirectory);
        this.directoryWatcher = new DirectoryWatcher(directory, fileProcessor);
    }

    private void initializeDirectories() throws IOException {
    
        Files.createDirectories(directory);
        Files.createDirectories(processedDirectory);
        

        if (!Files.exists(directory) || !Files.exists(processedDirectory)) {
            throw new IOException("Failed to create required directories");
        }
        
        logger.info("Initialized directories: {} and {}", directory, processedDirectory);
    }

    public void startMonitor() throws IOException {
    
        processExistingFiles();
        
   
        directoryWatcher.startWatching();
    }

    private void processExistingFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (path.equals(processedDirectory)) {
                    continue;
                }
                
                if (fileProcessor.shouldProcess(path)) {
                    fileProcessor.processFile(path);
                }
            }
        } catch (IOException e) {
            logger.error("Error processing existing files: {}", e.getMessage());
        }
    }

    public void stop() {
        directoryWatcher.stop();
    }
}