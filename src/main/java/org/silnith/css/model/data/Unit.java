package org.silnith.css.model.data;

/**
 * A length unit in the CSS specification.
 * 
 * @author kent
 * @see AbsoluteUnit
 * @see PercentageUnit
 * @see RelativeUnit
 */
public interface Unit {
    
    /**
     * Returns the suffix used to identify this unit on values.
     * 
     * @return the unit value suffix
     */
    String getSuffix();
    
}
