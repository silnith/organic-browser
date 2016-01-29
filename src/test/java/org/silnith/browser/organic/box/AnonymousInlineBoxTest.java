package org.silnith.browser.organic.box;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.CharacterIterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledText;
import org.silnith.browser.organic.box.InlineLevelBox.LayoutResults;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;

@RunWith(MockitoJUnitRunner.class)
public class AnonymousInlineBoxTest {

	private static final float DELTA = 1f / 1024f;

	@Mock
	private PropertyAccessor<AbsoluteLength> fontSizeAccessor;

	@Mock
	private StyledText styledText;

	@Mock
	private Graphics2D graphics2d;

	private Font font;

	@Mock
	private FontMetrics fontMetrics;

	@Mock
	private FontRenderContext fontRenderContext;

	private AnonymousInlineBox anonymousInlineBox;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		font = new Font("serif", Font.PLAIN, 12);
		final AbsoluteLength fontSize = new AbsoluteLength(12, AbsoluteUnit.PT);
		
		when(fontSizeAccessor.getComputedValue(any(StyleData.class))).thenReturn(fontSize);
		when(graphics2d.getFont()).thenReturn(font);
		when(graphics2d.getFontMetrics(any(Font.class))).thenReturn(fontMetrics);
		when(fontMetrics.getFontRenderContext()).thenReturn(fontRenderContext);
		when(fontRenderContext.getAntiAliasingHint()).thenReturn(true);
		when(fontRenderContext.isAntiAliased()).thenReturn(true);
		when(fontRenderContext.getFractionalMetricsHint()).thenReturn(true);
		when(fontRenderContext.usesFractionalMetrics()).thenReturn(true);
		when(fontRenderContext.equals(any(FontRenderContext.class))).thenReturn(false);
		when(fontRenderContext.isTransformed()).thenReturn(false);
		when(fontRenderContext.getTransform()).thenReturn(new AffineTransform());
		when(fontRenderContext.getTransformType()).thenReturn(AffineTransform.TYPE_IDENTITY);
		
		when(fontMetrics.bytesWidth(any(byte[].class), anyInt(), anyInt())).thenThrow(new RuntimeException("FontMetrics.bytesWidth"));
		when(fontMetrics.charsWidth(any(char[].class), anyInt(), anyInt())).thenThrow(new RuntimeException("FontMetrics.charsWidth"));
		when(fontMetrics.charWidth(anyChar())).thenThrow(new RuntimeException("FontMetrics.charWidth"));
		when(fontMetrics.charWidth(anyInt())).thenThrow(new RuntimeException("FontMetrics.charWidth"));
		when(fontMetrics.getAscent()).thenThrow(new RuntimeException("FontMetrics.getAscent"));
		when(fontMetrics.getDescent()).thenThrow(new RuntimeException("FontMetrics.getDescent"));
		when(fontMetrics.getFont()).thenThrow(new RuntimeException("FontMetrics.getFont"));
		when(fontMetrics.getHeight()).thenThrow(new RuntimeException("FontMetrics.getHeight"));
		when(fontMetrics.getLeading()).thenThrow(new RuntimeException("FontMetrics.getHeight"));
		when(fontMetrics.getLineMetrics(any(String.class), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getLineMetrics"));
		when(fontMetrics.getLineMetrics(any(char[].class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getLineMetrics"));
		when(fontMetrics.getLineMetrics(any(CharacterIterator.class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getLineMetrics"));
		when(fontMetrics.getLineMetrics(any(String.class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getLineMetrics"));
		when(fontMetrics.getMaxAdvance()).thenThrow(new RuntimeException("FontMetrics.getMaxAdvance"));
		when(fontMetrics.getMaxAscent()).thenThrow(new RuntimeException("FontMetrics.getMaxAscent"));
		when(fontMetrics.getMaxCharBounds(any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getMaxCharBounds"));
		when(fontMetrics.getMaxDecent()).thenThrow(new RuntimeException("FontMetrics.getMaxDecent"));
		when(fontMetrics.getMaxDescent()).thenThrow(new RuntimeException("FontMetrics.getMaxDescent"));
		when(fontMetrics.getStringBounds(any(String.class), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getStringBounds"));
		when(fontMetrics.getStringBounds(any(char[].class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getStringBounds"));
		when(fontMetrics.getStringBounds(any(CharacterIterator.class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getStringBounds"));
		when(fontMetrics.getStringBounds(any(String.class), anyInt(), anyInt(), any(Graphics.class))).thenThrow(new RuntimeException("FontMetrics.getStringBounds"));
		when(fontMetrics.getWidths()).thenThrow(new RuntimeException("FontMetrics.getWidths"));
		when(fontMetrics.hasUniformLineMetrics()).thenThrow(new RuntimeException("FontMetrics.getWidths"));
		when(fontMetrics.stringWidth(any(String.class))).thenThrow(new RuntimeException("FontMetrics.stringWidth"));
	}

	@Test
	public void testLayoutContents() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(5, AbsoluteUnit.IN);
		final AbsoluteLength targetWidth = new AbsoluteLength(5, AbsoluteUnit.IN);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, true);
		
		assertNotNull(layoutResults);
	}

	@Test
	public void testLayoutContentsCannotFitNoOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		final AbsoluteLength targetWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, false);
		
		assertNull(layoutResults);
	}

	@Test
	public void testLayoutContentsCannotFitWithOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		final AbsoluteLength targetWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, true);
		
		assertNotNull(layoutResults);
		final RenderableLineContent layedOutContent = layoutResults.getLayedOutContent();
		assertNotNull(layedOutContent);
		assertTrue(layedOutContent.getSize().getWidth() > 1f);
		TextLayout tl = new TextLayout("This ", font, fontRenderContext);
		assertEquals(tl.getAdvance(), layedOutContent.getSize().getWidth(), 2.0);
		final InlineLevelBox remainingContent = layoutResults.getRemainingContent();
		assertNotNull(remainingContent);
	}

	@Test
	public void testLayoutContentsZeroWidthNoOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = AbsoluteLength.ZERO;
		final AbsoluteLength targetWidth = AbsoluteLength.ZERO;
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, false);
		
		assertNull(layoutResults);
	}

	@Test
	public void testLayoutContentsZeroWidthWithOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(-1, AbsoluteUnit.PX);
		final AbsoluteLength targetWidth = new AbsoluteLength(-1, AbsoluteUnit.PX);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, true);
		
		assertNotNull(layoutResults);
		final RenderableLineContent layedOutContent = layoutResults.getLayedOutContent();
		assertNotNull(layedOutContent);
		assertTrue(layedOutContent.getSize().getWidth() > 1f);
		TextLayout tl = new TextLayout("This ", font, fontRenderContext);
		assertEquals(tl.getAdvance(), layedOutContent.getSize().getWidth(), 2.0);
		final InlineLevelBox remainingContent = layoutResults.getRemainingContent();
		assertNotNull(remainingContent);
	}

	@Test
	public void testLayoutContentsNegativeWidthNoOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(-1, AbsoluteUnit.PX);
		final AbsoluteLength targetWidth = new AbsoluteLength(-1, AbsoluteUnit.PX);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, false);
		
		assertNull(layoutResults);
	}

	@Test
	public void testLayoutContentsNegativeWidthWithOverflow() {
		when(styledText.getText()).thenReturn("This is a test.");
		
		anonymousInlineBox = new AnonymousInlineBox(fontSizeAccessor, styledText);
		
		final AbsoluteLength parentWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		final AbsoluteLength targetWidth = new AbsoluteLength(1, AbsoluteUnit.PX);
		
		final LayoutResults layoutResults = anonymousInlineBox.layoutContents(parentWidth, targetWidth, graphics2d, true);
		
		assertNotNull(layoutResults);
		final RenderableLineContent layedOutContent = layoutResults.getLayedOutContent();
		assertNotNull(layedOutContent);
		assertTrue(layedOutContent.getSize().getWidth() > 1f);
		TextLayout tl = new TextLayout("This ", font, fontRenderContext);
		assertEquals(tl.getAdvance(), layedOutContent.getSize().getWidth(), 2.0);
		final InlineLevelBox remainingContent = layoutResults.getRemainingContent();
		assertNotNull(remainingContent);
	}

}
