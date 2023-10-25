package customanalyzers;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.analysis.hunspell.HunspellStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.store.FSDirectory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

public class TextEnAnalyzer extends Analyzer {

    public TextEnAnalyzer(){
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        Dictionary dictionary = null;

        try {
            InputStream affixStream = getClass().getResourceAsStream("/en_EN.aff");
            InputStream dictStream = getClass().getResourceAsStream("/en_EN.dic");
            FSDirectory directorioTemp;

            directorioTemp = FSDirectory.open(Paths.get("/temp"));
            if(affixStream != null)
                dictionary = new Dictionary(directorioTemp, "temporalFile", affixStream, dictStream);

        } catch (Exception ignored) {}


        TokenStream result;

        result = new HunspellStemFilter(source, dictionary, true, true);

        //result = PROCESADO DE SINONIMOS ()


        return new TokenStreamComponents(source, result);
    }

}
