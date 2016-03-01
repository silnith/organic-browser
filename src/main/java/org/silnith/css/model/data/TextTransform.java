package org.silnith.css.model.data;


public enum TextTransform implements Keyword {
    CAPITALIZE("capitalize"),
    UPPERCASE("uppercase"),
    LOWERCASE("lowercase"),
    NONE("none");
    
    private final String value;

    private TextTransform(String value) {
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
