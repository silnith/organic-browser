package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


/**
 * An enumeration of the legal values for the "border-*-style" properties.
 * 
 * @author kent
 * @see PropertyName#BORDER_TOP_STYLE
 * @see PropertyName#BORDER_RIGHT_STYLE
 * @see PropertyName#BORDER_BOTTOM_STYLE
 * @see PropertyName#BORDER_LEFT_STYLE
 * @see org.silnith.browser.organic.property.accessor.BorderTopStyleAccessor
 * @see org.silnith.browser.organic.property.accessor.BorderRightStyleAccessor
 * @see org.silnith.browser.organic.property.accessor.BorderBottomStyleAccessor
 * @see org.silnith.browser.organic.property.accessor.BorderLeftStyleAccessor
 */
public enum BorderStyle {
    NONE("none"),
    HIDDEN("hidden"),
    DOTTED("dotted"),
    DASHED("dashed"),
    SOLID("solid"),
    DOUBLE("double"),
    GROOVE("groove"),
    RIDGE("ridge"),
    INSET("inset"),
    OUTSET("outset");
    
    private static final Map<String, BorderStyle> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        for (final BorderStyle borderStyle : values()) {
            lookupMap.put(borderStyle.getValue(), borderStyle);
        }
    }
    
    public static BorderStyle getFromValue(final String specifiedValue) {
        return lookupMap.get(specifiedValue);
    }
    
    private final String value;
    
    private BorderStyle(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
