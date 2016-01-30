package org.silnith.browser.organic.property.accessor;

import java.awt.Color;

import org.silnith.css.model.data.ColorParser;
import org.silnith.css.model.data.PropertyName;


public class BorderTopColorAccessor extends PropertyThatDependsOnTheColorPropertyAccessor {
    
    public BorderTopColorAccessor(final ColorParser colorParser, final PropertyAccessor<Color> colorPropertyAccessor) {
        super(PropertyName.BORDER_TOP_COLOR, colorParser, colorPropertyAccessor);
    }
    
}
