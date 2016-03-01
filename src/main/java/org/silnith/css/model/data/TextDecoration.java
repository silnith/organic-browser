package org.silnith.css.model.data;


public enum TextDecoration implements Keyword {
    NONE("none"),
    UNDERLINE("underline"),
    OVERLINE("overline"),
    LINE_THROUGH("line-through"),
    BLINK("blink");
    
    private final String value;
    
    private TextDecoration(final String value) {
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
