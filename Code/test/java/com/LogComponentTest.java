package com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LogComponentTest {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final String LOG_DIR = "./logs/";
    private LogComponent logComponent;

    @BeforeEach
    void setUp() {
        logComponent = new LogComponent();
    }

    @AfterEach
    void tearDown() {
        logComponent.stop(true);
    }

    @Test
    void testWrite() throws IOException {
        String logMessage = "Test log entry";
        logComponent.write(logMessage);

        // Assert
        String today = DATE_FORMAT.format(new Date());
        String logFilePath = LOG_DIR + "log_" + today + ".log";
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line = reader.readLine();
            assertTrue(line.contains(logMessage));
        }
    }


    

    private static Path getMostRecentFile(Path directoryPath) throws IOException {
        // Use Files.walk to traverse the directory and find all files
        return Files.walk(directoryPath).filter(Files::isRegularFile).max(Comparator.comparingLong(file -> file.toFile().lastModified())).orElse(null);
    }

    @Test
    void testNewFilesCreatedOnMidnightCross() throws InterruptedException, IOException {
        // Act (Sleep until midnight)
        Thread.sleep(getTimeTillMidnight() * 1000);

        // Assert
        logComponent.write("Test New File Creation");
        String today = DATE_FORMAT.format(new Date());
        String logFilePathAfterMidnight = LOG_DIR + "log_" + today + ".log";

        Path directoryPath = Paths.get("./");
        Path mostRecentFile = getMostRecentFile(directoryPath);
        if (mostRecentFile != null) {
            // System.out.println("Most recent file: " + mostRecentFile.getFileName());
            assertTrue(logFilePathAfterMidnight.equals(mostRecentFile.getFileName().toString()));
        }
    }

    @Test
    void testStopImmediately() throws InterruptedException {
        logComponent.stop(false);
        // Wait for a short time to ensure the log thread stops
        Thread.sleep(1000);
        assertTrue(!((Thread) logComponent.logThread).isAlive());
    }

    @Test
    void testStopAndWait() throws InterruptedException {
        logComponent.stop(true);
        assertTrue(!((Thread) logComponent.logThread).isAlive());
    }

    private long getTimeTillMidnight() {
        int seconds = LocalTime.now().getHour() * 3600 + LocalTime.now().getMinute() * 60 + LocalTime.now().getSecond();
        return 86400 - seconds;
    }
}
