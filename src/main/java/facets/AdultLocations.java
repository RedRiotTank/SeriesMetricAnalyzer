package facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum AdultLocations {
    EVERGREEN_TERRACE,
    MOES_TAVERN,
    NUCLEAR_POWER_PLANT,
    TOWN_HALL,
    POLICE_STATION,
    RETIREMENT_CASTLE,
    GENERAL_HOSPITAL,
    MALL,
    PRISION,
    CLETUSS_FARM,
    SQUISHEE_STORE,
    DUFF_BREWERY,
    MONORAIL_STATION,
    BURNS_MANOR;

    //-------- Locations names set --------//

    private static final Set<String> locationNames;

    static {
        locationNames = new HashSet<>();

        for (AdultLocations location : values()) {
            String[] splitedNames = location.name().toLowerCase().split("_");
            locationNames.addAll(Arrays.asList(splitedNames));
        }
    }

    //-------- Public methods --------//

    public static boolean isAdultLocation(String name) {
        String[] locations_names = name.split(" ");

        for (String n : locations_names)
            if (locationNames.contains(n.toLowerCase()))
                return true;

        return false;
    }
}

