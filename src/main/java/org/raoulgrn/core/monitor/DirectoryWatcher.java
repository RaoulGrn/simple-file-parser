package org.raoulgrn.core.monitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryWatcher {
    private static final Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);
    
    private final WatchService watchService;
    private final Path directory;
    private final FileProcessor fileProcessor;
    private final AtomicBoolean isRunning;

    public DirectoryWatcher(Path directory, FileProcessor fileProcessor) throws IOException {
        if (!Files.exists(directory)) {
            throw new IOException("Directory does not exist: " + directory);
        }
        
        this.directory = directory;
        this.fileProcessor = fileProcessor;
        this.watchService = FileSystems.getDefault().newWatchService();
        this.isRunning = new AtomicBoolean(false);

        directory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);
                
        logger.info("Directory watcher initialized for: {}", directory);
    }

    public void startWatching() {
        if (!Files.exists(directory)) {
            logger.error("Cannot start watching - directory does not exist: {}", directory);
            return;
        }

        isRunning.set(true);
        logger.info("Started monitoring folder: {}", directory);

        while(isRunning.get()) {
            try {
                WatchKey key = watchService.take();
                for(WatchEvent<?> event : key.pollEvents()) {
                    Path file = directory.resolve((Path) event.context());
                    if (fileProcessor.shouldProcess(file)) {
                        fileProcessor.processFile(file);
                    }
                }
                key.reset();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
            } catch (Exception e) {
                logger.error("Error monitoring folder: {}", e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning.set(false);
        try {
            watchService.close();
            logger.info("Directory watcher stopped for: {}", directory);
        } catch (IOException e) {
            logger.error("Error stopping service: {}", e.getMessage());
        }
    }
} 