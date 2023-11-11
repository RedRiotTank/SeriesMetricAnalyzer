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
        StringBuilder line = new StringBuilder();

        while(input.incrementToken()){
           line.append(charTermAttribute.toString()).append(" ");
           charTermAttribute.setEmpty();
        }

        if(line.toString().endsWith(" "))
            line = new StringBuilder(line.substring(0, line.length() - 1));


        if (namesMap.containsKey(line.toString())) {
            charTermAttribute.append(namesMap.get(line.toString()));
        } else {
            charTermAttribute.append(line.toString());
        }

        return !line.toString().isEmpty();

    }

}
