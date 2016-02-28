package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.util.List;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * When an inline box contains an in-flow block-level box, the inline box (and its inline ancestors within the same line box) are broken around the block-level box (and any block-level siblings that are consecutive or separated only by collapsible whitespace and/or out-of-flow elements), splitting the inline box into two boxes (even if either side is empty), one on each side of the block-level box(es). The line boxes before the break and after the break are enclosed in anonymous block boxes, and the block-level box becomes a sibling of those anonymous boxes. When such an inline box is affected by relative positioning, any resulting translation also affects the block-level box contained in the inline box.
 * <p>
 * The properties of anonymous boxes are inherited from the enclosing non-anonymous box. Non-inherited properties have their initial value.
 * <p>
 * Properties set on elements that cause anonymous block boxes to be generated still apply to the boxes and content of that element.
 * <p>
 * Anonymous block boxes are ignored when resolving percentage values that would refer to it: the closest non-anonymous ancestor box is used instead.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#anonymous-block-level">9.2.1.1 Anonymous block boxes</a>
 */
public class AnonymousBlockBox implements BlockLevelBox, InlineFormattingContext {
    
    private final StyledElement parentElement;
    
    private final StyleData anonymousElementStyleData;
    
    private final InlineFormattingContextImpl inlineFormattingContextImpl;
    
    public AnonymousBlockBox(final StyledElement parentElement) {
        super();
        this.parentElement = parentElement;
        this.anonymousElementStyleData = StyleData.getAnonymousElementStyle(this.parentElement.getStyleData());
        
        this.inlineFormattingContextImpl = new InlineFormattingContextImpl();
    }
    
    @Override
    public synchronized void addChild(final InlineLevelBox child) {
        inlineFormattingContextImpl.addChild(child);
        
    }
    
    @Override
    public synchronized List<InlineLevelBox> getChildren() {
        return inlineFormattingContextImpl.getChildren();
    }
    
    @Override
    public ResolvedMarginInformation getMarginInformation(final AbsoluteLength parentWidth) {
        return MarginInformation.getMarginInformation(anonymousElementStyleData).resolve(parentWidth);
    }
    
    @Override
    public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
        return inlineFormattingContextImpl.negotiateWidth(containingBlockWidth, targetWidth, graphics);
    }
    
    @Override
    public RenderableContent layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
        return inlineFormattingContextImpl.layoutContents(containingBlockWidth, targetWidth, graphics);
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("AnonymousBlockBox");
        for (final PropertyName propertyName : PropertyName.values()) {
            if (anonymousElementStyleData.isPropertyComputed(propertyName)) {
                node.setAttribute(propertyName.getKey(),
                        String.valueOf(anonymousElementStyleData.getComputedValue(propertyName)));
            }
        }
        node.appendChild(inlineFormattingContextImpl.createDOM(document));
        return node;
    }
    
//	@Override
//	public String toString() {
//		return "AnonymousBlockBox {children: " + children + "}";
//	}

}
