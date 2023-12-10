package Application;
import java.io.IOException;
import java.nio.file.*;
// import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

public class test {
    public static void main(String[] args) {
        // Specify the directory path
        Path directoryPath = Paths.get("./");

        try {
            // Get the most recent file in the directory
            Path mostRecentFile = getMostRecentFile(directoryPath);

            // Print the name of the most recent file
            if (mostRecentFile != null) {
                System.out.println("Most recent file: " + mostRecentFile.getFileName());
            } else {
                System.out.println("No files found in the directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getMostRecentFile(Path directoryPath) throws IOException {
        // Use Files.walk to traverse the directory and find all files
        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .max(Comparator.comparingLong(file -> file.toFile().lastModified()))
                .orElse(null);
    }
}
