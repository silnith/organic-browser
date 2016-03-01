package org.silnith.css.model.data;


public enum FontVariant implements Keyword {
    NORMAL("normal"),
    SMALL_CAPS("small-caps");
    
    private final String value;
    
    private FontVariant(final String value) {
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
