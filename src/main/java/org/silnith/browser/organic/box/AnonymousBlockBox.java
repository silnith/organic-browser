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

public class AnonymousBlockBox implements BlockLevelBox,
		InlineFormattingContext {

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
	public ResolvedMarginInformation getMarginInformation(
			final AbsoluteLength parentWidth) {
		return MarginInformation.getMarginInformation(anonymousElementStyleData).resolve(parentWidth);
	}

	@Override
	public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth,
			final AbsoluteLength targetWidth, final Graphics2D graphics) {
		return inlineFormattingContextImpl.negotiateWidth(containingBlockWidth, targetWidth, graphics);
	}

	@Override
	public RenderableContent layoutContents(final AbsoluteLength containingBlockWidth,
			final AbsoluteLength targetWidth, final Graphics2D graphics) {
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
