package csvmetricprocessor;

import java.util.HashMap;

public class EpisoDialog {
    private int number;
    private int time_stamp;
    private String character;
    private String fullCharacterName;
    private String location;

    private String text;

    EpisoDialog(String[] data){
        number = Integer.parseInt(data[2]);
        time_stamp = Integer.parseInt(data[3]);
        character = data[4].toLowerCase();

        if(CsvMetricProcessor.getCharacterAnalyzer().namesMap.containsKey(character)){
            fullCharacterName = CsvMetricProcessor.getCharacterAnalyzer().namesMap.get(character);
        } else {
            fullCharacterName = character;
        }

        location = data[5];
        text = data[6];

    }

    public int getNumber() {
        return number;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public String getCharacter() {
        return character;
    }

    public String getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public String getFullCharacterName() {
        return fullCharacterName;
    }
}
