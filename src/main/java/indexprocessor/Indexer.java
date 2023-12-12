package indexprocessor;

import csvmetricprocessor.CsvMetricProcessor;
import csvmetricprocessor.EpisodeDialog;
import csvmetricprocessor.EpisodeDataModel;
import customanalyzers.EnHunspellAnalyzer;
import facets.*;
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
    public static void index(HashMap<Integer, EpisodeDataModel> episodesData) throws IOException, ParseException {
        Directory directory;

        if(CsvMetricProcessor.getIndexFolderPath() != null)
            directory = FSDirectory.open(Paths.get(CsvMetricProcessor.getIndexFolderPath()));
        else
            directory = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/index"));

        FSDirectory facetsDir = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/facets"));

        FacetsConfig facetsConfig = getFacetsConfig();

        System.out.println("Indexing...");

        try{
            IndexWriter indexWriter = new IndexWriter(directory, getWriterConfig());
            DirectoryTaxonomyWriter taxonomyWriter = new DirectoryTaxonomyWriter(facetsDir);

            for(EpisodeDataModel episode : episodesData.values()){

                if(!episode.getEpisodeDialogData().isEmpty())
                    for (EpisodeDialog dialog : episode.getEpisodeDialogData())
                        indexWriter.addDocument(facetsConfig.build(taxonomyWriter,generateDialogDoc(dialog, episode)));

                indexWriter.addDocument(facetsConfig.build(taxonomyWriter,generateEpisodeDoc(episode)));
            }

            indexWriter.commit();
            indexWriter.close();

            taxonomyWriter.commit();
            taxonomyWriter.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //---------- Internal Methods ----------//

    private static FacetsConfig getFacetsConfig(){
        FacetsConfig facetsConfig = new FacetsConfig();

        facetsConfig.setHierarchical("Character", true);
        facetsConfig.setHierarchical("Location", true);
        facetsConfig.setIndexFieldName("ImdbRating", "imdb_rating");

        return facetsConfig;
    }

    private static IndexWriterConfig getWriterConfig() throws IOException, ParseException {

        Analyzer enHunspellAnalyzer = new EnHunspellAnalyzer();

        HashMap<String, Analyzer> fieldAnalyzers = new HashMap<>();
        fieldAnalyzers.put("spoken_words", enHunspellAnalyzer);
        fieldAnalyzers.put("spoken_words_dialog", enHunspellAnalyzer);
        fieldAnalyzers.put("title", enHunspellAnalyzer);
        fieldAnalyzers.put("character", new StandardAnalyzer());
        fieldAnalyzers.put("location", new StandardAnalyzer());
        fieldAnalyzers.put("characters_list", new WhitespaceAnalyzer());

        return new IndexWriterConfig(new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(),fieldAnalyzers));
    }

    private static Document generateDialogDoc(EpisodeDialog dialog, EpisodeDataModel episode){
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

        else if(Child.isChild(dialog.getCharacter()))
            dialogDoc.add(new FacetField("Character", "Child", dialog.getCharacter()));

        else if(Adults.isMainCharacter(dialog.getCharacter()))
            dialogDoc.add(new FacetField("Character", "Adult", dialog.getCharacter()));


        dialogDoc.add(new TextField("location", dialog.getLocation(), TextField.Store.YES));
        dialogDoc.add(new TextField("location", dialog.getFullLocation(), TextField.Store.NO));

        if(ChildLocations.isChildLocation(dialog.getLocation()))
            dialogDoc.add(new FacetField("Location", "ChildLocations", dialog.getLocation()));

        else if(AdultLocations.isAdultLocation(dialog.getLocation()))
            dialogDoc.add(new FacetField("Location", "AdultLocations", dialog.getLocation()));


        return dialogDoc;
    }

    private static Document generateEpisodeDoc(EpisodeDataModel episode){
        Document episodeDoc = new Document();
        episodeDoc.add(new TextField("doc_type", "episode", TextField.Store.YES));

        episodeDoc.add(new IntPoint("episode_number",episode.getEpisode_id()));
        episodeDoc.add(new StoredField("episode_number_stored", episode.getEpisode_id()));

        episodeDoc.add(new TextField("spoken_words", episode.getSpoken_words(), TextField.Store.YES));

        episodeDoc.add(new TextField("characters_list", episode.getCharactersListString(), TextField.Store.YES));

        episodeDoc.add(new FloatPoint("imdb_rating",episode.getImdb_rating()));
        episodeDoc.add(new StoredField("imdb_rating_stored", episode.getImdb_rating()));
        episodeDoc.add(new SortedSetDocValuesFacetField("ImdbRating",Float.toString(episode.getImdb_rating())));

        episodeDoc.add(new LongPoint("release_date",episode.getOriginal_air_date().getTime()));
        episodeDoc.add(new StoredField("release_date_stored", episode.getOriginal_air_date().getTime()));

        episodeDoc.add(new IntPoint("season",episode.getSeason()));
        episodeDoc.add(new StoredField("season_stored", episode.getSeason()));

        episodeDoc.add(new TextField("title", episode.getTitle(), TextField.Store.YES));
        episodeDoc.add(new StringField("episode_views", String.valueOf(episode.getViews()), StringField.Store.YES));

        return episodeDoc;
    }

}
