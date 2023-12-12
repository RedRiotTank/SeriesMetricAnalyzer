package facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ChildLocations {
    ELEMENTARY_SCHOOL,
    KWIK_E_MART,
    ANDROIDS_DUNGEON_BASEBALL_CARD_SHOP,
    KRUSTY_BURGER,
    CLETUSS_FARM,
    SQUISHEE_STORE,
    ITCHY_SCRATCHY_LAND,
    KRUSTYLAND,
    MONORAIL_STATION;

    //-------- Locations names set --------//

    private static final Set<String> locationNames;

    static {
        locationNames = new HashSet<>();

        for (ChildLocations location : values()) {
            String[] splitedNames = location.name().toLowerCase().split("_");
            locationNames.addAll(Arrays.asList(splitedNames));
        }
    }

    //-------- Public methods --------//

    public static boolean isChildLocation(String name) {
        String[] locations_names = name.split(" ");

        for (String n : locations_names)
            if (locationNames.contains(n.toLowerCase()))
                return true;

        return false;
    }
}

