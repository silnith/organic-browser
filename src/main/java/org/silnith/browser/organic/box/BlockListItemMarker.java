package org.silnith.browser.organic.box;

import org.silnith.browser.organic.StyledElement;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;


public class BlockListItemMarker extends AnonymousBlockBox {
    
    public BlockListItemMarker(final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final StyledElement parentElement) {
        super(parentElement);
        this.addChild(new InlineListItemMarker(fontSizeAccessor, parentElement));
    }
    
//	@Override
//	public Node createDOM(final Document document) {
//		final Element node = document.createElement("BlockListItemMarker");
//		for (final PropertyName propertyName : PropertyName.values()) {
//			if (anonymousElementStyleData.isPropertyComputed(propertyName)) {
//				node.setAttribute(propertyName.getKey(),
//						String.valueOf(anonymousElementStyleData.getComputedValue(propertyName)));
//			}
//		}
//		node.appendChild(inlineFormattingContextImpl.createDOM(document));
//		return node;
//	}

}
