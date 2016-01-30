package org.silnith.css.model.data;

public enum VerticalAlignment {
    BASELINE("baseline"),
    SUB("sub"),
    SUPER("super"),
    TOP("top"),
    TEXT_TOP("text-top"),
    MIDDLE("middle"),
    BOTTOM("bottom"),
    TEXT_BOTTOM("text-bottom");
    
    private final String value;
    
    private VerticalAlignment(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
