package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


/**
 * An enumeration of the legal values for the "position" property in CSS.
 * 
 * @author kent
 * @see PropertyName#POSITION
 * @see org.silnith.browser.organic.property.accessor.PositionAccessor
 */
public enum Position {
    STATIC("static"),
    RELATIVE("relative"),
    ABSOLUTE("absolute"),
    FIXED("fixed");
    
    private static final Map<String, Position> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        for (final Position position : values()) {
            lookupMap.put(position.getValue(), position);
        }
    }
    
    public static Position getFromValue(final String value) {
        return lookupMap.get(value);
    }
    
    private final String value;
    
    private Position(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
