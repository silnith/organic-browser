package org.silnith.browser.organic.property.accessor;

import java.awt.Color;

import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


public class BorderRightColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {
    
    public BorderRightColorAccessor(final ColorParser colorParser,
            final PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.BORDER_RIGHT_COLOR, colorParser, colorPropertyAccessor);
    }
    
}
