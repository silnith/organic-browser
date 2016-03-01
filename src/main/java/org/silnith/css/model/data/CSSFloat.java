package org.silnith.css.model.data;


public enum CSSFloat implements Keyword {
    LEFT("left"),
    RIGHT("right"),
    NONE("none");
    
    private final String value;
    
    private CSSFloat(final String value) {
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
