package org.silnith.browser.organic.box;

import java.awt.Color;
import java.util.List;

import org.silnith.browser.organic.StyledDOMElement;
import org.silnith.browser.organic.StyledElement;
import org.silnith.browser.organic.StyledText;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.FontStyle;
import org.silnith.css.model.data.FontWeight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * An inline-level box for an element with "display: list-item" and
 * "list-style-position: inside".
 * 
 * @author kent
 */
public class InlineListItemMarker extends AnonymousInlineBox {
    
    private final StyledElement listItemElement;
    
    /**
     * @param listItemElement the list item being marked
     */
    public InlineListItemMarker(final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final PropertyAccessor<Color> colorAccessor,
            final PropertyAccessor<List<String>> fontFamilyAccessor,
            final PropertyAccessor<FontStyle> fontStyleAccessor,
            final PropertyAccessor<FontWeight> fontWeightAccessor,
            final StyledDOMElement listItemElement) {
        super(colorAccessor, fontSizeAccessor, fontFamilyAccessor, fontStyleAccessor, fontWeightAccessor, new StyledText(listItemElement, "1. "));
        this.listItemElement = listItemElement;
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("InlineListItemMarker");
        node.appendChild(document.createTextNode("1. "));
        return node;
    }
    
}
