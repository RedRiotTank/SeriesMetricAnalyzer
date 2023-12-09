package indexprocessor;

import csvmetricprocessor.EpisoDialog;
import csvmetricprocessor.EpisodeDataModel;
import customanalyzers.EnHunspellAnalyzer;
import facets.MainCharacters;
import facets.MainLocations;
import facets.SimpsonsFamily;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;


public class Indexer {
    public static void index(HashMap<Integer, EpisodeDataModel> episodesData, String indexpath) throws IOException, ParseException {
        Directory directory;

        if(indexpath != null)
            directory = FSDirectory.open(Paths.get(indexpath));
        else
            directory = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/index"));


        FSDirectory facetsDir = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/facets"));

        // FACETAS
        FacetsConfig facetsConfig = new FacetsConfig();

        facetsConfig.setHierarchical("Character", true);
        facetsConfig.setHierarchical("Location", true);
        facetsConfig.setIndexFieldName("ImdbRating", "imdb_rating");

        Analyzer enHunspellAnalyzer = new EnHunspellAnalyzer();

        System.out.println("Indexing...");

        HashMap<String, Analyzer> fieldAnalyzers = new HashMap<>();
        fieldAnalyzers.put("spoken_words", enHunspellAnalyzer);
        fieldAnalyzers.put("spoken_words_dialog", enHunspellAnalyzer);
        fieldAnalyzers.put("title", enHunspellAnalyzer);
        fieldAnalyzers.put("character", new StandardAnalyzer());
        fieldAnalyzers.put("location", new StandardAnalyzer());
        fieldAnalyzers.put("characters_list", new WhitespaceAnalyzer());

        PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(),fieldAnalyzers);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(perFieldAnalyzer);

        try{
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            DirectoryTaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter(facetsDir); // para las facetas

            for(EpisodeDataModel episode : episodesData.values()){
                Document episodeDoc = new Document();
                episodeDoc.add(new TextField("doc_type", "episode", TextField.Store.YES));

                episodeDoc.add(new IntPoint("episode_number",episode.getEpisode_id()));
                episodeDoc.add(new StoredField("episode_number_stored", episode.getEpisode_id()));

                episodeDoc.add(new TextField("spoken_words", episode.getSpoken_words(), TextField.Store.YES));

                episodeDoc.add(new TextField("characters_list", episode.getCharactersListString(), TextField.Store.YES));

                episodeDoc.add(new FloatPoint("imdb_rating",episode.getImdb_rating()));
                episodeDoc.add(new StoredField("imdb_rating_stored", episode.getImdb_rating()));
                // faceta imdb
                float imdbRating = episode.getImdb_rating();
                episodeDoc.add(new SortedSetDocValuesFacetField("ImdbRating",Float.toString(imdbRating)));

                episodeDoc.add(new LongPoint("release_date",episode.getOriginal_air_date().getTime()));
                episodeDoc.add(new StoredField("release_date_stored", episode.getOriginal_air_date().getTime()));

                episodeDoc.add(new IntPoint("season",episode.getSeason()));
                episodeDoc.add(new StoredField("season_stored", episode.getSeason()));

                episodeDoc.add(new TextField("title", episode.getTitle(), TextField.Store.YES));
                episodeDoc.add(new StringField("episode_views", String.valueOf(episode.getViews()), StringField.Store.YES));


                if(!episode.getEpisodeDialogData().isEmpty())
                    for (EpisoDialog dialog : episode.getEpisodeDialogData()){
                        Document dialogDoc = new Document();

                        dialogDoc.add(new TextField("doc_type", "dialog", TextField.Store.YES));

                        dialogDoc.add(new IntPoint("episode_number",episode.getEpisode_id()));
                        dialogDoc.add(new StoredField("episode_number_stored", episode.getEpisode_id()));

                        dialogDoc.add(new TextField("spoken_words_dialog", dialog.getText(), TextField.Store.YES));

                        dialogDoc.add(new TextField("character", dialog.getCharacter(), TextField.Store.YES));
                        dialogDoc.add(new TextField("character", dialog.getFullCharacterName(), TextField.Store.NO));

                        // TODO: añadir mas facetas

                        if(SimpsonsFamily.isSimpsonFamilyMember(dialog.getCharacter()))
                            dialogDoc.add(new FacetField("Character","SimpsonsFamily", dialog.getCharacter()));

                        else if(MainCharacters.isMainCharacter(dialog.getCharacter()))
                            dialogDoc.add(new FacetField("Character", "MainCharacter", dialog.getCharacter()));

                        dialogDoc.add(new TextField("location", dialog.getLocation(), TextField.Store.YES));
                        dialogDoc.add(new TextField("location", dialog.getFullLocation(), TextField.Store.NO));

                        if(MainLocations.isMainLocation(dialog.getLocation()))
                            dialogDoc.add(new FacetField("Location", "MainLocations", dialog.getLocation()));

                        indexWriter.addDocument(facetsConfig.build(taxonomyWriter,dialogDoc));
                    }

                indexWriter.addDocument(facetsConfig.build(taxonomyWriter,episodeDoc));
            }

            indexWriter.commit();
            indexWriter.close();

            taxonomyWriter.commit();
            taxonomyWriter.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
