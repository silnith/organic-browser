package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InlineBox implements InlineLevelBox, InlineFormattingContext {

	private class Results implements LayoutResults {

		private final RenderableLineContent renderableLineContent;

		private final boolean split;

		private final InlineLevelBox remainingContent;

		public Results(final RenderableLineContent renderableLineContent) {
			super();
			if (renderableLineContent == null) {
				throw new NullPointerException();
			}
			this.renderableLineContent = renderableLineContent;
			this.split = false;
			this.remainingContent = null;
		}

		public Results(final RenderableLineContent renderableLineContent, final InlineLevelBox remainingContent) {
			super();
			if (renderableLineContent == null) {
				throw new NullPointerException();
			}
			if (remainingContent == null) {
				throw new NullPointerException();
			}
			this.renderableLineContent = renderableLineContent;
			this.split = true;
			this.remainingContent = remainingContent;
		}

		@Override
		public RenderableLineContent getLayedOutContent() {
			return renderableLineContent;
		}

		@Override
		public boolean isSplit() {
			return split;
		}

		@Override
		public InlineLevelBox getRemainingContent() {
			return remainingContent;
		}

	}

	private class Continuation implements InlineLevelBox {

		private InlineLevelBox currentChild;

		private final Iterator<InlineLevelBox> iterator;

		private final ResolvedPaddingInformation resolvedPaddingInformation;

		public Continuation(final InlineLevelBox currentChild, final Iterator<InlineLevelBox> iterator, final ResolvedPaddingInformation resolvedPaddingInformation) {
			super();
			this.currentChild = currentChild;
			this.iterator = iterator;
			this.resolvedPaddingInformation = resolvedPaddingInformation;
		}

		@Override
		public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth,
				final AbsoluteLength targetWidth, final Graphics2D graphics) {
			throw new IllegalStateException();
		}

		@Override
		public LayoutResults layoutContents(final AbsoluteLength containingBlockWidth,
				final AbsoluteLength targetWidth, final Graphics2D graphics,
				boolean canOverflow) {
			if (containingBlockWidth == null) {
				throw new NullPointerException();
			}
			if (targetWidth == null) {
				throw new NullPointerException();
			}
			
			AbsoluteLength childTargetWidth = targetWidth;
			childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingRight());
			childTargetWidth = childTargetWidth.minus(borderInformation.getBorderRightWidth());
			return fillLine(containingBlockWidth, childTargetWidth, graphics, canOverflow, resolvedPaddingInformation, currentChild, iterator, true);
		}

		@Override
		public Node createDOM(final Document document) {
			throw new IllegalStateException();
		}

	}

	private final StyledElement styledElement;

	private final List<InlineLevelBox> children;

	private final BackgroundInformation backgroundInformation;

	private final BorderInformation borderInformation;

	private final PaddingInformation paddingInformation;

	public InlineBox(final StyledElement styledElement) {
		super();
		this.styledElement = styledElement;
		this.children = new ArrayList<>();
		
		final StyleData styleData = styledElement.getStyleData();
		this.backgroundInformation = BackgroundInformation.getBackgroundInformation(styleData);
		this.borderInformation = BorderInformation.getBorderInformation(styleData);
		this.paddingInformation = PaddingInformation.getPaddingInformation(styleData);
	}

	@Override
	public void addChild(final InlineLevelBox child) {
		children.add(child);
	}

	@Override
	public List<InlineLevelBox> getChildren() {
		return children;
	}

	@Override
	public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth,
			final AbsoluteLength targetWidth, final Graphics2D graphics) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		final AbsoluteLength thisTargetWidth = AbsoluteLength.max(targetWidth, AbsoluteLength.ZERO);
		
		final ResolvedPaddingInformation resolvedPaddingInformation = paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
		AbsoluteLength childTargetWidth = thisTargetWidth;
		childTargetWidth = childTargetWidth.minus(borderInformation.getBorderLeftWidth());
		childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingLeft());
		childTargetWidth = childTargetWidth.minus(resolvedPaddingInformation.getPaddingRight());
		childTargetWidth = childTargetWidth.minus(borderInformation.getBorderRightWidth());
		childTargetWidth = AbsoluteLength.max(childTargetWidth, AbsoluteLength.ZERO);
		
		AbsoluteLength minChildWidth = childTargetWidth;
		for (final InlineLevelBox child : children) {
			final AbsoluteLength childNegotiatedWidth = child.negotiateWidth(containingBlockWidth, childTargetWidth, graphics);
			minChildWidth = AbsoluteLength.max(minChildWidth, childNegotiatedWidth);
		}
		
		AbsoluteLength finalWidth = minChildWidth;
		finalWidth = finalWidth.plus(borderInformation.getBorderLeftWidth());
		finalWidth = finalWidth.plus(resolvedPaddingInformation.getPaddingLeft());
		finalWidth = finalWidth.plus(resolvedPaddingInformation.getPaddingRight());
		finalWidth = finalWidth.plus(borderInformation.getBorderRightWidth());
		return finalWidth;
	}

	@Override
	public LayoutResults layoutContents(final AbsoluteLength containingBlockWidth,
			final AbsoluteLength targetWidth, final Graphics2D graphics,
			final boolean canOverflow) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		final AbsoluteLength borderRightWidth = borderInformation.getBorderRightWidth();
		final AbsoluteLength borderLeftWidth = borderInformation.getBorderLeftWidth();
		
		final ResolvedPaddingInformation resolvedPaddingInformation = paddingInformation.resolve(AbsoluteLength.max(containingBlockWidth, AbsoluteLength.ZERO));
		final AbsoluteLength paddingRight = resolvedPaddingInformation.getPaddingRight();
		final AbsoluteLength paddingLeft = resolvedPaddingInformation.getPaddingLeft();
		
		AbsoluteLength childTargetWidth = targetWidth;
		childTargetWidth = childTargetWidth.minus(borderLeftWidth);
		childTargetWidth = childTargetWidth.minus(paddingLeft);
		childTargetWidth = childTargetWidth.minus(paddingRight);
		childTargetWidth = childTargetWidth.minus(borderRightWidth);
		
		if (children.isEmpty()) {
			final List<RenderableLineContent> noChildren = Collections.emptyList();
			final WrappedLineBox wrapped = new WrappedLineBox(noChildren, false, false, backgroundInformation, borderInformation, resolvedPaddingInformation);
			return new Results(wrapped);
		}
		
		final Iterator<InlineLevelBox> iterator = children.iterator();
		InlineLevelBox currentChild = iterator.next();
		
		return fillLine(containingBlockWidth, childTargetWidth, graphics,
				canOverflow,
				resolvedPaddingInformation,
				currentChild, iterator, false);
	}

	private LayoutResults fillLine(final AbsoluteLength containingBlockWidth,
			AbsoluteLength remainingWidth, final Graphics2D graphics,
			final boolean canOverflow,
			final ResolvedPaddingInformation resolvedPaddingInformation,
			InlineLevelBox currentChild,
			final Iterator<InlineLevelBox> iterator, final boolean openAtStart) {
		final List<RenderableLineContent> renderables = new ArrayList<>();
		
		boolean done = (currentChild == null);
		while ( !done) {
			final LayoutResults childLayout = currentChild.layoutContents(containingBlockWidth, remainingWidth, graphics, canOverflow && renderables.isEmpty());
			
			if (childLayout == null) {
				break;
			}
			
			final RenderableLineContent childRenderable = childLayout.getLayedOutContent();
			
			renderables.add(childRenderable);
			
			final AbsoluteLength childWidth = new AbsoluteLength((float) childRenderable.getSize().getWidth(), AbsoluteUnit.PX);
			remainingWidth = remainingWidth.minus(childWidth);
			
			if (childLayout.isSplit()) {
				currentChild = childLayout.getRemainingContent();
				done = true;
			} else {
				if (iterator.hasNext()) {
					currentChild = iterator.next();
				} else {
					currentChild = null;
					done = true;
				}
			}
		}
		
		if (renderables.isEmpty()) {
			return null;
		} else {
			if (currentChild == null) {
				final WrappedLineBox wrappedLineBox = new WrappedLineBox(renderables,
						openAtStart, false, backgroundInformation, borderInformation, resolvedPaddingInformation);
				return new Results(wrappedLineBox);
			} else {
				final WrappedLineBox wrappedLineBox = new WrappedLineBox(renderables,
						openAtStart, true, backgroundInformation, borderInformation, resolvedPaddingInformation);
				return new Results(wrappedLineBox, new Continuation(currentChild, iterator, resolvedPaddingInformation));
			}
		}
	}

	@Override
	public Node createDOM(final Document document) {
		final Element node = document.createElement("InlineBox");
		for (final PropertyName propertyName : PropertyName.values()) {
			if (styledElement.getStyleData().isPropertyComputed(propertyName)) {
				node.setAttribute(propertyName.getKey(),
						String.valueOf(styledElement.getStyleData().getComputedValue(propertyName)));
			}
		}
		for (final InlineLevelBox child : children) {
			node.appendChild(child.createDOM(document));
		}
		return node;
	}

}
