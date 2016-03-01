package org.silnith.css.model.data;


public enum TextAlign implements Keyword {
    LEFT("left"),
    RIGHT("right"),
    CENTER("center"),
    JUSTIFY("justify"),
    NAMELESS("");
    
    private final String value;

    private TextAlign(final String value) {
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
