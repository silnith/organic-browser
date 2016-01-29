package org.silnith.css.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class PercentageLengthTest {

	@Test
	public void testGetType() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals(Length.Type.PERCENTAGE, fifteenPercent.getType());
	}

	@Test
	public void testPercentageLengthCSSNumber() {
		final PercentageLength fifteenPercent = new PercentageLength(CSSNumber.valueOf(15));
		
		assertEquals(CSSNumber.valueOf(15), fifteenPercent.getLength());
		assertEquals(PercentageUnit.PERCENTAGE, fifteenPercent.getUnit());
	}

	@Test
	public void testPercentageLengthFloat() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals(CSSNumber.valueOf(15), fifteenPercent.getLength());
		assertEquals(PercentageUnit.PERCENTAGE, fifteenPercent.getUnit());
	}

	@Test
	public void testResolve() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		final AbsoluteLength oneHundredMillimeters = new AbsoluteLength(100, AbsoluteUnit.MM);
		
		assertEquals(new AbsoluteLength(15, AbsoluteUnit.MM), fifteenPercent.resolve(oneHundredMillimeters));
	}

	@Test
	public void testCompareToLessThan() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		final PercentageLength fourteenPercent = new PercentageLength(14);
		
		assertTrue(fourteenPercent.compareTo(fifteenPercent) < 0);
	}

	@Test
	public void testCompareToGreaterThan() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		final PercentageLength fourteenPercent = new PercentageLength(14);
		
		assertTrue(fifteenPercent.compareTo(fourteenPercent) > 0);
	}

	@Test
	public void testCompareToEqual() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals(0, fifteenPercent.compareTo(fifteenPercent));
	}

	@Test
	public void testHashCode() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals(new PercentageLength(15).hashCode(), fifteenPercent.hashCode());
	}

	@Test
	public void testEqualsObject() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals(new PercentageLength(15), fifteenPercent);
	}

	@Test
	public void testToString() {
		final PercentageLength fifteenPercent = new PercentageLength(15);
		
		assertEquals("15.0%", fifteenPercent.toString());
	}

}
