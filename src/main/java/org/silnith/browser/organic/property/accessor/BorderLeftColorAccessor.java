package org.silnith.browser.organic.property.accessor;

import java.awt.Color;

import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


public class BorderLeftColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {
    
    public BorderLeftColorAccessor(final ColorParser colorParser, final PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.BORDER_LEFT_COLOR, colorParser, colorPropertyAccessor);
    }
    
}
