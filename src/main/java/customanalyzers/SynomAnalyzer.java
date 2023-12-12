package customanalyzers;

import customfilters.SynonymFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import java.util.HashMap;

public class SynomAnalyzer extends Analyzer {
    private final HashMap<String, String> namesMap;

    public SynomAnalyzer(HashMap<String, String> namesMap) {
        this.namesMap = namesMap;
    }
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(source);
        result = new SynonymFilter(result, namesMap);
        return new TokenStreamComponents(source, result);
    }

    public boolean contains(String key){
        return namesMap.containsKey(key);
    }

    public String getValue(String key){
        return namesMap.get(key);
    }
}
