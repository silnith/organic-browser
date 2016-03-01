package org.silnith.css.model.data;


public enum BackgroundRepeat implements Keyword {
    REPEAT("repeat"),
    REPEAT_X("repeat-x"),
    REPEAT_Y("repeat-y"),
    NO_REPEAT("no-repeat");
    
    private final String value;
    
    private BackgroundRepeat(final String value) {
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
