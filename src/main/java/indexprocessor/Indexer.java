package indexprocessor;

import csvmetricprocessor.EpisoDialog;
import csvmetricprocessor.EpisodeDataModel;
import customanalyzers.EnHunspellAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Indexer {

    public static void index(HashMap<Integer, EpisodeDataModel> episodesData) throws IOException, ParseException {

        String path = System.getProperty("user.dir") + "/index";
        Directory directory = FSDirectory.open(Paths.get(path));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");


        // Analyzer englishAnalyzer = new EnglishAnalyzer();
        Analyzer enHunspellAnalyzer = new EnHunspellAnalyzer();

        System.out.println("Indexing...");

        HashMap<String, Analyzer> fieldAnalyzers = new HashMap<>();
        fieldAnalyzers.put("spoken_words", enHunspellAnalyzer);
        fieldAnalyzers.put("title", enHunspellAnalyzer);
        fieldAnalyzers.put("character", new StandardAnalyzer());
        fieldAnalyzers.put("location", new StandardAnalyzer());
        fieldAnalyzers.put("characters_list", new WhitespaceAnalyzer());

        PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(),fieldAnalyzers);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(perFieldAnalyzer);
        try{
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

            for(EpisodeDataModel episode : episodesData.values()){
                Document episodeDoc = new Document();
                String number = String.valueOf(episode.getEpisode_id());
                String season = String.valueOf(episode.getSeason());

                if(number.length() == 1) number = "00" + number;
                if(number.length() == 2) number = "0" + number;
                if(season.length() == 1) season = "00" + season;
                if(season.length() == 2) season = "0" + season;

                if(!episode.getEpisodeDialogData().isEmpty()){
                    for (EpisoDialog dialog : episode.getEpisodeDialogData()) {
                        Document dialogDoc = new Document();

                        dialogDoc.add(new StringField("episode_number", number, StringField.Store.YES));
                        dialogDoc.add(new TextField("spoken_words", dialog.getText(), TextField.Store.YES));
                        dialogDoc.add(new TextField("character", dialog.getCharacter(), TextField.Store.YES));
                        dialogDoc.add(new TextField("character", dialog.getFullCharacterName(), TextField.Store.NO));
                        dialogDoc.add(new TextField("location", dialog.getLocation(), TextField.Store.YES));
                        dialogDoc.add(new TextField("location", dialog.getFullLocation(), TextField.Store.NO));
                        dialogDoc.add(new StringField("imdb_rating", String.valueOf(episode.getImdb_rating()*10), StringField.Store.YES));
                        dialogDoc.add(new StringField("imdb_votes", String.valueOf(episode.getImdb_votes()), StringField.Store.YES));
                        dialogDoc.add(new StringField("release_date", Long.toString(episode.getOriginal_air_date().getTime()), StringField.Store.YES));
                        dialogDoc.add(new StringField("season", season, StringField.Store.YES));
                        dialogDoc.add(new TextField("title", episode.getTitle(), StringField.Store.YES));
                        dialogDoc.add(new StringField("episode_views", String.valueOf(episode.getViews()), StringField.Store.YES));

                        indexWriter.addDocument(dialogDoc);
                    }

                } else {
                    episodeDoc.add(new StringField("episode_number", number, StringField.Store.YES));
                    episodeDoc.add(new TextField("spoken_words", episode.getSpoken_words(), TextField.Store.YES));
                    episodeDoc.add(new TextField("characters_list", episode.getCharactersListString(), TextField.Store.YES));
                    episodeDoc.add(new StringField("imdb_rating", String.valueOf(episode.getImdb_rating()*10), StringField.Store.YES));
                    episodeDoc.add(new StringField("imdb_votes", String.valueOf(episode.getImdb_votes()), StringField.Store.YES));
                    episodeDoc.add(new StringField("release_date", Long.toString(episode.getOriginal_air_date().getTime()), StringField.Store.YES));
                    episodeDoc.add(new StringField("season", season, StringField.Store.YES));
                    episodeDoc.add(new TextField("title", episode.getTitle(), TextField.Store.YES));
                    episodeDoc.add(new StringField("episode_views", String.valueOf(episode.getViews()), StringField.Store.YES));

                    indexWriter.addDocument(episodeDoc);
                }


            }

            indexWriter.commit();
            indexWriter.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
