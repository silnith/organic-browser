package org.silnith.browser.organic.box;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.RenderableContainer;
import org.silnith.browser.organic.RenderedBackground;
import org.silnith.browser.organic.RenderedBox;
import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BlockBoxForFlow implements BlockLevelBox,
		InlineFormattingContext {

	private final StyledElement styledElement;

	private final BackgroundInformation backgroundInformation;

	private final MarginInformation marginInformation;

	private final BorderInformation borderInformation;

	private final PaddingInformation paddingInformation;

	private final InlineFormattingContextImpl inlineFormattingContextImpl;

	public BlockBoxForFlow(final StyledElement styledElement) {
		super();
		this.styledElement = styledElement;
		final StyleData styleData = this.styledElement.getStyleData();
		this.backgroundInformation = BackgroundInformation.getBackgroundInformation(styleData);
		this.marginInformation = MarginInformation.getMarginInformation(styleData);
		this.borderInformation = BorderInformation.getBorderInformation(styleData);
		this.paddingInformation = PaddingInformation.getPaddingInformation(styleData);
		
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

	private AbsoluteLength resolvePercentageLength(final Length<?> length, final AbsoluteLength containingBlockWidth) {
		switch (length.getType()) {
		case ABSOLUTE: {
			final AbsoluteLength absoluteLength = (AbsoluteLength) length;
			return absoluteLength;
		} // break;
		case RELATIVE: {
//			final RelativeLength relativeLength = (RelativeLength) length;
			throw new IllegalStateException("Property should have been resolved into an absolute length before layout: " + length);
		} // break;
		case PERCENTAGE: {
			final PercentageLength percentageLength = (PercentageLength) length;
			final AbsoluteLength resolvedLength = percentageLength.resolve(containingBlockWidth);
			return resolvedLength;
		} // break;
		default: {
			throw new IllegalArgumentException("Unknown length type: " + length);
		} // break;
		}
	}

	private float getPixels(final AbsoluteLength absoluteLength) {
		return absoluteLength.convertTo(AbsoluteUnit.PX).getLength().floatValue();
	}

	@Override
	public ResolvedMarginInformation getMarginInformation(
			final AbsoluteLength parentWidth) {
		return marginInformation.resolve(parentWidth);
	}

	@Override
	public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth, final Graphics2D graphics) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		final ResolvedPaddingInformation resolvedPaddingInformation = paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
		
		AbsoluteLength childContainingBlockWidth = targetWidth;
		childContainingBlockWidth = childContainingBlockWidth.minus(borderInformation.getBorderLeftWidth());
		childContainingBlockWidth = childContainingBlockWidth.minus(resolvedPaddingInformation.getPaddingLeft());
		childContainingBlockWidth = childContainingBlockWidth.minus(resolvedPaddingInformation.getPaddingRight());
		childContainingBlockWidth = childContainingBlockWidth.minus(borderInformation.getBorderRightWidth());
		childContainingBlockWidth = AbsoluteLength.max(childContainingBlockWidth, AbsoluteLength.ZERO);
		
		// TODO: do inline elements get margins?
		final AbsoluteLength childNegotiatedWidth = inlineFormattingContextImpl.negotiateWidth(childContainingBlockWidth, childContainingBlockWidth, graphics);
		
		AbsoluteLength minWidth = AbsoluteLength.max(childContainingBlockWidth, childNegotiatedWidth);
		minWidth = minWidth.plus(borderInformation.getBorderLeftWidth());
		minWidth = minWidth.plus(resolvedPaddingInformation.getPaddingLeft());
		minWidth = minWidth.plus(resolvedPaddingInformation.getPaddingRight());
		minWidth = minWidth.plus(borderInformation.getBorderRightWidth());
		return minWidth;
	}

	@Override
	public synchronized RenderableContent layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth, final Graphics2D graphics) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		// pull padding and border from styled element
		final ResolvedPaddingInformation resolvedPaddingInformation = paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
		
		AbsoluteLength childTargetWidth = targetWidth;
		childTargetWidth = childTargetWidth.minus(borderInformation.getBorderLeftWidth());
		childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingLeft());
		childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingRight());
		childTargetWidth = childTargetWidth.minus(borderInformation.getBorderRightWidth());
		childTargetWidth = AbsoluteLength.max(childTargetWidth, AbsoluteLength.ZERO);
		
		final float borderTop = getPixels(borderInformation.getBorderTopWidth());
		final float borderRight = getPixels(borderInformation.getBorderRightWidth());
		final float borderBottom = getPixels(borderInformation.getBorderBottomWidth());
		final float borderLeft = getPixels(borderInformation.getBorderLeftWidth());
		final float paddingTop = getPixels(resolvedPaddingInformation.getPaddingTop());
		final float paddingRight = getPixels(resolvedPaddingInformation.getPaddingRight());
		final float paddingBottom = getPixels(resolvedPaddingInformation.getPaddingBottom());
		final float paddingLeft = getPixels(resolvedPaddingInformation.getPaddingLeft());
		
		final RenderableContent renderableContent = inlineFormattingContextImpl.layoutContents(containingBlockWidth, childTargetWidth, graphics);
		final Dimension2D renderableSize = renderableContent.getSize();
		
		final List<RenderableContent> childRenderables = new ArrayList<>();
		final List<Point2D> childLocations = new ArrayList<>();
		
		/*
		 * TODO: Do we want to expand to fill the available horizontal space?
		 */
		final RenderedBox box = new RenderedBox(paddingLeft + (float) renderableSize.getWidth() + paddingRight, paddingTop + (float) renderableSize.getHeight() + paddingBottom,
				borderInformation);
		final Dimension2D size = new Dimension();
		size.setSize(borderLeft + paddingLeft + (float) renderableSize.getWidth() + paddingRight + borderRight,
				borderTop + paddingTop + (float) renderableSize.getHeight() + paddingBottom + borderBottom);
		
		final RenderedBackground background = new RenderedBackground(size, backgroundInformation.getColor());
		
		childLocations.add(new Point2D.Float());
		childRenderables.add(background);
		
		childLocations.add(new Point2D.Float());
		childRenderables.add(box);
		
		childLocations.add(new Point2D.Float(borderLeft + paddingLeft, borderTop + paddingTop));
		childRenderables.add(renderableContent);
		
		return new RenderableContainer(childLocations, childRenderables, size);
	}

	@Override
	public Node createDOM(final Document document) {
		final Element node = document.createElement("BlockBoxForFlow");
		for (final PropertyName propertyName : PropertyName.values()) {
			if (styledElement.getStyleData().isPropertyComputed(propertyName)) {
				node.setAttribute(propertyName.getKey(),
						String.valueOf(styledElement.getStyleData().getComputedValue(propertyName)));
			}
		}
		node.appendChild(inlineFormattingContextImpl.createDOM(document));
		return node;
	}

	@Override
	public String toString() {
		return "BlockBoxForFlow {inline: " + inlineFormattingContextImpl + "}";
	}

}
