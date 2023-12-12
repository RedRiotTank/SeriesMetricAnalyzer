package files;

import console.ConsoleProcessor;

import java.io.*;
import java.util.*;

public class FileProcessor {

    private static final Set<File> csvFiles = new HashSet<>();
    private static final Map<String, File> extendedCsvFiles = new HashMap<>();


    // ------------------- Methods -------------------//

    public static void folderInitializer(String folderPath) {
        File folder = validateFolder(folderPath);

        csvFiles.addAll(Arrays.asList(Objects.requireNonNull(folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv")))));

        if (csvFiles.isEmpty()) {
            handleEmptyFolder();
        }
    }

    public static void extendedFolderInitializer(String extendedFolderPath) {
        File folder = validateFolder(extendedFolderPath);

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith(".csv")) {
                String capNumber = file.getName().split("_")[0];
                extendedCsvFiles.put(capNumber, file);
            }
        }

        if (extendedCsvFiles.isEmpty()) {
            handleEmptyFolder();
        }
    }

    public static HashMap<String, String> generateSynonymMap(String path) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                map.put(parts[0].toLowerCase(), parts[1].toLowerCase());
            }
        }

        return map;
    }

    // ------------------- Internal -------------------//

    private static File validateFolder(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.isDirectory() || (!folderPath.endsWith("/") && !folderPath.endsWith("\\"))) {
            ConsoleProcessor.notFolder();
            ConsoleProcessor.printHelpMessage();
            throw new IllegalArgumentException("Invalid folder path");
        }

        return folder;
    }


    // ------------------- Exceptions -------------------//
    private static void handleEmptyFolder() {
        ConsoleProcessor.emptyFolder();
        throw new IllegalStateException("Folder is empty");
    }

    // ------------------- Getters -------------------//

    public static Set<File> getCsvFiles() {
        return Collections.unmodifiableSet(csvFiles);
    }

    public static Map<String, File> getExtendedCsvFiles() {
        return Collections.unmodifiableMap(extendedCsvFiles);
    }
}
