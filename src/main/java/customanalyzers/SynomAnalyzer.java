package customanalyzers;

import customfilters.SynomFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class SynomAnalyzer extends Analyzer {

    public HashMap<String, String> namesMap;

    public SynomAnalyzer(HashMap<String, String> namesMap) throws FileNotFoundException {
        this.namesMap = namesMap;

    }
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(source);
        result = new SynomFilter(result, namesMap);
        return new TokenStreamComponents(source, result);
    }
}
