package org.silnith.css.model.data;


public enum Overflow implements Keyword {
    VISIBLE("visible"),
    HIDDEN("hidden"),
    SCROLL("scroll"),
    AUTO("auto");
    
    private final String value;
    
    private Overflow(final String value) {
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
