package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "border-right-width" property.
 * 
 * @author kent
 * @see PropertyName#BORDER_RIGHT_WIDTH
 */
public class BorderRightWidthAccessor extends BorderWidthAccessor {
    
    public BorderRightWidthAccessor(final LengthParser<?> lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(PropertyName.BORDER_RIGHT_WIDTH, lengthParser, fontSizeAccessor);
    }
    
}
