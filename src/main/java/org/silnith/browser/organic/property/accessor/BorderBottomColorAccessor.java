package org.silnith.browser.organic.property.accessor;

import java.awt.Color;

import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "border-bottom-color" property.
 * 
 * @author kent
 * @see PropertyName#BORDER_BOTTOM_COLOR
 */
public class BorderBottomColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {
    
    public BorderBottomColorAccessor(final ColorParser colorParser,
            final PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.BORDER_BOTTOM_COLOR, colorParser, colorPropertyAccessor);
    }
    
}
