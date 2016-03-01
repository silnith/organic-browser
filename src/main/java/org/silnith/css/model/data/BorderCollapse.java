package org.silnith.css.model.data;


public enum BorderCollapse implements Keyword {
    COLLAPSE("collapse"),
    SEPARATE("separate");
    
    private final String value;
    
    private BorderCollapse(final String value) {
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
