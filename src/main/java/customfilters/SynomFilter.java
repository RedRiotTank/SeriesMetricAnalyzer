package customfilters;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.HashMap;

public class SynomFilter extends TokenFilter {

    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    private final HashMap<String, String> namesMap;

    public SynomFilter(TokenStream input, HashMap<String, String> namesMap) {
        super(input);
        this.namesMap = namesMap;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (input.incrementToken()) {
            String word = charTermAttribute.toString();
            if (namesMap.containsKey(word)) {
                charTermAttribute.setEmpty();
                charTermAttribute.append(namesMap.get(word));
            }
            return true;
        } else {
            return false;
        }
    }

}
