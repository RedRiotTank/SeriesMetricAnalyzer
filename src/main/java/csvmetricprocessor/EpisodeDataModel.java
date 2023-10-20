package csvmetricprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EpisodeDataModel {

    private int episode_id;
    private String spoken_words;
    private ArrayList character_list;
    private float imdb_rating;
    private float imdb_votes;
    private int number_in_season;
    private Date original_air_date;
    private int original_air_year;
    private int seasson;
    private String title;
    private float us_viewers_in_millions;
    private float views;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    EpisodeDataModel(String[] data) throws ParseException {


        this.episode_id = Integer.parseInt(data[1]);
        this.spoken_words = data[2];
        this.character_list = new ArrayList(Arrays.asList(data[3].split(",")));
        this.imdb_rating = Float.parseFloat(data[4]);
        this.imdb_votes = Float.parseFloat(data[5]);
        this.number_in_season = Integer.parseInt(data[6]);
        this.original_air_date = dateFormat.parse(data[7]);
        this.original_air_year = Integer.parseInt(data[8]);
        this.seasson = Integer.parseInt(data[9]);
        this.title = data[10];
        this.us_viewers_in_millions = Float.parseFloat(data[11]);
        this.views = Float.parseFloat(data[12]);
    }

    public int getEpisode_id() {
        return episode_id;
    }

    public String getSpoken_words() {
        return spoken_words;
    }

    public ArrayList getCharacter_list() {
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

    public int getSeasson() {
        return seasson;
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

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
