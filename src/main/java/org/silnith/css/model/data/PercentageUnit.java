package org.silnith.css.model.data;

/**
 * An enumeration with the percentage unit from the CSS specification.
 * The interpretation of the percentage value is specific to each property.
 * 
 * @author kent
 */
public enum PercentageUnit implements Unit {
    PERCENTAGE("%");
    
    private final String suffix;
    
    private PercentageUnit(final String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public String getSuffix() {
        return suffix;
    }
    
}
