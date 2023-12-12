package csvmetricprocessor;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import customanalyzers.SynomAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CsvMetricProcessor {

    private static SynomAnalyzer
            characterAnalyzer,
            locationAnalyzer;

    private static float
            averageVotes = -1,
            averageRating = -1,
            averageCharacterNumber = -1;

    private static String INDEX_FOLDER_PATH = null;

    private static final HashMap<Integer, EpisodeDataModel> episodesData = new HashMap<>();

    //---------------------------- Methods ----------------------------//

    public static void loadEpisodeDataModels(Set<File> csvFiles, Map<String, File> extendedCsvFiles) {
        for (File file : csvFiles) {
            String capNumber = file.getName().split("_")[0];
            File extendedFile = extendedCsvFiles != null ? extendedCsvFiles.get(capNumber) : null;

            readAndParseCSV(file, extendedFile);
        }
    }

    //---------------------------- Internal ----------------------------//

    private static void readAndParseCSV(File file, File extendedFile) {
        try (CSVReader csvReader = createCSVReader(file);
             CSVReader extendedCsvReader = (extendedFile != null) ? createCSVReader(extendedFile) : null) {

            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                ArrayList<EpisodeDialog> episodeDialogs = new ArrayList<>();

                if (extendedCsvReader != null) {
                    String[] extendedNextRecord;
                    while ((extendedNextRecord = extendedCsvReader.readNext()) != null) {
                        EpisodeDialog episodeDialog = new EpisodeDialog(extendedNextRecord);
                        episodeDialogs.add(episodeDialog);
                    }
                }

                EpisodeDataModel episodeData = new EpisodeDataModel(nextRecord, episodeDialogs);
                episodesData.put(episodeData.getEpisode_id(), episodeData);
            }

        } catch (IOException | CsvValidationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static CSVReader createCSVReader(File file) throws FileNotFoundException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"')
                .build();
        CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).withSkipLines(1);
        return csvReaderBuilder.build();
    }


    //---------------------------- Getters & Setters ----------------------------//

    public static void setCharacterAnalyzer(SynomAnalyzer characterAnalyzer) {
        CsvMetricProcessor.characterAnalyzer = characterAnalyzer;
    }

    public static SynomAnalyzer getCharacterAnalyzer() {
        return characterAnalyzer;
    }

    public static void setLocationAnalyzer(SynomAnalyzer locationAnalyzer) {
        CsvMetricProcessor.locationAnalyzer = locationAnalyzer;
    }

    public static SynomAnalyzer getLocationAnalyzer() {
        return locationAnalyzer;
    }

    public static float getAverageVotes() {
        return averageVotes;
    }

    public static void setAverageVotes(float averageVotes) {
        CsvMetricProcessor.averageVotes = averageVotes;
    }

    public static float getAverageRating() {
        return averageRating;
    }

    public static void setAverageRating(float averageRating) {
        CsvMetricProcessor.averageRating = averageRating;
    }

    public static float getAverageCharacterNumber() {
        return averageCharacterNumber;
    }

    public static void setAverageCharacterNumber(float averageCharacterNumber) {
        CsvMetricProcessor.averageCharacterNumber = averageCharacterNumber;
    }

    public static void setIndexFolderPath(String path) {
        INDEX_FOLDER_PATH = path;
    }

    public static String getIndexFolderPath() {
        return INDEX_FOLDER_PATH;
    }

    public static HashMap<Integer, EpisodeDataModel> getEpisodesData() {
        return episodesData;
    }

}
