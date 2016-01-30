package org.silnith.css.model.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * These tests assume 96dpi.
 */
public class AbsoluteLengthTest {
    
    private static final double DELTA = 1.0 / 1024.0;
    
    @Test
    public void testGetType() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(Length.Type.ABSOLUTE, twelvePoint.getType());
    }
    
    @Test
    public void testCompareToLessThan() {
        final AbsoluteLength elevenPoint = new AbsoluteLength(11, AbsoluteUnit.PT);
        final AbsoluteLength onePica = new AbsoluteLength(1, AbsoluteUnit.PC);
        
        assertTrue(elevenPoint.compareTo(onePica) < 0);
    }
    
    @Test
    public void testCompareToGreaterThan() {
        final AbsoluteLength elevenPoint = new AbsoluteLength(11, AbsoluteUnit.PT);
        final AbsoluteLength onePica = new AbsoluteLength(1, AbsoluteUnit.PC);
        
        assertTrue(onePica.compareTo(elevenPoint) > 0);
    }
    
    @Test
    public void testCompareToEqual() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        final AbsoluteLength onePica = new AbsoluteLength(1, AbsoluteUnit.PC);
        
        assertEquals(0, onePica.compareTo(twelvePoint));
    }
    
    @Test
    public void testEquals() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(12, AbsoluteUnit.PT), twelvePoint);
    }
    
    @Test
    public void testEqualsNotLength() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertNotEquals(new AbsoluteLength(11, AbsoluteUnit.PT), twelvePoint);
    }
    
    @Test
    public void testEqualsNotUnit() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertNotEquals(new AbsoluteLength(12, AbsoluteUnit.PC), twelvePoint);
    }
    
    @Test
    public void testHashCode() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(12, AbsoluteUnit.PT).hashCode(), twelvePoint.hashCode());
    }
    
    @Test
    public void testToString() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals("12.0pt", twelvePoint.toString());
    }
    
    @Test
    public void testPlus() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(24, AbsoluteUnit.PT), twelvePoint.plus(twelvePoint));
    }
    
    @Test
    public void testMinus() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(AbsoluteLength.getZero(AbsoluteUnit.PT), twelvePoint.minus(twelvePoint));
    }
    
    @Test
    public void testTimesCSSNumber() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(24, AbsoluteUnit.PT), twelvePoint.times(CSSNumber.valueOf(2)));
    }
    
    @Test
    public void testTimesFloat() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(24, AbsoluteUnit.PT), twelvePoint.times(2));
    }
    
    @Test
    public void testDividedByCSSNumber() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(6, AbsoluteUnit.PT), twelvePoint.dividedBy(CSSNumber.valueOf(2)));
    }
    
    @Test
    public void testDividedByFloat() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12, AbsoluteUnit.PT);
        
        assertEquals(new AbsoluteLength(6, AbsoluteUnit.PT), twelvePoint.dividedBy(2));
    }
    
    @Test
    public void testInchConvertToInch() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testInchConvertToCentimeter() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.CM);
        
        assertEquals(2.54f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testInchConvertToMillimeter() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.MM);
        
        assertEquals(25.4f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testInchConvertToPoint() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.PT);
        
        assertEquals(72f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testInchConvertToPica() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.PC);
        
        assertEquals(6f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testInchConvertToPixel() {
        final AbsoluteLength oneInch = new AbsoluteLength(1f, AbsoluteUnit.IN);
        
        final Length<AbsoluteUnit> actual = oneInch.convertTo(AbsoluteUnit.PX);
        
        assertEquals(96f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToInch() {
        final AbsoluteLength twoAndAHalfCentimeter = new AbsoluteLength(2.54f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = twoAndAHalfCentimeter.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToCentimeter() {
        final AbsoluteLength oneCentimeter = new AbsoluteLength(1f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = oneCentimeter.convertTo(AbsoluteUnit.CM);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToMillimeter() {
        final AbsoluteLength oneCentimeter = new AbsoluteLength(1f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = oneCentimeter.convertTo(AbsoluteUnit.MM);
        
        assertEquals(10f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToPoint() {
        final AbsoluteLength twoPointFiveFourCentimeter = new AbsoluteLength(2.54f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = twoPointFiveFourCentimeter.convertTo(AbsoluteUnit.PT);
        
        assertEquals(72, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToPica() {
        final AbsoluteLength twoPointFiveFourCentimeter = new AbsoluteLength(2.54f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = twoPointFiveFourCentimeter.convertTo(AbsoluteUnit.PC);
        
        assertEquals(6f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testCentimeterConvertToPixel() {
        final AbsoluteLength twoPointFiveFourCentimeter = new AbsoluteLength(2.54f, AbsoluteUnit.CM);
        
        final Length<AbsoluteUnit> actual = twoPointFiveFourCentimeter.convertTo(AbsoluteUnit.PX);
        
        assertEquals(96f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToInch() {
        final AbsoluteLength twentyFivePointFourMillimeter = new AbsoluteLength(25.4f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = twentyFivePointFourMillimeter.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToCentimeter() {
        final AbsoluteLength tenMillimeter = new AbsoluteLength(10f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = tenMillimeter.convertTo(AbsoluteUnit.CM);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToMillimeter() {
        final AbsoluteLength oneMillimeter = new AbsoluteLength(1f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = oneMillimeter.convertTo(AbsoluteUnit.MM);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToPoint() {
        final AbsoluteLength twentyFivePointFourMillimeter = new AbsoluteLength(25.4f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = twentyFivePointFourMillimeter.convertTo(AbsoluteUnit.PT);
        
        assertEquals(72f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToPica() {
        final AbsoluteLength twentyFivePointFourMillimeter = new AbsoluteLength(25.4f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = twentyFivePointFourMillimeter.convertTo(AbsoluteUnit.PC);
        
        assertEquals(6f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testMillimeterConvertToPixel() {
        final AbsoluteLength twentyFivePointFourMillimeter = new AbsoluteLength(25.4f, AbsoluteUnit.MM);
        
        final Length<AbsoluteUnit> actual = twentyFivePointFourMillimeter.convertTo(AbsoluteUnit.PX);
        
        assertEquals(96f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToInch() {
        final AbsoluteLength seventyTwoPoint = new AbsoluteLength(72f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = seventyTwoPoint.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToCentimeter() {
        final AbsoluteLength seventyTwoPoint = new AbsoluteLength(72f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = seventyTwoPoint.convertTo(AbsoluteUnit.CM);
        
        assertEquals(2.54f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToMillimeter() {
        final AbsoluteLength seventyTwoPoint = new AbsoluteLength(72f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = seventyTwoPoint.convertTo(AbsoluteUnit.MM);
        
        assertEquals(25.4f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToPoint() {
        final AbsoluteLength onePoint = new AbsoluteLength(1f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = onePoint.convertTo(AbsoluteUnit.PT);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToPica() {
        final AbsoluteLength twelvePoint = new AbsoluteLength(12f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = twelvePoint.convertTo(AbsoluteUnit.PC);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testPointConvertToPixel() {
        final AbsoluteLength threePoint = new AbsoluteLength(3f, AbsoluteUnit.PT);
        
        final Length<AbsoluteUnit> actual = threePoint.convertTo(AbsoluteUnit.PX);
        
        assertEquals(4f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToInch() {
        final AbsoluteLength sixPica = new AbsoluteLength(6f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = sixPica.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToCentimeter() {
        final AbsoluteLength sixPica = new AbsoluteLength(6f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = sixPica.convertTo(AbsoluteUnit.CM);
        
        assertEquals(2.54f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToMillimeter() {
        final AbsoluteLength sixPica = new AbsoluteLength(6f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = sixPica.convertTo(AbsoluteUnit.MM);
        
        assertEquals(25.4f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToPoint() {
        final AbsoluteLength onePica = new AbsoluteLength(1f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = onePica.convertTo(AbsoluteUnit.PT);
        
        assertEquals(12f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToPica() {
        final AbsoluteLength onePica = new AbsoluteLength(1f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = onePica.convertTo(AbsoluteUnit.PC);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testPicaConvertToPixel() {
        final AbsoluteLength onePica = new AbsoluteLength(1f, AbsoluteUnit.PC);
        
        final Length<AbsoluteUnit> actual = onePica.convertTo(AbsoluteUnit.PX);
        
        assertEquals(16f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToInch() {
        final AbsoluteLength ninetySixPixel = new AbsoluteLength(96f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = ninetySixPixel.convertTo(AbsoluteUnit.IN);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.IN, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToCentimeter() {
        final AbsoluteLength ninetySixPixel = new AbsoluteLength(96f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = ninetySixPixel.convertTo(AbsoluteUnit.CM);
        
        assertEquals(2.54f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.CM, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToMillimeter() {
        final AbsoluteLength ninetySixPixel = new AbsoluteLength(96f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = ninetySixPixel.convertTo(AbsoluteUnit.MM);
        
        assertEquals(25.4f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.MM, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToPoint() {
        final AbsoluteLength fourPixel = new AbsoluteLength(4f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = fourPixel.convertTo(AbsoluteUnit.PT);
        
        assertEquals(3f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PT, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToPica() {
        final AbsoluteLength sixteenPixel = new AbsoluteLength(16f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = sixteenPixel.convertTo(AbsoluteUnit.PC);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PC, actual.getUnit());
    }
    
    @Test
    public void testPixelConvertToPixel() {
        final AbsoluteLength onePixel = new AbsoluteLength(1f, AbsoluteUnit.PX);
        
        final Length<AbsoluteUnit> actual = onePixel.convertTo(AbsoluteUnit.PX);
        
        assertEquals(1f, actual.getLength().floatValue(), DELTA);
        assertEquals(AbsoluteUnit.PX, actual.getUnit());
    }
    
}
