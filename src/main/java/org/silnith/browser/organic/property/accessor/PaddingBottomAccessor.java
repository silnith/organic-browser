package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;


public class PaddingBottomAccessor extends PaddingAccessor {
    
    public PaddingBottomAccessor(final LengthParser lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(PropertyName.PADDING_BOTTOM, lengthParser, fontSizeAccessor);
    }
    
}
