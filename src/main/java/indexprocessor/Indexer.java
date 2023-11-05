package indexprocessor;

import csvmetricprocessor.CsvMetricProcessor;
import csvmetricprocessor.EpisoDialog;
import csvmetricprocessor.EpisodeDataModel;
import customanalyzers.EnHunspellAnalyzer;
import customanalyzers.SynomAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
import java.util.HashMap;

public class Indexer {

    public static void index(HashMap<Integer, EpisodeDataModel> episodesData) throws IOException, ParseException {

        String path = System.getProperty("user.dir") + "/index";
        Directory directory = FSDirectory.open(Paths.get(path));



        Analyzer englishAnalyzer = new EnglishAnalyzer();
        Analyzer enHunspellAnalyzer = new EnHunspellAnalyzer();

        System.out.println("Indexing...");

        HashMap<String, Analyzer> fieldAnalyzers = new HashMap<>();
        fieldAnalyzers.put("spoken_words", englishAnalyzer);
        fieldAnalyzers.put("character", new StandardAnalyzer());
        fieldAnalyzers.put("location", new StandardAnalyzer());

        PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(),fieldAnalyzers);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(perFieldAnalyzer);
        try{
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

            for(EpisodeDataModel episode : episodesData.values()){
                Document episodeDoc = new Document();
                if(!episode.getEpisodeDialogData().isEmpty()){
                    for (EpisoDialog dialog : episode.getEpisodeDialogData()) {
                        Document dialogDoc = new Document();
                        dialogDoc.add(new TextField("character", dialog.getCharacter(), TextField.Store.YES));
                        dialogDoc.add(new TextField("character", dialog.getFullCharacterName(), TextField.Store.NO));

                        dialogDoc.add(new TextField("location", dialog.getLocation(), TextField.Store.YES));
                        dialogDoc.add(new TextField("location", dialog.getFullLocation(), TextField.Store.NO));

                        dialogDoc.add(new TextField("spoken_words", dialog.getText(), TextField.Store.YES));

                        dialogDoc.add(new StringField("imdb_rating", String.valueOf(episode.getImdb_rating()), StringField.Store.YES));

                        //dialogDoc.add(new StringField("imdb_votes", String.valueOf(episode.getOriginal_air_date()), StringField.Store.YES));

                        dialogDoc.add(new StringField("title", episode.getTitle(), StringField.Store.YES));
                        indexWriter.addDocument(dialogDoc);
                    }

                } else {
                    episodeDoc.add(new TextField("character", "-1", TextField.Store.YES));

                    episodeDoc.add(new TextField("spoken_words", "-1", TextField.Store.YES));
                    episodeDoc.add(new TextField("title", episode.getTitle(), TextField.Store.YES));
                    episodeDoc.add(new StringField("imdb_rating", String.valueOf(episode.getImdb_rating()), StringField.Store.YES));
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
