package org.silnith.css.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class CSSNumberTest {

	@Test
	public void testIntValue() {
		final int value = 257;
		
		assertEquals(value, CSSNumber.valueOf(value).intValue());
	}

	@Test
	public void testLongValue() {
		final long value = 2571982340096l;
		
		assertEquals(value, CSSNumber.valueOf(value).longValue());
	}

	@Test
	public void testFloatValue() {
		final float value = 2.529278345f;
		
		assertEquals(value, CSSNumber.valueOf(value).floatValue(), 0f);
	}

	@Test
	public void testDoubleValue() {
		final double value = 2.52927827835083;
		
		assertEquals(value, CSSNumber.valueOf((float) value).doubleValue(), 0.0);
	}

	@Test
	public void testByteValue() {
		final byte value = 126;
		
		assertEquals(value, CSSNumber.valueOf(value).byteValue());
	}

	@Test
	public void testShortValue() {
		final short value = 257;
		
		assertEquals(value, CSSNumber.valueOf(value).shortValue());
	}

	@Test
	public void testValueOf() {
		assertEquals(new CSSNumber(257f), CSSNumber.valueOf(257f));
	}

	@Test
	public void testCSSNumber() {
		@SuppressWarnings("unused")
		final CSSNumber number = new CSSNumber(257f);
	}

	@Test
	public void testPlusCSSNumber() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertEquals(CSSNumber.valueOf(12f), two.plus(ten));
	}

	@Test
	public void testPlusFloat() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertEquals(CSSNumber.valueOf(12f), two.plus(10f));
	}

	@Test
	public void testMinusCSSNumber() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertEquals(CSSNumber.valueOf(-8f), two.minus(ten));
	}

	@Test
	public void testMinusFloat() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertEquals(CSSNumber.valueOf(-8f), two.minus(10f));
	}

	@Test
	public void testTimesCSSNumber() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertEquals(CSSNumber.valueOf(20f), two.times(ten));
	}

	@Test
	public void testTimesFloat() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertEquals(CSSNumber.valueOf(20f), two.times(10f));
	}

	@Test
	public void testDividedByCSSNumber() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertEquals(CSSNumber.valueOf(0.2f), two.dividedBy(ten));
	}

	@Test
	public void testDividedByFloat() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertEquals(CSSNumber.valueOf(0.2f), two.dividedBy(10f));
	}

	@Test
	public void testCompareToLessThan() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertTrue(two.compareTo(ten) < 0);
	}

	@Test
	public void testCompareToGreaterThan() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		final CSSNumber ten = CSSNumber.valueOf(10f);
		
		assertTrue(ten.compareTo(two) > 0);
	}

	@Test
	public void testCompareToEqual() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertEquals(0, two.compareTo(two));
	}

	@Test
	public void testEquals() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertTrue(two.equals(two));
	}

	@Test
	public void testEqualsNot() {
		final CSSNumber two = CSSNumber.valueOf(2f);
		
		assertFalse(two.equals(CSSNumber.valueOf(2.5f)));
	}

	@Test
	public void testHashCode() {
		final float value = 2.529278345f;
		
		assertEquals(Float.valueOf(value).hashCode(), CSSNumber.valueOf(value).hashCode());
	}

	@Test
	public void testToString() {
		final float value = 1238.465f;
		
		assertEquals(Float.toString(value), CSSNumber.valueOf(value).toString());
	}

}
