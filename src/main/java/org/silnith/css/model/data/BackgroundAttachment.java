package org.silnith.css.model.data;


public enum BackgroundAttachment implements Keyword {
    SCROLL("scroll"),
    FIXED("fixed");
    
    private final String value;
    
    private BackgroundAttachment(final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public boolean is(final String identifier) {
        return value.equals(identifier);
    }

}
