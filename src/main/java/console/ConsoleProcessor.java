package console;

public class ConsoleProcessor {

    public static void fileNotFound(String fileName) {
        System.out.println("Error: File " + fileName + " not found.");
        System.out.println("It will be ignored in the process.");
    }
    public static void printAverageCharacterNumber(Float average){
        System.out.println("Average character number is: " + average);
        System.out.println();
    }
    public static void printAverageVoteNumber(Float average){
        System.out.println("Average vote number is: " + average);
        System.out.println();
    }

    public static void PrintAverageSeriesRating(Float average){
        System.out.println("Average series metric is: " + average);
        System.out.println();
    }

    public static void emptyFolder(){
        System.err.println("Error: Folder is empty.");
    }

    public static void notFolder(){
        System.err.println("Error: Path is not a folder.");
    }

    public static void parameterError() {
        System.err.println("Error: Not enough parameters.");
    }

    public static void printHelpMessage() {
        System.out.println("Usage: java -jar SeriesMetricAnalyzer.jar <options> [folderPath]");
        System.out.println("Options:");

    }

}
