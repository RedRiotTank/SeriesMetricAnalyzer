package org.htt;

import console.ConsoleProcessor;
import csvmetricprocessor.CsvMetricProcessor;
import files.FileProcessor;

import java.io.FileNotFoundException;

import static console.ConsoleProcessor.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            parameterError();
            printHelpMessage();
            return;
        }

        String lastArg = args[args.length - 1];
        FileProcessor.folderInitializer(lastArg);
        CsvMetricProcessor.loadEpisodeDataModels(FileProcessor.getCsvFiles());

        boolean
                averageVotes = false,
                averageMetric = false,
                averageCharacterNumber = false;

        for(int i=0; i< args.length-1; i++){
            String arg = args[i];

            switch (arg) {
                case "-V" -> // average votes
                        averageVotes = true;
                case "-R" ->  // average metric
                        averageMetric = true;
                case "-C" -> // average character number
                        averageCharacterNumber = true;
            }
            CsvMetricProcessor.executeOptions(averageVotes,averageMetric, averageCharacterNumber);
        }

        if(averageVotes) ConsoleProcessor.printAverageVoteNumber(CsvMetricProcessor.getAverageVotes());

        if(averageMetric) ConsoleProcessor.PrintAverageSeriesRating(CsvMetricProcessor.getAverageRating());

        if(averageCharacterNumber) ConsoleProcessor.printAverageCharacterNumber(CsvMetricProcessor.getAverageCharacterNumber());

    }
}