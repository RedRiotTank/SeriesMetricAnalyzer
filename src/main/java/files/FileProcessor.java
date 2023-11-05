package files;

import console.ConsoleProcessor;

import java.io.*;
import java.util.*;

public class FileProcessor {

    private static final Set<File> csvFiles = new HashSet<>();
    private static final Map<String, File> extendedCsvFiles = new HashMap<>();
    public static void folderInitializer(String folderPath) {
        File folder = isFolder(folderPath);

        Collections.addAll(csvFiles, Objects.requireNonNull(folder.listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".csv")
        )));

        if(csvFiles.isEmpty()){
            ConsoleProcessor.emptyFolder();
            System.exit(1);
        }
    }

    public static void extendedFolderInitializer(String extendedFolderPath){
        File folder = isFolder(extendedFolderPath);

        for(File file : folder.listFiles()){
            if (file.getName().endsWith(".csv")){
                String capNumber = file.getName().split("_")[0];
                extendedCsvFiles.put(capNumber, file);
            }
        }

        if(extendedCsvFiles.isEmpty()){
            ConsoleProcessor.emptyFolder();
            System.exit(1);
        }
    }

    public static HashMap<String, String> generateSynomMap(String path) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String linea;

        while ((linea = br.readLine()) != null) {

            String[] line = linea.split(":");
            map.put(line[0].toLowerCase(), line[1].toLowerCase());

        }

        return map;
    }


    public static File isFolder(String folderPath){
        File folder = new File(folderPath);

        if (!folder.isDirectory() || (!folderPath.endsWith("/") && !folderPath.endsWith("\\"))) {
            ConsoleProcessor.notFolder();
            ConsoleProcessor.printHelpMessage();
            System.exit(1);
        }
        return folder;
    }

    public static Set<File> getCsvFiles() {
        return csvFiles;
    }

    public static Map<String, File> getExtendedCsvFiles() {
        return extendedCsvFiles;
    }

}
