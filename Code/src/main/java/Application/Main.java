package Application;

import com.LogComponent;

public class Main {
    public static void main(String[] args) {
        // Create an instance of LogComponent
        LogComponent logComponent = new LogComponent();
        logComponent.write("Log entry");

        // Stop the LogComponent (wait for outstanding logs to be written)
        logComponent.stop(true);

        // To stop immediately without waiting for logs to be written:
        // logComponent.stop(false);
    }
}