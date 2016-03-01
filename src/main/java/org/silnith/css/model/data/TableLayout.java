package org.silnith.css.model.data;


public enum TableLayout implements Keyword {
    AUTO("auto"),
    FIXED("fixed");
    
    private final String value;
    
    private TableLayout(final String value) {
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
