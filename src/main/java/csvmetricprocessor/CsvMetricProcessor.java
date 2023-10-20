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

    private static float
            averageVotes = -1,
            averageRating = -1,
            averageCharacterNumber = -1;

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
        float
                sumVotes = 0,
                sumRating = 0,
                sumCharacterNumber = 0;

        for(EpisodeDataModel episode : episodesData.values()){
            if(averageVotes) sumVotes += episode.getImdb_votes();
            if(averageRating) sumRating += episode.getImdb_rating();
            if(averageCharacterNumber) sumCharacterNumber += episode.getCharacter_list().size();
        }
        if(averageVotes) CsvMetricProcessor.averageVotes = sumVotes / episodesData.size();
        if(averageRating) CsvMetricProcessor.averageRating = sumRating / episodesData.size();
        if(averageCharacterNumber) CsvMetricProcessor.averageCharacterNumber = sumCharacterNumber / episodesData.size();

    }

    public static void loadEpisodeDataModels( Set<File> csvFiles) throws FileNotFoundException {

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"')
                .build();

        for (File file : csvFiles) {
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).withSkipLines(1);
            try (CSVReader csvReader = csvReaderBuilder.build()) {
                String[] nextRecord;
                while ((nextRecord = csvReader.readNext()) != null) {
                    EpisodeDataModel episodeData = new EpisodeDataModel(nextRecord);
                    episodesData.put(episodeData.getEpisode_id(),episodeData);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }

    }

    public static float getAverageVoteNumber(){
        return 1;
    }


    public static HashMap<Integer, EpisodeDataModel> getEpisodesData() {
        return episodesData;
    }

}
