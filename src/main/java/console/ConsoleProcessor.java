package console;

public class ConsoleProcessor {

    public static void printAveracheCharacterNumber(Float average){
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
        // Print a help message with available options.
        System.out.println("Usage: java -jar SeriesMetricAnalyzer.jar <options> [folderPath]"); //folderpath = allepisodes
        System.out.println("Options:");

    }

}
