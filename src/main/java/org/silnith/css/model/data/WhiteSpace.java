package org.silnith.css.model.data;


public enum WhiteSpace implements Keyword {
    NORMAL("normal"),
    PRE("pre"),
    NOWRAP("nowrap"),
    PRE_WRAP("pre-wrap"),
    PRE_LINE("pre-line");
    
    private final String value;
    
    private WhiteSpace(final String value) {
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
