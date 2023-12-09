package facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum MainCharacters {
    HOMER,
    MARGE,
    BART,
    LISA,
    MAGGIE,
    ABRAHAM_SIMPSON,
    NED_FLANDERS,
    MAUDE_FLANDERS,
    KRUSTY_THE_CLOWN,
    CHIEF_WIGGUM,
    MOE_SZYSLAK,
    APU_NAHASAPEEMAPETILON,
    SEYMOUR_SKINNER,
    EDNA_KRABAPPEL,
    MONTGOMERY_BURNS,
    WAYLON_SMITHERS,
    BARNEY_GUMBLE,
    MILHOUSE_VAN_HOUTEN,
    NELSON_MUNTZ,
    RALPH_WIGGUM;

    private static final Set<String> mainCharacterNames;

    static {
        mainCharacterNames = new HashSet<>();

        for (MainCharacters character : values()) {

            String[] splitedNames = character.name().toLowerCase().split("_");
            mainCharacterNames.addAll(Arrays.asList(splitedNames));

        }
    }

    public static boolean isMainCharacter(String name) {
        String[] names = name.split(" ");

        for (String n : names) {
            if (mainCharacterNames.contains(n.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

}

