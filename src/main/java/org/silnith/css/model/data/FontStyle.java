package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;

public enum FontStyle {
    NORMAL("normal"),
    ITALIC("italic"),
    OBLIQUE("oblique");
    
    private static final Map<String, FontStyle> LOOKUP_MAP;
    
    static {
        LOOKUP_MAP = new HashMap<>();
        for (final FontStyle fontStyle : values()) {
            if (LOOKUP_MAP.containsKey(fontStyle.getValue())) {
                throw new IllegalStateException("Two font styles share the same value: " + fontStyle.getValue());
            }
            LOOKUP_MAP.put(fontStyle.getValue(), fontStyle);
        }
    }
    
    public static FontStyle getFromValue(final String value) {
        return LOOKUP_MAP.get(value);
    }
    
    private final String value;
    
    private FontStyle(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
