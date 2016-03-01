package org.silnith.css.model.data;


public enum Clear implements Keyword {
    NONE("none"),
    LEFT("left"),
    RIGHT("right"),
    BOTH("both");
    
    private final String value;
    
    private Clear(final String value) {
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
