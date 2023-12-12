package csvmetricprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EpisodeDataModel {
    private final int
            episode_id,
            number_in_season,
            original_air_year,
            season;

    private final float
            imdb_rating,
            imdb_votes,
            us_viewers_in_millions,
            views;
    private final String
            spoken_words,
            title;

    private final ArrayList<String> character_list;

    private final Date original_air_date;

    private final ArrayList<EpisodeDialog> episodeDialogData;

    EpisodeDataModel(String[] data, ArrayList<EpisodeDialog> episodeDialogData) throws ParseException {
        this.episode_id = Integer.parseInt(data[1]);
        this.spoken_words = data[2];
        this.character_list = new ArrayList<>(Arrays.asList(data[3].split(",")));
        this.imdb_rating = Float.parseFloat(data[4]);
        this.imdb_votes = Float.parseFloat(data[5]);
        this.number_in_season = Integer.parseInt(data[6]);
        this.original_air_date = (new SimpleDateFormat("yyyy-MM-dd")).parse(data[7]);
        this.original_air_year = Integer.parseInt(data[8]);
        this.season = Integer.parseInt(data[9]);
        this.title = data[10];
        this.us_viewers_in_millions = Float.parseFloat(data[11]);
        this.views = Float.parseFloat(data[12]);
        this.episodeDialogData = episodeDialogData;
    }

    //--------------------Getters--------------------//

    public int getEpisode_id() {
        return episode_id;
    }

    public String getSpoken_words() {
        return spoken_words;
    }

    public ArrayList<String> getCharacter_list() {
        return character_list;
    }

    public float getImdb_rating() {
        return imdb_rating;
    }

    public float getImdb_votes() {
        return imdb_votes;
    }

    public int getNumber_in_season() {
        return number_in_season;
    }

    public Date getOriginal_air_date() {
        return original_air_date;
    }

    public int getOriginal_air_year() {
        return original_air_year;
    }

    public int getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public float getUs_viewers_in_millions() {
        return us_viewers_in_millions;
    }

    public float getViews() {
        return views;
    }

    public ArrayList<EpisodeDialog> getEpisodeDialogData() {
        return episodeDialogData;
    }


    public String getCharactersListString(){
        StringBuilder result = new StringBuilder();
        for(String character : character_list){
            result.append(character).append(" ");
        }
        return result.substring(0, result.length() - 1);
    }
}
