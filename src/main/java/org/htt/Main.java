package org.htt;

import console.ConsoleProcessor;
import csvmetricprocessor.CsvMetricProcessor;
import csvmetricprocessor.EpisodeDataModel;
import customanalyzers.SynomAnalyzer;
import files.FileProcessor;
import indexprocessor.Indexer;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;

import static console.ConsoleProcessor.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length == 0) {
            parameterError();
            printHelpMessage();
            return;
        }

        String lastArg = args[args.length - 1];
        String extendedFolderPath = null;

        boolean
                averageVotes = false,
                averageMetric = false,
                averageCharacterNumber = false,
                generateIndex = false,
                addToIndex = false;


        for(int i=0; i< args.length-1; i++){
            String arg = args[i];

            switch (arg) {
                case "-ef" -> {
                    extendedFolderPath = args[i + 1];
                    i++;
                }
                case "-chs" -> {
                    CsvMetricProcessor.setCharacterAnalyzer(new SynomAnalyzer(FileProcessor.generateSynonymMap(args[i + 1])));
                    i++;
                }

                case "-lcs" -> {
                    CsvMetricProcessor.setLocationAnalyzer(new SynomAnalyzer(FileProcessor.generateSynonymMap(args[i + 1])));
                    i++;
                }

                case "-V" -> // average votes
                        averageVotes = true;
                case "-R" ->  // average metric
                        averageMetric = true;
                case "-C" -> // average character number
                        averageCharacterNumber = true;
                case "-I" -> generateIndex = true;
                case "-A" -> {
                    addToIndex = true;
                    CsvMetricProcessor.setIndexFolderPath(args[i + 1]);
                    i++;
                }
            }
        }

        //if(extendedFolderPath != null) loadExtendedResources();

        FileProcessor.folderInitializer(lastArg);

        if(extendedFolderPath != null) FileProcessor.extendedFolderInitializer(extendedFolderPath);

        CsvMetricProcessor.loadEpisodeDataModels(FileProcessor.getCsvFiles(), FileProcessor.getExtendedCsvFiles());

        executeOptions(averageVotes,averageMetric, averageCharacterNumber, generateIndex, addToIndex);

        if(averageVotes) ConsoleProcessor.printAverageVoteNumber(CsvMetricProcessor.getAverageVotes());

        if(averageMetric) ConsoleProcessor.PrintAverageSeriesRating(CsvMetricProcessor.getAverageRating());

        if(averageCharacterNumber) ConsoleProcessor.printAverageCharacterNumber(CsvMetricProcessor.getAverageCharacterNumber());

    }

    public static void executeOptions(boolean averageVotes, boolean averageRating, boolean averageCharacterNumber, boolean generateIndex, boolean addToIndex) throws IOException, ParseException {
        float sumVotes = 0;
        float sumRating = 0;
        float sumCharacterNumber = 0;

        HashMap<Integer, EpisodeDataModel> episodesData = CsvMetricProcessor.getEpisodesData();
        int episodeCount = episodesData.size();

        if(generateIndex && addToIndex) throw new RuntimeException("Cannot generate and add to index at the same time");

        for (EpisodeDataModel episode : episodesData.values()) {
            if (averageVotes) sumVotes += episode.getImdb_votes();
            if (averageRating) sumRating += episode.getImdb_rating();
            if (averageCharacterNumber) sumCharacterNumber += episode.getCharacter_list().size();
        }

        if (averageVotes) CsvMetricProcessor.setAverageVotes(sumVotes / episodeCount);
        if (averageRating) CsvMetricProcessor.setAverageRating(sumRating / episodeCount);
        if (averageCharacterNumber) CsvMetricProcessor.setAverageCharacterNumber(sumCharacterNumber / episodeCount);
        if(generateIndex || addToIndex) Indexer.index(episodesData);
    }

    public static void loadExtendedResources() throws IOException {
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
            CsvMetricProcessor.setCharacterAnalyzer(new SynomAnalyzer(FileProcessor.generateSynonymMap(charactersPath)));

        if(locationsPath != null)
            CsvMetricProcessor.setLocationAnalyzer(new SynomAnalyzer(FileProcessor.generateSynonymMap(locationsPath)));
    }

}