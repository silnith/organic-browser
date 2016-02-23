package org.silnith.browser.organic.box;

import org.silnith.browser.organic.StyledElement;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.FontStyle;


public class BlockListItemMarker extends AnonymousBlockBox {
    
    public BlockListItemMarker(final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final PropertyAccessor<FontStyle> fontStyleAccessor,
            final StyledElement parentElement) {
        super(parentElement);
        this.addChild(new InlineListItemMarker(fontSizeAccessor, fontStyleAccessor, parentElement));
    }

}
