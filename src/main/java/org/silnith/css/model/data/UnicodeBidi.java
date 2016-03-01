package org.silnith.css.model.data;


public enum UnicodeBidi implements Keyword {
    NORMAL("normal"),
    EMBED("embed"),
    BIDI_OVERRIDE("bidi-override");
    
    private final String value;
    
    private UnicodeBidi(final String value) {
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
