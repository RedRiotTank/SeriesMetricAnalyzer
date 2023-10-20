package files;

import console.ConsoleProcessor;

import java.io.File;
import java.util.*;

public class FileProcessor {

    private static final Set<File> csvFiles = new HashSet<>();
    public static void folderInitializer(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.isDirectory() || (!folderPath.endsWith("/") && !folderPath.endsWith("\\"))) {
            ConsoleProcessor.notFolder();
            ConsoleProcessor.printHelpMessage();
            System.exit(1);
        }

        Collections.addAll(csvFiles, Objects.requireNonNull(folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".csv")
        )));

        if(csvFiles.isEmpty()){
            ConsoleProcessor.emptyFolder();
            System.exit(1);
        }
    }


    public static Set<File> getCsvFiles() {
        return csvFiles;
    }

}
