package com;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

public class LogWorker implements Runnable {
    private static final String LOG_DIR = "./logs/";
    private static final String LOG_FILE_PREFIX = "log_";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final int LOG_INTERVAL_MS = 1000;

    private final Queue<String> logQueue;
    private volatile boolean isRunning;
    
    // Thread logThread;
    public LogWorker(Queue<String> logQueue) {
        this.logQueue = logQueue;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            Date currentDate = new Date();
            String logFilePath = LOG_DIR + LOG_FILE_PREFIX + DATE_FORMAT.format(currentDate) + ".log";
            try (FileWriter writer = new FileWriter(logFilePath, true)) {
                while (!logQueue.isEmpty()) {
                    String message = logQueue.poll();
                    writer.write(currentDate + " - " + message + "\n");
                }
                writer.flush();
                Thread.sleep(LOG_INTERVAL_MS);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void join(Thread logThread) throws InterruptedException {
        if (logThread != null) {
            logThread.join();
        }
    }
}
