package facets;

import java.util.HashSet;
import java.util.Set;

public enum SimpsonsFamily {
    HOMER,
    MARGE,
    BART,
    LISA,
    MAGGIE;

    private static final Set<String> familyNames;


    static {
        familyNames = new HashSet<>();
        for (SimpsonsFamily member : values()) {
            familyNames.add(member.name().toLowerCase());
        }
    }

    public static Set<String> getSimpsonFamilyNames() {
        return familyNames;
    }

    public static boolean isSimpsonFamilyMember(String name) {
        String[] names = name.split(" ");

        for (String n : names) {
            if (familyNames.contains(n.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
