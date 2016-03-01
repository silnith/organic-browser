package org.silnith.css.model.data;

/**
 * An enumeration of the legal values for the "visibility" property.
 * 
 * @author kent
 * @see PropertyName#VISIBILITY
 * @see org.silnith.browser.organic.property.accessor.VisibilityAccessor
 */
public enum Visibility implements Keyword {
    VISIBLE("visible"),
    HIDDEN("hidden"),
    COLLAPSE("collapse");
    
    private final String value;
    
    private Visibility(final String value) {
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
