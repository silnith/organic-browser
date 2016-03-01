package org.silnith.browser.organic.property.accessor;

import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;

public class OutlineWidthAccessor extends BorderWidthAccessor {

    public OutlineWidthAccessor(LengthParser<?> lengthParser,
            PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(PropertyName.OUTLINE_WIDTH, lengthParser, fontSizeAccessor);
        
    }

}
