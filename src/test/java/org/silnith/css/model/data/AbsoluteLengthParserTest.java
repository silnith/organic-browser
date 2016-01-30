package org.silnith.css.model.data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class AbsoluteLengthParserTest {
    
    private CSSNumberParser cssNumberParser;
    
    private AbsoluteLengthParser absoluteLengthParser;
    
    @Before
    public void setUp() {
        cssNumberParser = new CSSNumberParser();
        absoluteLengthParser = new AbsoluteLengthParser(cssNumberParser);
    }
    
    @Test
    public void testParseZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0");
        
        assertEquals(CSSNumber.ZERO, value.getLength());
    }
    
    @Test
    public void testParseOneInch() {
        final AbsoluteLength value = absoluteLengthParser.parse("1in");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.IN), value);
    }
    
    @Test
    public void testParseOneCentimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("1cm");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.CM), value);
    }
    
    @Test
    public void testParseOneMillimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("1mm");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.MM), value);
    }
    
    @Test
    public void testParseOnePoint() {
        final AbsoluteLength value = absoluteLengthParser.parse("1pt");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PT), value);
    }
    
    @Test
    public void testParseOnePica() {
        final AbsoluteLength value = absoluteLengthParser.parse("1pc");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PC), value);
    }
    
    @Test
    public void testParseOnePixel() {
        final AbsoluteLength value = absoluteLengthParser.parse("1px");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PX), value);
    }
    
    @Test
    public void testParseNegativeOneInch() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1in");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.IN), value);
    }
    
    @Test
    public void testParseNegativeOneCentimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1cm");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.CM), value);
    }
    
    @Test
    public void testParseNegativeOneMillimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1mm");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.MM), value);
    }
    
    @Test
    public void testParseNegativeOnePoint() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1pt");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.PT), value);
    }
    
    @Test
    public void testParseNegativeOnePica() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1pc");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.PC), value);
    }
    
    @Test
    public void testParseNegativeOnePixel() {
        final AbsoluteLength value = absoluteLengthParser.parse("-1px");
        
        assertEquals(new AbsoluteLength( -1, AbsoluteUnit.PX), value);
    }
    
    @Test
    public void testParsePositiveOneInch() {
        final AbsoluteLength value = absoluteLengthParser.parse("+1in");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.IN), value);
    }
    
    @Test
    public void testParsePositiveOneCentimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("+1cm");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.CM), value);
    }
    
    @Test
    public void testParsePositiveOneMillimeter() {
        final AbsoluteLength value = absoluteLengthParser.parse("+1mm");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.MM), value);
    }
    
    @Test
    public void testParsePositiveOnePoint() {
        final AbsoluteLength value = absoluteLengthParser.parse("1pt");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PT), value);
    }
    
    @Test
    public void testParsePositiveOnePica() {
        final AbsoluteLength value = absoluteLengthParser.parse("+1pc");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PC), value);
    }
    
    @Test
    public void testParsePositiveOnePixel() {
        final AbsoluteLength value = absoluteLengthParser.parse("1px");
        
        assertEquals(new AbsoluteLength(1, AbsoluteUnit.PX), value);
    }
    
    @Test
    public void testParseOneHalfInchNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5in");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.IN), value);
    }
    
    @Test
    public void testParseOneHalfCentimeterNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5cm");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.CM), value);
    }
    
    @Test
    public void testParseOneHalfMillimeterNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5mm");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.MM), value);
    }
    
    @Test
    public void testParseOneHalfPointNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5pt");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PT), value);
    }
    
    @Test
    public void testParseOneHalfPicaNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5pc");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PC), value);
    }
    
    @Test
    public void testParseOneHalfPixelNoLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse(".5px");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PX), value);
    }
    
    @Test
    public void testParseOneHalfInchWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5in");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.IN), value);
    }
    
    @Test
    public void testParseOneHalfCentimeterWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5cm");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.CM), value);
    }
    
    @Test
    public void testParseOneHalfMillimeterWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5mm");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.MM), value);
    }
    
    @Test
    public void testParseOneHalfPointWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5pt");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PT), value);
    }
    
    @Test
    public void testParseOneHalfPicaWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5pc");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PC), value);
    }
    
    @Test
    public void testParseOneHalfPixelWithLeadingZero() {
        final AbsoluteLength value = absoluteLengthParser.parse("0.5px");
        
        assertEquals(new AbsoluteLength(0.5f, AbsoluteUnit.PX), value);
    }
    
}
