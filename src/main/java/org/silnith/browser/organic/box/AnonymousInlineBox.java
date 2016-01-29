package org.silnith.browser.organic.box;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import org.silnith.browser.organic.StyledText;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A formatting box that represents inline content in a block container element,
 * such as inline text.
 * This inline box participates in a {@link BlockFormattingContext}.
 * 
 * @author kent
 */
public class AnonymousInlineBox implements InlineLevelBox {

	private class RenderableText implements RenderableLineContent {

		private final TextLayout textLayout;

		private final float baseline;

		private final Dimension2D size;

		public RenderableText(final TextLayout textLayout) {
			super();
			this.textLayout = textLayout;
			this.baseline = this.textLayout.getAscent();
			this.size = new Dimension();
			this.size.setSize(this.textLayout.getAdvance(),
					this.baseline + this.textLayout.getDescent() + this.textLayout.getLeading());
		}

		@Override
		public Dimension2D getSize() {
			final Dimension2D dimension = new Dimension();
			dimension.setSize(size);
			return dimension;
		}

		@Override
		public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
			graphics.setColor(Color.BLACK);
			textLayout.draw(graphics, (float) startPoint.getX(), (float) startPoint.getY() + baseline);
			
//			final AbsoluteLength onePixel = new AbsoluteLength(Number.valueOf(1), AbsoluteUnit.PX);
//			
//			final BorderInformation borderInformation = new BorderInformation(
//					BorderStyle.SOLID, BorderStyle.SOLID, BorderStyle.SOLID, BorderStyle.SOLID,
//					Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
//					onePixel, onePixel, onePixel, onePixel);
//			
//			final RenderedBox box = new RenderedBox(size.width - 1, size.height - 1,
//					borderInformation);
//			box.paintComponent(startPoint, graphics);
		}

		@Override
		public float getBaseline() {
			return baseline;
		}

	}

	private class Results implements InlineLevelBox.LayoutResults {

		private final RenderableLineContent layedOutContent;

		private final boolean split;

		private final InlineLevelBox remainingContent;

		public Results(final RenderableLineContent layedOutContent) {
			super();
			if (layedOutContent == null) {
				throw new NullPointerException();
			}
			this.layedOutContent = layedOutContent;
			this.split = false;
			this.remainingContent = null;
		}

		public Results(final RenderableLineContent layedOutContent, final InlineLevelBox remainingContent) {
			super();
			if (layedOutContent == null) {
				throw new NullPointerException();
			}
			if (remainingContent == null) {
				throw new NullPointerException();
			}
			this.layedOutContent = layedOutContent;
			this.split = true;
			this.remainingContent = remainingContent;
		}

		@Override
		public RenderableLineContent getLayedOutContent() {
			return layedOutContent;
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

		private final LineBreakMeasurer lineBreakMeasurer;

		private final int textLength;

		public Continuation(final LineBreakMeasurer lineBreakMeasurer, final int textLength) {
			super();
			if (lineBreakMeasurer == null) {
				throw new NullPointerException();
			}
			this.lineBreakMeasurer = lineBreakMeasurer;
			this.textLength = textLength;
		}

		@Override
		public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth,
				final AbsoluteLength targetWidth, final Graphics2D graphics) {
			throw new IllegalStateException();
		}

		@Override
		public synchronized LayoutResults layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
				final Graphics2D graphics, final boolean canOverflow) {
			if (containingBlockWidth == null) {
				throw new NullPointerException();
			}
			if (targetWidth == null) {
				throw new NullPointerException();
			}
			
			return findNextTextLayout(lineBreakMeasurer, targetWidth, textLength, canOverflow);
		}

		@Override
		public Node createDOM(final Document document) {
			final Element node = document.createElement("AnonymousInlineBox.Continuation");
			node.appendChild(document.createTextNode(styledText.getText()));
			return node;
		}

	}

//	private final PropertyAccessor<AbsoluteLength> fontSizeAccessor;

	private final StyledText styledText;

	private final AbsoluteLength fontSize;

	public AnonymousInlineBox(final PropertyAccessor<AbsoluteLength> fontSizeAccessor, final StyledText styledText) {
		super();
		if (fontSizeAccessor == null) {
			throw new NullPointerException();
		}
		if (styledText == null) {
			throw new NullPointerException();
		}
//		this.fontSizeAccessor = fontSizeAccessor;
		this.styledText = styledText;
		this.fontSize = fontSizeAccessor.getComputedValue(styledText.getStyleData()).convertTo(AbsoluteUnit.PT);
	}

	private float getPixels(final AbsoluteLength length) {
		return length.convertTo(AbsoluteUnit.PX).getLength().floatValue();
	}

	private float getPoints(final AbsoluteLength length) {
		return length.convertTo(AbsoluteUnit.PT).getLength().floatValue();
	}

	private Font getFont(final Graphics2D graphics) {
		// TODO: font family, weight, transform
		final Font font = graphics.getFont().deriveFont(getPoints(fontSize));
		return font;
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
		
		final String text = styledText.getText();
		// derive font takes size in points, not pixels
		final Font font = getFont(graphics);
		final FontMetrics fontMetrics = graphics.getFontMetrics(font);
		
		/*
		 * Could use the map-based API to specify font attributes and let the
		 * system determine the physical font instead.
		 */
		final Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.FONT, font);
		
		final FontRenderContext frc = fontMetrics.getFontRenderContext();
		final BreakIterator br = BreakIterator.getWordInstance();
		br.setText(text);
		int start = br.first();
		int end = br.next();
		AbsoluteLength maxWordWidth = AbsoluteLength.max(targetWidth, AbsoluteLength.ZERO);
		while (end != BreakIterator.DONE) {
			final String word = text.substring(start, end);
			final TextLayout textLayout = new TextLayout(word, attributes, frc);
			final float advance = textLayout.getAdvance();
			final AbsoluteLength wordBoxLength = new AbsoluteLength(advance, AbsoluteUnit.PX);
			maxWordWidth = AbsoluteLength.max(maxWordWidth, wordBoxLength);
			
			start = end;
			end = br.next();
		}
		
		return maxWordWidth;
	}

	private LayoutResults findNextTextLayout(final LineBreakMeasurer lineBreakMeasurer,
			final AbsoluteLength targetWidth, final int endIndex, final boolean canOverflow) {
		final float width = Math.max(getPixels(targetWidth), 1f);
		TextLayout nextLayout = lineBreakMeasurer.nextLayout(width, endIndex, true);
		float incrementedWidth = width;
		while (canOverflow && nextLayout == null) {
			incrementedWidth += 1f;
			nextLayout = lineBreakMeasurer.nextLayout(incrementedWidth, endIndex, true);
		}
		if (nextLayout == null) {
			assert !canOverflow;
			return null;
		}
		final RenderableText renderableText = new RenderableText(nextLayout);
		if (lineBreakMeasurer.getPosition() == endIndex) {
			// we're done
			// return a result with isSplit() == false and no continuation
			return new Results(renderableText);
		} else {
			// make another continuation
			// return a result with isSplit() == true
			// add in a continuation object that keeps the current LineBreakMeasurer
			final Continuation continuation = new Continuation(lineBreakMeasurer, endIndex);
			return new Results(renderableText, continuation);
		}
	}

	@Override
	public synchronized LayoutResults layoutContents(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
			final Graphics2D graphics, final boolean canOverflow) {
		if (containingBlockWidth == null) {
			throw new NullPointerException();
		}
		if (targetWidth == null) {
			throw new NullPointerException();
		}
		
		final String text = styledText.getText();
		// derive font takes size in points, not pixels
		final Font font = getFont(graphics);
		final FontMetrics fontMetrics = graphics.getFontMetrics(font);
		
		if (text.isEmpty()) {
			// zero glyphs, but still have line height
			throw new UnsupportedOperationException();
		} else {
			final BreakIterator br = BreakIterator.getLineInstance();
			final AttributedString as = new AttributedString(text);
			as.addAttribute(TextAttribute.FONT, font);
			final AttributedCharacterIterator aci = as.getIterator();
			final FontRenderContext frc = fontMetrics.getFontRenderContext();
			final LineBreakMeasurer lbm = new LineBreakMeasurer(aci, br, frc);
			
			final int endIndex = aci.getEndIndex();
			return findNextTextLayout(lbm, targetWidth, endIndex, canOverflow);
		}
		
//		renderedText = new RenderedText(text, font, fontMetrics);
//		renderedBox = new RenderedBox(paddingLeft + renderedText.getWidth() + paddingRight, paddingTop + renderedText.getHeight() + paddingBottom,
//				borderTop, borderRight, borderBottom, borderLeft, Color.ORANGE);
//		size = new Dimension(renderedBox.getWidth(), renderedBox.getHeight());
//		textLocation = new Point(borderLeft + paddingLeft, borderTop + paddingTop);
//		return size;
	}

	@Override
	public Node createDOM(final Document document) {
		final Element node = document.createElement("AnonymousInlineBox");
		node.appendChild(document.createTextNode(styledText.getText()));
		return node;
	}

	@Override
	public String toString() {
		return "AnonymousInlineBox {content: " + styledText + "}";
	}

}
