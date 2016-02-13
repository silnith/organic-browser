package org.silnith.browser.organic.property.accessor;

import java.awt.Color;

import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "border-top-color" property.
 * 
 * @author kent
 * @see PropertyName#BORDER_TOP_COLOR
 */
public class BorderTopColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {
    
    public BorderTopColorAccessor(final ColorParser colorParser, final PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.BORDER_TOP_COLOR, colorParser, colorPropertyAccessor);
    }
    
}
