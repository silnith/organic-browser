package org.silnith.css.model.data;


public enum Direction implements Keyword {
    LTR("ltr"),
    RTL("rtl");
    
    private final String value;
    
    private Direction(final String value) {
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
