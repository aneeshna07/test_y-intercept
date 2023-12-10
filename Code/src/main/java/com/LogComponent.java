// package com;

// import java.io.FileWriter;
// import java.io.IOException;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.Queue;
// import java.util.concurrent.ConcurrentLinkedQueue;


// public class LogComponent {
//     private static final String LOG_DIR = "./logs/";
//     private static final String LOG_FILE_PREFIX = "log_";
//     private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
//     private static final int LOG_INTERVAL = 1000;

//     private final Queue<String> logQueue;
//     private volatile boolean isRunning;
//     Thread logThread;

//     public LogComponent() {
//         this.logQueue = new ConcurrentLinkedQueue<>();
//         this.isRunning = true;
//         // Start a background thread for asynchronous logging
//         this.logThread = new Thread(this::logWorker);
//         this.logThread.start();
//     }

//     public void write(String message) {
//         logQueue.add(message);
//     }

//     private void logWorker() {
//         while (isRunning) {
//             Date currentDate = new Date();
//             String logFilePath = LOG_DIR + LOG_FILE_PREFIX + DATE_FORMAT.format(currentDate) + ".log";

//             try (FileWriter writer = new FileWriter(logFilePath, true)) {
//                 while (!logQueue.isEmpty()) {
//                     String message = logQueue.poll();
//                     writer.write(currentDate + " - " + message + "\n");
//                 }
//                 writer.flush();

//                 // Sleep for a short interval to avoid continuous checking
//                 Thread.sleep(LOG_INTERVAL);
//             } catch (IOException | InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     public void stop(boolean wait) {
//         isRunning = false;

//         if (wait) {
//             try {
//                 logThread.join();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     // public static void main(String[] args) {
//     //     LogComponent logComponent = new LogComponent();

//     //     logComponent.write("Log entry 1");
//     //     logComponent.write("Log entry 2");

//     //     // To stop immediately without waiting for logs to be written:
//     //     // logComponent.stop(false);

//     //     // To stop and wait for outstanding logs to be written:
//     //     logComponent.stop(true);
//     // }
// }
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
