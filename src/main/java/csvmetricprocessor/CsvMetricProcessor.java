package csvmetricprocessor;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import customanalyzers.SynomAnalyzer;
import indexprocessor.Indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class CsvMetricProcessor {

    private static SynomAnalyzer characterAnalyzer;
    private static SynomAnalyzer locationAnalyzer;

    private static float averageVotes = -1;
    private static float averageRating = -1;
    private static float averageCharacterNumber = -1;

    private static String INDEX_FOLDER_PATH = null;

    private static final HashMap<Integer, EpisodeDataModel> episodesData = new HashMap<>();

    public static float getAverageVotes() {
        return averageVotes;
    }

    public static float getAverageRating() {
        return averageRating;
    }

    public static float getAverageCharacterNumber() {
        return averageCharacterNumber;
    }

    public static void executeOptions(boolean averageVotes, boolean averageRating, boolean averageCharacterNumber, boolean generateIndex, boolean addtoIndex) throws IOException, ParseException {
        float sumVotes = 0;
        float sumRating = 0;
        float sumCharacterNumber = 0;
        int episodeCount = episodesData.size();

        if(generateIndex && addtoIndex) throw new RuntimeException("Cannot generate and add to index at the same time");

        for (EpisodeDataModel episode : episodesData.values()) {
            if (averageVotes) sumVotes += episode.getImdb_votes();
            if (averageRating) sumRating += episode.getImdb_rating();
            if (averageCharacterNumber) sumCharacterNumber += episode.getCharacter_list().size();
        }

        if (averageVotes) CsvMetricProcessor.averageVotes = sumVotes / episodeCount;
        if (averageRating) CsvMetricProcessor.averageRating = sumRating / episodeCount;
        if (averageCharacterNumber) CsvMetricProcessor.averageCharacterNumber = sumCharacterNumber / episodeCount;
        if(generateIndex) Indexer.index(episodesData, null);
        if(addtoIndex) Indexer.index(episodesData, INDEX_FOLDER_PATH);
    }

    public static void loadEpisodeDataModels(Set<File> csvFiles, Map<String,File> extendedCsvFiles) {

        EpisodeDataModel.setExtendedDataLoaded(extendedCsvFiles != null && !extendedCsvFiles.isEmpty());
        for (File file : csvFiles) {
            String capNumber = file.getName().split("_")[0];

            if(extendedCsvFiles != null)
                readAndParseCSV(file,extendedCsvFiles.get(capNumber));

        }
    }

    private static void readAndParseCSV(File file, File extendedFile) {
        try (CSVReader csvReader = createCSVReader(file)) {
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                ArrayList<EpisoDialog> episoDialogs = new ArrayList<>();

                if(extendedFile != null){
                    try (CSVReader extendedCsvReader = createCSVReader(extendedFile)) {
                        String[] extendedNextRecord;
                        while ((extendedNextRecord = extendedCsvReader.readNext()) != null) {
                            EpisoDialog episoDialog = new EpisoDialog(extendedNextRecord);
                            episoDialogs.add(episoDialog);
                        }
                    }
                }

                EpisodeDataModel episodeData = new EpisodeDataModel(nextRecord, episoDialogs);

                episodesData.put(episodeData.getEpisode_id(), episodeData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException | ParseException e) {
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

    public static void setIndexFolderPath(String path) {
        INDEX_FOLDER_PATH = path;
    }
}
