package facets;

import java.util.HashSet;
import java.util.Set;

public enum Child {
    MILHOUSE,
    NELSON,
    JIMBO,
    RALPH;

    //-------- Child names set --------//
    private static final Set<String> childNames;

    static {
        childNames = new HashSet<>();
        for (Child member : values())
            childNames.add(member.name().toLowerCase());

    }

    //-------- Public methods --------//

    public static boolean isChild(String name) {
        String[] names = name.split(" ");

        for (String n : names)
            if (childNames.contains(n.toLowerCase()))
                return true;

        return false;
    }
}
