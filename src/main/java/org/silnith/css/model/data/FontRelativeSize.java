package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


public enum FontRelativeSize {
    LARGER("larger"),
    SMALLER("smaller");
    
    private static final Map<String, FontRelativeSize> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        for (final FontRelativeSize fontRelativeSize : FontRelativeSize.values()) {
            lookupMap.put(fontRelativeSize.getKey(), fontRelativeSize);
        }
    }
    
    public static FontRelativeSize getFontRelativeSize(final String key) {
        return lookupMap.get(key);
    }
    
    private final String key;
    
    private FontRelativeSize(final String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
}
