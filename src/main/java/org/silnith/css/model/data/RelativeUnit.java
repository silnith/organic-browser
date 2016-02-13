package org.silnith.css.model.data;

/**
 * An enumeration of all the relative units in the CSS specification.
 * 
 * @author kent
 */
public enum RelativeUnit implements Unit {
    EM("em"),
    EX("ex");
    
    private final String suffix;
    
    private RelativeUnit(final String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public String getSuffix() {
        return suffix;
    }
    
}
