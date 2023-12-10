package com;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogComponent {
    private final Queue<String> logQueue;
    private final LogWorker logWorker;
    public Thread logThread;

    public LogComponent() {
        this.logQueue = new ConcurrentLinkedQueue<>();
        this.logWorker = new LogWorker(logQueue);

        Thread logThread = new Thread(logWorker);
        logThread.start();
    }

    public void write(String message) {
        logQueue.add(message);
    }

    public void stop(boolean wait) {
        logWorker.stop();

        if (wait) {
            try {
                logWorker.join(logThread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
