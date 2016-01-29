package org.silnith.css.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class RelativeLengthTest {

	@Test
	public void testGetType() {
		final RelativeLength onePointTwoEM = new RelativeLength(CSSNumber.valueOf(1.2f), RelativeUnit.EM);
		
		assertEquals(Length.Type.RELATIVE, onePointTwoEM.getType());
	}

	@Test
	public void testRelativeLengthCSSNumberRelativeUnit() {
		final RelativeLength onePointTwoEM = new RelativeLength(CSSNumber.valueOf(1.2f), RelativeUnit.EM);
		
		assertEquals(CSSNumber.valueOf(1.2f), onePointTwoEM.getLength());
		assertEquals(RelativeUnit.EM, onePointTwoEM.getUnit());
	}

	@Test
	public void testRelativeLengthFloatRelativeUnit() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertEquals(CSSNumber.valueOf(1.2f), onePointTwoEM.getLength());
		assertEquals(RelativeUnit.EM, onePointTwoEM.getUnit());
	}

	@Test
	public void testResolve() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		final AbsoluteLength tenPoint = new AbsoluteLength(10, AbsoluteUnit.PT);
		
		assertEquals(new AbsoluteLength(12, AbsoluteUnit.PT), onePointTwoEM.resolve(tenPoint));
	}

	@Test
	public void testHashCode() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertEquals(new RelativeLength(1.2f, RelativeUnit.EM).hashCode(), onePointTwoEM.hashCode());
	}

	@Test
	public void testEquals() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertEquals(new RelativeLength(1.2f, RelativeUnit.EM), onePointTwoEM);
	}

	@Test
	public void testEqualsNotLength() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertNotEquals(new RelativeLength(1.4f, RelativeUnit.EM), onePointTwoEM);
	}

	@Test
	public void testEqualsNotUnit() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertNotEquals(new RelativeLength(1.2f, RelativeUnit.EX), onePointTwoEM);
	}

	@Test
	public void testToString() {
		final RelativeLength onePointTwoEM = new RelativeLength(1.2f, RelativeUnit.EM);
		
		assertEquals("1.2em", onePointTwoEM.toString());
	}

}
