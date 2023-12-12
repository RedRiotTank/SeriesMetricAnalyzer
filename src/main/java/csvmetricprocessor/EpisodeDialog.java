package csvmetricprocessor;

public class EpisodeDialog {
    private final int number;
    private final String
            character,
            fullCharacterName,
            location,
            fullLocation,
            text;

    EpisodeDialog(String[] data){
        number = Integer.parseInt(data[2]);
        character = data[4].toLowerCase();

        if(CsvMetricProcessor.getCharacterAnalyzer() != null && CsvMetricProcessor.getCharacterAnalyzer().contains(character))
            fullCharacterName = CsvMetricProcessor.getCharacterAnalyzer().getValue(character);
         else
            fullCharacterName = character;

        location = data[5].toLowerCase();
        if(CsvMetricProcessor.getLocationAnalyzer() != null && CsvMetricProcessor.getLocationAnalyzer().contains(location))
            fullLocation = CsvMetricProcessor.getLocationAnalyzer().getValue(location);
         else
            fullLocation = location;

        text = data[6];
    }


    //---------Getters---------//

    public int getNumber() {
        return number;
    }

    public String getCharacter() {
        return character;
    }

    public String getLocation() {
        return location;
    }

    public String getFullLocation(){
        return fullLocation;
    }

    public String getText() {
        return text;
    }

    public String getFullCharacterName() {
        return fullCharacterName;
    }
}
