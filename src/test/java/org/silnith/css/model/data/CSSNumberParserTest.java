package org.silnith.css.model.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CSSNumberParserTest {

	private CSSNumberParser parser;

	@Before
	public void setUp() {
		parser = new CSSNumberParser();
	}

	@Test
	public void testParseZero() {
		final CSSNumber value = parser.parse("0");
		
		assertEquals(CSSNumber.valueOf(0), value);
	}

	@Test
	public void testParsePositiveZero() {
		final CSSNumber value = parser.parse("+0");
		
		assertEquals(CSSNumber.valueOf(0), value);
	}

	@Test
	public void testParseNegativeZero() {
		final CSSNumber value = parser.parse("-0");
		
		assertEquals(CSSNumber.valueOf(0), value);
	}

	@Test
	public void testParseOne() {
		final CSSNumber value = parser.parse("1");
		
		assertEquals(CSSNumber.valueOf(1), value);
	}

	@Test
	public void testParsePositiveOne() {
		final CSSNumber value = parser.parse("+1");
		
		assertEquals(CSSNumber.valueOf(1), value);
	}

	@Test
	public void testParseNegativeOne() {
		final CSSNumber value = parser.parse("-1");
		
		assertEquals(CSSNumber.valueOf(-1), value);
	}

	@Test
	public void testParseOneHalfWithLeadingZero() {
		final CSSNumber value = parser.parse("0.5");
		
		assertEquals(CSSNumber.valueOf(0.5f), value);
	}

	@Test
	public void testParsePositiveOneHalfWithLeadingZero() {
		final CSSNumber value = parser.parse("+0.5");
		
		assertEquals(CSSNumber.valueOf(0.5f), value);
	}

	@Test
	public void testParseNegativeOneHalfWithLeadingZero() {
		final CSSNumber value = parser.parse("-0.5");
		
		assertEquals(CSSNumber.valueOf(-0.5f), value);
	}

	@Test
	public void testParseOneHalfNoLeadingZero() {
		final CSSNumber value = parser.parse(".5");
		
		assertEquals(CSSNumber.valueOf(0.5f), value);
	}

	@Test
	public void testParsePositiveOneHalfNoLeadingZero() {
		final CSSNumber value = parser.parse("+.5");
		
		assertEquals(CSSNumber.valueOf(0.5f), value);
	}

	@Test
	public void testParseNegativeOneHalfNoLeadingZero() {
		final CSSNumber value = parser.parse("-.5");
		
		assertEquals(CSSNumber.valueOf(-0.5f), value);
	}

	@Test
	public void testParseWeirdNumber() {
		final CSSNumber value = parser.parse("9248.37132");
		
		assertEquals(CSSNumber.valueOf(9248.37132f), value);
	}

	@Test
	public void testParsePositiveWeirdNumber() {
		final CSSNumber value = parser.parse("+9248.37132");
		
		assertEquals(CSSNumber.valueOf(9248.37132f), value);
	}

	@Test
	public void testParseNegativeWeirdNumber() {
		final CSSNumber value = parser.parse("-9248.37132");
		
		assertEquals(CSSNumber.valueOf(-9248.37132f), value);
	}

	@Test
	public void testParseNumberWithExponent() {
		final CSSNumber value = parser.parse("1e12");
		
		assertNull(value);
	}

	@Test
	public void testParsePositiveNumberWithExponent() {
		final CSSNumber value = parser.parse("+1e12");
		
		assertNull(value);
	}

	@Test
	public void testParseNegativeNumberWithExponent() {
		final CSSNumber value = parser.parse("-1e12");
		
		assertNull(value);
	}

	@Test
	public void testParseLeadingZeros() {
		final CSSNumber value = parser.parse("0000015");
		
		assertEquals(CSSNumber.valueOf(15), value);
	}

	@Test
	public void testParseHexadecimal() {
		final CSSNumber value = parser.parse("0x10");
		
		assertNull(value);
	}

	@Test
	public void testParseTrailingDot() {
		final CSSNumber value = parser.parse("1.");
		
		assertNull(value);
	}

}
