package org.silnith.css.model.data;


public enum CaptionSide implements Keyword {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String value;
    
    private CaptionSide(final String value) {
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
