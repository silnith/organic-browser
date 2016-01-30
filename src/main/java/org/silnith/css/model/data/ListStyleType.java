package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


public enum ListStyleType {
    DISC("disc"),
    CIRCLE("circle"),
    SQUARE("square"),
    DECIMAL("decimal"),
    DECIMAL_LEADING_ZERO("decimal-leading-zero"),
    LOWER_ROMAN("lower-roman"),
    UPPER_ROMAN("upper-roman"),
    LOWER_GREEK("lower-greek"),
    LOWER_LATIN("lower-latin"),
    UPPER_LATIN("upper-latin"),
    ARMENIAN("armenian"),
    GEORGIAN("georgian"),
    LOWER_ALPHA("lower-alpha"),
    UPPER_ALPHA("upper-alpha"),
    NONE("none");
    
    private static final Map<String, ListStyleType> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        for (final ListStyleType listStyleType : values()) {
            lookupMap.put(listStyleType.getKey(), listStyleType);
        }
    }
    
    public static ListStyleType getFromValue(final String value) {
        return lookupMap.get(value);
    }
    
    private final String key;
    
    private ListStyleType(final String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
}
