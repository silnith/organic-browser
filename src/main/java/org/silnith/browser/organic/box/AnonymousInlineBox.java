package org.silnith.browser.organic.box;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.silnith.browser.organic.StyledText;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.FontStyle;
import org.silnith.css.model.data.FontWeight;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * A formatting box that represents inline content in a block container element,
 * such as inline text. This inline box participates in a
 * {@link BlockFormattingContext}.
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
        public synchronized LayoutResults layoutContents(final AbsoluteLength containingBlockWidth,
                final AbsoluteLength targetWidth, final Graphics2D graphics, final boolean canOverflow) {
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
    
    private final StyledText styledText;
    
    private final String fontFamilyName;
    
    private final AbsoluteLength fontSize;
    
    private final FontStyle fontStyle;
    
    private final FontWeight fontWeight;
    
    public AnonymousInlineBox(final PropertyAccessor<AbsoluteLength> fontSizeAccessor,
            final PropertyAccessor<List<String>> fontFamilyAccessor,
            final PropertyAccessor<FontStyle> fontStyleAccessor,
            final PropertyAccessor<FontWeight> fontWeightAccessor,
            final StyledText styledText) {
        super();
        if (fontSizeAccessor == null) {
            throw new NullPointerException();
        }
        if (styledText == null) {
            throw new NullPointerException();
        }
        this.styledText = styledText;
        this.fontFamilyName = fontFamilyAccessor.getComputedValue(styledText.getStyleData()).get(0);
        this.fontSize = fontSizeAccessor.getComputedValue(styledText.getStyleData()).convertTo(AbsoluteUnit.PT);
        this.fontStyle = fontStyleAccessor.getComputedValue(styledText.getStyleData());
        this.fontWeight = fontWeightAccessor.getComputedValue(styledText.getStyleData());
    }
    
    private float getPixels(final AbsoluteLength length) {
        return length.convertTo(AbsoluteUnit.PX).getLength().floatValue();
    }
    
    private float getPoints(final AbsoluteLength length) {
        return length.convertTo(AbsoluteUnit.PT).getLength().floatValue();
    }
    
    private Font getFont(final Graphics2D graphics) {
        /*
         * Note that the font size is specified twice.  The first time is an
         * integer, the second time is a float.  The second value should be
         * applied.
         */
        int style = Font.PLAIN;
        final Map<AttributedCharacterIterator.Attribute, Object> textAttributes = new HashMap<>();
        switch (fontStyle) {
        case NORMAL: {
            textAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
        } break;
        case ITALIC: {
            style = Font.ITALIC;
        } break;
        case OBLIQUE: {
            textAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        } break;
        }
        final Font font = new Font(fontFamilyName, style, (int) getPoints(fontSize));
        textAttributes.put(TextAttribute.SIZE, getPoints(fontSize));
        textAttributes.put(TextAttribute.WEIGHT, fontWeightAttributes.get(fontWeight));
        return font.deriveFont(textAttributes);
    }
    
    private static final Map<FontWeight, Float> fontWeightAttributes;
    static {
        fontWeightAttributes = new EnumMap<>(FontWeight.class);
        fontWeightAttributes.put(FontWeight.WEIGHT_100, TextAttribute.WEIGHT_EXTRA_LIGHT);
        fontWeightAttributes.put(FontWeight.WEIGHT_200, TextAttribute.WEIGHT_LIGHT);
        fontWeightAttributes.put(FontWeight.WEIGHT_300, TextAttribute.WEIGHT_DEMILIGHT);
        fontWeightAttributes.put(FontWeight.WEIGHT_400, TextAttribute.WEIGHT_REGULAR);
        fontWeightAttributes.put(FontWeight.WEIGHT_500, TextAttribute.WEIGHT_SEMIBOLD);
        fontWeightAttributes.put(FontWeight.WEIGHT_600, TextAttribute.WEIGHT_DEMIBOLD);
        fontWeightAttributes.put(FontWeight.WEIGHT_700, TextAttribute.WEIGHT_BOLD);
        fontWeightAttributes.put(FontWeight.WEIGHT_800, TextAttribute.WEIGHT_HEAVY);
        fontWeightAttributes.put(FontWeight.WEIGHT_900, TextAttribute.WEIGHT_ULTRABOLD);
    }
    
    @Override
    public AbsoluteLength negotiateWidth(final AbsoluteLength containingBlockWidth, final AbsoluteLength targetWidth,
            final Graphics2D graphics) {
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
            // add in a continuation object that keeps the current
            // LineBreakMeasurer
            final Continuation continuation = new Continuation(lineBreakMeasurer, endIndex);
            return new Results(renderableText, continuation);
        }
    }
    
    private static final Map<Object, Object> renderingHints;
    
    static {
        renderingHints = new HashMap<>();
        renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
    
    @Override
    public synchronized LayoutResults layoutContents(final AbsoluteLength containingBlockWidth,
            final AbsoluteLength targetWidth, final Graphics2D graphics, final boolean canOverflow) {
        if (containingBlockWidth == null) {
            throw new NullPointerException();
        }
        if (targetWidth == null) {
            throw new NullPointerException();
        }
        
        final String text = styledText.getText();
        graphics.addRenderingHints(renderingHints);
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
