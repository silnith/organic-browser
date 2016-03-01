package org.silnith.css.model.data;

import java.util.HashMap;
import java.util.Map;


/**
 * An enumeration of the legal values for the "display" property in CSS.
 * 
 * @author kent
 * @see PropertyName#DISPLAY
 * @see org.silnith.browser.organic.property.accessor.DisplayAccessor
 */
public enum Display implements Keyword {
    INLINE("inline"),
    BLOCK("block"),
    LIST_ITEM("list-item"),
    INLINE_BLOCK("inline-block"),
    TABLE("table"),
    INLINE_TABLE("inline-table"),
    TABLE_ROW_GROUP("table-row-group"),
    TABLE_HEADER_GROUP("table-header-group"),
    TABLE_FOOTER_GROUP("table-footer-group"),
    TABLE_ROW("table-row"),
    TABLE_COLUMN_GROUP("table-column-group"),
    TABLE_COLUMN("table-column"),
    TABLE_CELL("table-cell"),
    TABLE_CAPTION("table-caption"),
    NONE("none");
    
    private static final Map<String, Display> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        for (final Display display : values()) {
            lookupMap.put(display.getValue(), display);
        }
    }
    
    public static Display getFromValue(final String value) {
        return lookupMap.get(value);
    }
    
    private final String value;
    
    private Display(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public boolean is(String identifier) {
        return value.equals(identifier);
    }
    
}
