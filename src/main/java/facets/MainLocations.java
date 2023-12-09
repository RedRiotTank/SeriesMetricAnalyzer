package facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum MainLocations {
    SPRINGFIELD_ELEMENTARY_SCHOOL,
    EVERGREEN_TERRACE,
    MOES_TAVERN,
    KWIK_E_MART,
    SPRINGFIELD_NUCLEAR_POWER_PLANT,
    SPRINGFIELD_TOWN_HALL,
    ANDROIDS_DUNGEON_BASEBALL_CARD_SHOP,
    SPRINGFIELD_POLICE_STATION,
    SPRINGFIELD_RETIREMENT_CASTLE,
    KRUSTY_BURGER,
    SPRINGFIELD_GENERAL_HOSPITAL,
    SPRINGFIELD_MALL,
    SPRINGFIELD_PRISION,
    CLETUSS_FARM,
    SQUISHEE_STORE,
    DUFF_BREWERY,
    ITCHY_SCRATCHY_LAND,
    KRUSTYLAND,
    MONORAIL_STATION,
    BURNS_MANOR;

    private static Set<String> locationNames;

    static {
        locationNames = new HashSet<>();

        for (MainLocations location : values()) {
            String[] splitedNames = location.name().toLowerCase().split("_");
            locationNames.addAll(Arrays.asList(splitedNames));
        }
    }


    public static boolean isMainLocation(String name) {
        String[] locations_names = name.split(" ");

        for (String n : locations_names) {
            if (locationNames.contains(n.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}

