package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;


public class MarginLeftAccessor extends MarginAccessor {
    
    public MarginLeftAccessor(final LengthParser lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(PropertyName.MARGIN_LEFT, lengthParser, fontSizeAccessor);
    }
    
}
