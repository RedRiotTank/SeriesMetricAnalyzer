package indexprocessor;

import java.util.HashSet;
import java.util.Set;

public class FacetsData {
    public static Set<String> getSimsponsFamilyCharacters() {
        Set<String> simsponsFamilyCharacters = new HashSet<>();
        simsponsFamilyCharacters.add("homer simpson");
        simsponsFamilyCharacters.add("homer");
        simsponsFamilyCharacters.add("marge simpson");
        simsponsFamilyCharacters.add("marge");
        simsponsFamilyCharacters.add("bart simpson");
        simsponsFamilyCharacters.add("lisa simpson");
        return simsponsFamilyCharacters;
    }

    public static Set<String> getMainCharacters() {
        Set<String> mainCharacters = new HashSet<>();
        mainCharacters.add("marge simpson");
        mainCharacters.add("bart simpson");
        mainCharacters.add("lisa simpson");
        mainCharacters.add("maggie simpson");
        mainCharacters.add("abraham simpson");
        mainCharacters.add("ned flanders");
        mainCharacters.add("maude flanders");
        mainCharacters.add("krusty the clown");
        mainCharacters.add("chief wiggum");
        mainCharacters.add("moe szyslak");
        mainCharacters.add("apu nahasapeemapetilon");
        mainCharacters.add("seymour skinner");
        mainCharacters.add("edna krabappel");
        mainCharacters.add("montgomery burns");
        mainCharacters.add("waylon smithers");
        mainCharacters.add("barney gumble");
        mainCharacters.add("milhouse van houten");
        mainCharacters.add("nelson muntz");
        mainCharacters.add("ralph wiggum");
        return mainCharacters;
    }

    public static Set<String> getMainLocations(){
        Set<String> locations = new HashSet<>();
        locations.add("springfield elementary school");
        locations.add("742 evergreen terrace");
        locations.add("moe's tavern");
        locations.add("kwik-e-mart");
        locations.add("springfield nuclear power plant");
        locations.add("springfield town hall");
        locations.add("the android's dungeon & baseball card shop");
        locations.add("springfield police station");
        locations.add("springfield retirement castle");
        locations.add("krusty burger");
        locations.add("springfield general hospital");
        locations.add("springfield mall");
        locations.add("cletus's farm");
        locations.add("squishee store");
        locations.add("duff brewery");
        locations.add("itchy & scratchy land");
        locations.add("krustyland");
        locations.add("monorail station");
        locations.add("burns manor");

        return locations;
    }
}

