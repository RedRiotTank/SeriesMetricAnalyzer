package org.htt;

import console.ConsoleProcessor;
import csvmetricprocessor.CsvMetricProcessor;
import customanalyzers.SynomAnalyzer;
import files.FileProcessor;
import indexprocessor.Indexer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Objects;

import static console.ConsoleProcessor.*;
import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length == 0) {
            parameterError();
            printHelpMessage();
            return;
        }

        String lastArg = args[args.length - 1];
        SynomAnalyzer characterAnalyzer = null;
        String extendedFolderPath = null;

        boolean
                averageVotes = false,
                averageMetric = false,
                averageCharacterNumber = false,
                generateIndex = false ;


        for(int i=0; i< args.length-1; i++){
            String arg = args[i];


            switch (arg) {
                case "-ef":
                    extendedFolderPath = args[i+1];
                    i++;
                    break;

                case "-V": // average votes
                    averageVotes = true;
                    break;

                case "-R":  // average metric
                    averageMetric = true;
                    break;

                case "-C": // average character number
                    averageCharacterNumber = true;
                    break;

                case "-I":
                    generateIndex = true;
                    break;
            }

        }

        ClassLoader classLoader = Main.class.getClassLoader();
        URL charactersURL = classLoader.getResource("characters.chs");
        String charactersPath = null, locationsPath = null;
        if(charactersURL == null){
            ConsoleProcessor.fileNotFound("characters.chs");
        } else {
            charactersPath = charactersURL.getFile();
        }

        URL locationsURL = classLoader.getResource("locations.lcs");

        if(locationsURL == null){
            ConsoleProcessor.fileNotFound("locations.lcs");
        } else {
            locationsPath = locationsURL.getFile();
        }

        if(charactersPath != null)
            CsvMetricProcessor.setCharacterAnalyzer(new SynomAnalyzer(FileProcessor.generateSynomMap(charactersPath)));

        if(locationsPath != null)
            CsvMetricProcessor.setLocationAnalyzer(new SynomAnalyzer(FileProcessor.generateSynomMap(locationsPath)));


        FileProcessor.folderInitializer(lastArg);

        if(extendedFolderPath != null)
            FileProcessor.extendedFolderInitializer(extendedFolderPath);

        CsvMetricProcessor.loadEpisodeDataModels(FileProcessor.getCsvFiles(), FileProcessor.getExtendedCsvFiles());

        CsvMetricProcessor.executeOptions(averageVotes,averageMetric, averageCharacterNumber, generateIndex);

        if(averageVotes) ConsoleProcessor.printAverageVoteNumber(CsvMetricProcessor.getAverageVotes());

        if(averageMetric) ConsoleProcessor.PrintAverageSeriesRating(CsvMetricProcessor.getAverageRating());

        if(averageCharacterNumber) ConsoleProcessor.printAverageCharacterNumber(CsvMetricProcessor.getAverageCharacterNumber());

    }
}