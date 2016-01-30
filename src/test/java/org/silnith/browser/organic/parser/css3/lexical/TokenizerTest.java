package org.silnith.browser.organic.parser.css3.lexical;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;


public class TokenizerTest {
    
    private Tokenizer tokenizer;
    
    @Test
    public void testConsumeCommentsEmptyCommentSpace() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/**/ "));
        
        tokenizer.consumeComments();
        
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsSpaceEmptyCommentSpace() throws IOException {
        tokenizer = new Tokenizer(new StringReader(" /**/"));
        
        tokenizer.consumeComments();
        
        assertEquals(' ', tokenizer.consume());
        assertEquals('/', tokenizer.consume());
        assertEquals('*', tokenizer.consume());
        assertEquals('*', tokenizer.consume());
        assertEquals('/', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsEmptyComment() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/**/"));
        
        tokenizer.consumeComments();
        
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsTextFilledComment() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/* This is a test. */"));
        
        tokenizer.consumeComments();
        
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsTwoEmptyCommentsSpace() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/**//**/ "));
        
        tokenizer.consumeComments();
        
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsTwoTextFilledComments() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/* This is a test. *//* This is a test. */"));
        
        tokenizer.consumeComments();
        
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testConsumeCommentsTwoSeparatedCommentsSpace() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/**/ /**/ "));
        
        tokenizer.consumeComments();
        
        assertEquals(' ', tokenizer.consume());
        assertEquals('/', tokenizer.consume());
        assertEquals('*', tokenizer.consume());
        assertEquals('*', tokenizer.consume());
        assertEquals('/', tokenizer.consume());
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    private static final double TOLERANCE = 1.0f / (1 << 12);
    
    @Test
    public void testConvertStringToNumberZeroInteger() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("0");
        
        assertEquals(0, number.longValue());
        assertEquals(0.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberZeroFloat() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("0.0");
        
        assertEquals(0, number.longValue());
        assertEquals(0.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberOneInteger() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("1");
        
        assertEquals(1, number.longValue());
        assertEquals(1.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberOneFloat() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("1.0");
        
        assertEquals(1, number.longValue());
        assertEquals(1.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberNegativeOneInteger() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("-1");
        
        assertEquals( -1, number.longValue());
        assertEquals( -1.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberNegativeOneFloat() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("-1.0");
        
        assertEquals( -1, number.longValue());
        assertEquals( -1.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberOneExponent() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("1e2");
        
        assertEquals(100, number.longValue());
        assertEquals(100.0, number.doubleValue(), TOLERANCE);
    }
    
    @Test
    public void testConvertStringToNumberBigComplicatedEverything() {
        tokenizer = new Tokenizer(new StringReader(""));
        
        final Number number = tokenizer.convertStringToNumber("+1234.2525e+14");
        
        assertEquals(123425250000000000L, number.longValue());
        assertEquals(1234.2525e+14, number.doubleValue(), TOLERANCE);
    }
    
}
