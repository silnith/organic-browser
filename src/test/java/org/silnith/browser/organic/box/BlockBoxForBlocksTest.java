package org.silnith.browser.organic.box;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics2D;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.StyledElement;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.CSSNumber;

//@RunWith(MockitoJUnitRunner.class)
public class BlockBoxForBlocksTest {

	@Mock
	private StyledElement styledElement;

	private StyleData styleData;

	@Mock
	private Graphics2D graphics2d;

	private BlockBoxForBlocks blockBoxForBlocks;

//	@Before
	public void setUp() {
		styleData = new StyleData(null);
		when(styledElement.getStyleData()).thenReturn(styleData);
		
		blockBoxForBlocks = new BlockBoxForBlocks(styledElement);
	}

//	@Test
	public void testGetMarginInformation() {
		final AbsoluteLength containingBlockWidth = new AbsoluteLength(100, AbsoluteUnit.PX);
		
		blockBoxForBlocks.getMarginInformation(containingBlockWidth);
		
		fail("Not yet implemented");
	}

//	@Test
	public void testNegotiateWidth() {
		final AbsoluteLength containingBlockWidth = new AbsoluteLength(100, AbsoluteUnit.PX);
		
		final AbsoluteLength negotiatedWidth = blockBoxForBlocks.negotiateWidth(containingBlockWidth, containingBlockWidth, graphics2d);
		
		fail("Not yet implemented");
	}

//	@Test
	public void testLayoutContents() {
		fail("Not yet implemented");
	}

}
