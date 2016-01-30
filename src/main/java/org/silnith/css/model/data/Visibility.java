package org.silnith.css.model.data;

public enum Visibility {
    VISIBLE("visible"),
    HIDDEN("hidden"),
    COLLAPSE("collapse");
    
    public static Visibility getFromValue(final String value) {
        return null;
    }
    
    private final String value;
    
    private Visibility(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
