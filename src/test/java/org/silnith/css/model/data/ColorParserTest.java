package org.silnith.css.model.data;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;


public class ColorParserTest {
    
    private final ColorParser colorParser = new ColorParser();
    
    @Test
    public void testParseWhiteFullHexLowercase() {
        assertEquals(Color.WHITE, colorParser.parse("#ffffff"));
    }
    
    @Test
    public void testParseWhiteFullHexUppercase() {
        assertEquals(Color.WHITE, colorParser.parse("#FFFFFF"));
    }
    
    @Test
    public void testParseWhiteShortenedHexLowercase() {
        assertEquals(Color.WHITE, colorParser.parse("#fff"));
    }
    
    @Test
    public void testParseWhiteShortenedHexUppercase() {
        assertEquals(Color.WHITE, colorParser.parse("#FFF"));
    }
    
    @Test
    public void testParseWhiteRGBConstantNoWhitespace() {
        assertEquals(Color.WHITE, colorParser.parse("rgb(255,255,255)"));
    }
    
    @Test
    public void testParseWhiteRGBConstantFullWhitespace() {
        assertEquals(Color.WHITE, colorParser.parse("rgb(   255   ,   255   ,   255   )"));
    }
    
    @Test
    public void testParseWhiteRGBPercentNoWhitespace() {
        assertEquals(Color.WHITE, colorParser.parse("rgb(100%,100%,100%)"));
    }
    
    @Test
    public void testParseWhiteRGBPercentFullWhitespace() {
        assertEquals(Color.WHITE, colorParser.parse("rgb(   100%   ,   100%   ,   100%   )"));
    }
    
    @Test
    public void testParseBlackKeyword() {
        assertEquals(Color.BLACK, colorParser.parse("black"));
    }
    
}
