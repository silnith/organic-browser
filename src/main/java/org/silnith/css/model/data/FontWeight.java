package org.silnith.css.model.data;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum FontWeight implements Comparable<FontWeight> {
    WEIGHT_100("100"),
    WEIGHT_200("200"),
    WEIGHT_300("300"),
    /**
     * Normal.
     */
    WEIGHT_400("400"),
    WEIGHT_500("500"),
    WEIGHT_600("600"),
    /**
     * Bold.
     */
    WEIGHT_700("700"),
    WEIGHT_800("800"),
    WEIGHT_900("900");
    
    private static final Map<String, FontWeight> LOOKUP;
    private static final Map<FontWeight, FontWeight> LIGHTER;
    private static final Map<FontWeight, FontWeight> BOLDER;
    
    static {
        LOOKUP = new HashMap<>();
        for (final FontWeight fontWeight : values()) {
            LOOKUP.put(fontWeight.getName(), fontWeight);
        }
        LIGHTER = new EnumMap<>(FontWeight.class);
        LIGHTER.put(WEIGHT_100, WEIGHT_100);
        LIGHTER.put(WEIGHT_200, WEIGHT_100);
        LIGHTER.put(WEIGHT_300, WEIGHT_100);
        LIGHTER.put(WEIGHT_400, WEIGHT_100);
        LIGHTER.put(WEIGHT_500, WEIGHT_100);
        LIGHTER.put(WEIGHT_600, WEIGHT_400);
        LIGHTER.put(WEIGHT_700, WEIGHT_400);
        LIGHTER.put(WEIGHT_800, WEIGHT_700);
        LIGHTER.put(WEIGHT_900, WEIGHT_700);
        BOLDER = new EnumMap<>(FontWeight.class);
        BOLDER.put(WEIGHT_100, WEIGHT_400);
        BOLDER.put(WEIGHT_200, WEIGHT_400);
        BOLDER.put(WEIGHT_300, WEIGHT_400);
        BOLDER.put(WEIGHT_400, WEIGHT_700);
        BOLDER.put(WEIGHT_500, WEIGHT_700);
        BOLDER.put(WEIGHT_600, WEIGHT_900);
        BOLDER.put(WEIGHT_700, WEIGHT_900);
        BOLDER.put(WEIGHT_800, WEIGHT_900);
        BOLDER.put(WEIGHT_900, WEIGHT_900);
    }
    
    public static FontWeight getFontWeight(final String name) {
        if ("normal".equals(name)) {
            return WEIGHT_400;
        } else if ("bold".equals(name)) {
            return WEIGHT_700;
        } else {
            return LOOKUP.get(name);
        }
    }
    
    private final String name;
    
    private FontWeight(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public FontWeight getLighter() {
        return LIGHTER.get(this);
    }

    public FontWeight getBolder() {
        return BOLDER.get(this);
    }
    
}
