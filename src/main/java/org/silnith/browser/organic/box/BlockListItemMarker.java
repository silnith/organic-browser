package org.silnith.browser.organic.box;

import java.awt.Color;
import java.util.List;

import org.silnith.browser.organic.StyledDOMElement;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.FontStyle;
import org.silnith.css.model.data.FontWeight;


public class BlockListItemMarker extends AnonymousBlockBox {
    
    public BlockListItemMarker(
            final PropertyAccessor<Color> colorAccessor,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final PropertyAccessor<List<String>> fontFamilyAccessor,
            final PropertyAccessor<FontStyle> fontStyleAccessor,
            final PropertyAccessor<FontWeight> fontWeightAccessor,
            final StyledDOMElement parentElement) {
        super(parentElement);
        this.addChild(new InlineListItemMarker(fontSizeAccessor, colorAccessor, fontFamilyAccessor, fontStyleAccessor, fontWeightAccessor, parentElement));
    }

}
