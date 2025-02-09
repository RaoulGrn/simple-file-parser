

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.raoulgrn.core.Analytics;
import org.raoulgrn.core.interfaces.FileType;
import org.raoulgrn.core.models.AnalyticsResult;
import org.raoulgrn.core.models.Data;
import org.raoulgrn.core.monitor.Monitor;
import org.raoulgrn.source.FileSource;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {

    @Mock
    private Analytics analytics;

    @Mock
    private FileType fileType;

    @TempDir
    Path tempDir;

    private Monitor monitor;
    private Path unprocessedDir;
    private Path processedDir;
    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() throws Exception {
        mockitoCloseable = MockitoAnnotations.openMocks(this);


        unprocessedDir = tempDir.resolve("unprocessed");
        processedDir = unprocessedDir.resolve("processed");
        Files.createDirectories(unprocessedDir);
        Files.createDirectories(processedDir);


        when(fileType.validateFile(anyString())).thenReturn(true);


        AnalyticsResult mockResult = createMockResult();
        when(analytics.processData(any())).thenReturn(mockResult);


        monitor = new Monitor(analytics, fileType);
        injectTestPaths(monitor, unprocessedDir);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
        if (monitor != null) {
            monitor.stop();
        }
    }

    private void injectTestPaths(Monitor monitor, Path testDir) throws Exception {
        Field directoryField = Monitor.class.getDeclaredField("directory");
        directoryField.setAccessible(true);
        directoryField.set(monitor, testDir);

        Field processedDirectoryField = Monitor.class.getDeclaredField("processedDirectory");
        processedDirectoryField.setAccessible(true);
        processedDirectoryField.set(monitor, processedDir);
    }

    private AnalyticsResult createMockResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("WordCountProcessor", 100);
        resultMap.put("DotCountProcessor", 50);

        Map<String, Object> mostUsedWord = new HashMap<>();
        mostUsedWord.put("word", "test");
        mostUsedWord.put("count", 10);
        resultMap.put("MostUsedWordProcessor", mostUsedWord);

        return new AnalyticsResult(resultMap);
    }



    @Test
    void testInvalidFileIsNotProcessed() throws Exception {

        when(fileType.validateFile(anyString())).thenReturn(false);


        Path testFile = unprocessedDir.resolve("invalid.txt");
        Files.writeString(testFile, "Invalid content");


        Method processExistingFiles = Monitor.class.getDeclaredMethod("processExistingFiles");
        processExistingFiles.setAccessible(true);
        processExistingFiles.invoke(monitor);


        verify(fileType).validateFile(testFile.toString());
        verify(analytics, never()).processData(any(FileSource.class));

        assertTrue(Files.exists(testFile), "Invalid file should remain in original directory");
        assertFalse(Files.exists(processedDir.resolve("invalid.txt")),
                "Invalid file should not be moved to processed directory");
    }
}