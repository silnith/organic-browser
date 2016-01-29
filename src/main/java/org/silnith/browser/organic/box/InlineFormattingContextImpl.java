package org.silnith.browser.organic.box;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.silnith.browser.organic.RenderableContainer;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InlineFormattingContextImpl implements InlineFormattingContext, BlockLevelBox {

	private final List<InlineLevelBox> children;

	public InlineFormattingContextImpl() {
		super();
		this.children = new ArrayList<>();
	}

	@Override
	public synchronized void addChild(final InlineLevelBox child) {
		children.add(child);
	}

	@Override
	public synchronized List<InlineLevelBox> getChildren() {
		return new ArrayList<>(children);
	}

	@Override
	public ResolvedMarginInformation getMarginInformation(
			final AbsoluteLength parentWidth) {
		return new ResolvedMarginInformation(AbsoluteLength.ZERO, AbsoluteLength.ZERO, AbsoluteLength.ZERO, AbsoluteLength.ZERO);
	}

	@Override
	public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth, final Graphics2D graphics) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		AbsoluteLength childMinWidth = AbsoluteLength.max(targetWidth, AbsoluteLength.ZERO);
		for (final InlineLevelBox child : children) {
			final AbsoluteLength childNegotiatedWidth = child.negotiateWidth(containingBlockWidth, targetWidth, graphics);
			childMinWidth = AbsoluteLength.max(childMinWidth, childNegotiatedWidth);
		}
		
		return childMinWidth;
	}

	private void createNewLineBox(final List<RenderableLineContent> lineContent, final Dimension2D contentSize, final List<LineBox> lineBoxes) {
		assert !lineContent.isEmpty();
		
		final LineBox lineBox = new LineBox(lineContent);
		final Dimension2D lineBoxSize = lineBox.getSize();
		contentSize.setSize(Math.max(contentSize.getWidth(), lineBoxSize.getWidth()), contentSize.getHeight() + lineBoxSize.getHeight());
		lineBoxes.add(lineBox);
	}

	private float getPixels(final AbsoluteLength absoluteLength) {
		return absoluteLength.convertTo(AbsoluteUnit.PX).getLength().floatValue();
	}

	@Override
	public synchronized RenderableContent layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth, final Graphics2D graphics) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		final Dimension2D size = new Dimension();
		size.setSize(getPixels(targetWidth), 0);
		final List<LineBox> lineBoxes = new ArrayList<>();
		final List<RenderableLineContent> lineContents = new ArrayList<>();
		AbsoluteLength remainingWidth = targetWidth;
		/*
		 * Walk through the children, ask them to lay themselves out inside the
		 * remaining target width.  If they need to split, split them and resume
		 * walking on the next line with the remainder.
		 */
		if (children.isEmpty()) {
			final List<Point2D> childLocations = Collections.emptyList();
			final List<RenderableContent> childRenderables = Collections.emptyList();
			return new RenderableContainer(childLocations, childRenderables, new Dimension(0, 0));
		}
		final Iterator<InlineLevelBox> childIterator = children.iterator();
		InlineLevelBox currentChild = childIterator.next();
		boolean done = false;
		while ( !done) {
			final InlineLevelBox.LayoutResults initialResults = currentChild.layoutContents(containingBlockWidth, remainingWidth, graphics, lineContents.isEmpty());
			
			final InlineLevelBox.LayoutResults results;
			if (initialResults == null) {
				if (lineContents.isEmpty()) {
					System.out.println("How did this happen?");
					final List<Point2D> childLocations = Collections.emptyList();
					final List<RenderableContent> childRenderables = Collections.emptyList();
					return new RenderableContainer(childLocations, childRenderables, new Dimension(0, 0));
				}
				createNewLineBox(lineContents, size, lineBoxes);
				lineContents.clear();
				remainingWidth = targetWidth;
				
				assert lineContents.isEmpty();
				results = currentChild.layoutContents(containingBlockWidth, remainingWidth, graphics, lineContents.isEmpty());
			} else {
				results = initialResults;
			}
			assert results != null;
			
			final RenderableLineContent layedOutContent = results.getLayedOutContent();
			assert layedOutContent != null;
			
			lineContents.add(layedOutContent);
			final Dimension2D contentSize = layedOutContent.getSize();
			remainingWidth = remainingWidth.minus(new AbsoluteLength((float) contentSize.getWidth(), AbsoluteUnit.PX));
			if (results.isSplit()) {
				createNewLineBox(lineContents, size, lineBoxes);
				lineContents.clear();
				remainingWidth = targetWidth;
				
				currentChild = results.getRemainingContent();
			} else {
				if (childIterator.hasNext()) {
					currentChild = childIterator.next();
				} else {
					done = true;
				}
			}
		}
		
		if ( !lineContents.isEmpty()) {
			createNewLineBox(lineContents, size, lineBoxes);
			lineContents.clear();
			remainingWidth = targetWidth;
		}
		
		final List<Point2D> lineBoxLocations = new ArrayList<>();
		float currentOffset = 0;
		for (final LineBox box : lineBoxes) {
			lineBoxLocations.add(new Point2D.Float(0, currentOffset));
			currentOffset += box.getSize().getHeight();
		}
		final RenderableContainer renderable = new RenderableContainer(lineBoxLocations, lineBoxes, size);
		return renderable;
	}

	@Override
	public Node createDOM(final Document document) {
		final Element node = document.createElement("InlineFormattingContext");
		for (final InlineLevelBox child : children) {
			node.appendChild(child.createDOM(document));
		}
		return node;
	}

}
