package org.silnith.css.model.data;


public enum EmptyCells implements Keyword {
    SHOW("show"),
    HIDE("hide");
    
    private final String value;
    
    private EmptyCells(final String value) {
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
