package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "border-bottom-width" property.
 * 
 * @author kent
 * @see PropertyName#BORDER_BOTTOM_WIDTH
 */
public class BorderBottomWidthAccessor extends BorderWidthAccessor {
    
    public BorderBottomWidthAccessor(final LengthParser lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(PropertyName.BORDER_BOTTOM_WIDTH, lengthParser, fontSizeAccessor);
    }
    
}
