package csvmetricprocessor;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Set;

public class CsvMetricProcessor {
    private static float averageVotes = -1;
    private static float averageRating = -1;
    private static float averageCharacterNumber = -1;

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

    public static void executeOptions(boolean averageVotes, boolean averageRating, boolean averageCharacterNumber) {
        float sumVotes = 0;
        float sumRating = 0;
        float sumCharacterNumber = 0;
        int episodeCount = episodesData.size();

        for (EpisodeDataModel episode : episodesData.values()) {
            if (averageVotes) sumVotes += episode.getImdb_votes();
            if (averageRating) sumRating += episode.getImdb_rating();
            if (averageCharacterNumber) sumCharacterNumber += episode.getCharacter_list().size();
        }

        if (averageVotes) CsvMetricProcessor.averageVotes = sumVotes / episodeCount;
        if (averageRating) CsvMetricProcessor.averageRating = sumRating / episodeCount;
        if (averageCharacterNumber) CsvMetricProcessor.averageCharacterNumber = sumCharacterNumber / episodeCount;
    }

    public static void loadEpisodeDataModels(Set<File> csvFiles) {
        for (File file : csvFiles) {
            readAndParseCSV(file);
        }
    }

    private static void readAndParseCSV(File file) {
        try (CSVReader csvReader = createCSVReader(file)) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                EpisodeDataModel episodeData = new EpisodeDataModel(nextRecord);
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
}
