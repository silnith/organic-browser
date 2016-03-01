package org.silnith.browser.organic.parser.css3.lexical;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Ignore;
import org.junit.Test;
import org.silnith.browser.organic.parser.css3.lexical.token.FunctionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.HashToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumericValueToken;
import org.silnith.browser.organic.parser.css3.lexical.token.StringToken;
import org.silnith.browser.organic.parser.css3.lexical.token.TypedNumericValueToken;
import org.silnith.browser.organic.parser.css3.lexical.token.URLToken;
import org.silnith.browser.organic.parser.css3.lexical.token.UnicodeRangeToken;


public class TokenizerTest {
    
    private Tokenizer tokenizer;
    
//    @Test
//    public void testConsumeCommentsEmptyCommentSpace() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/**/ "));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals(' ', tokenizer.consume());
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsSpaceEmptyCommentSpace() throws IOException {
//        tokenizer = new Tokenizer(new StringReader(" /**/"));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals(' ', tokenizer.consume());
//        assertEquals('/', tokenizer.consume());
//        assertEquals('*', tokenizer.consume());
//        assertEquals('*', tokenizer.consume());
//        assertEquals('/', tokenizer.consume());
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsEmptyComment() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/**/"));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsTextFilledComment() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/* This is a test. */"));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsTwoEmptyCommentsSpace() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/**//**/ "));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals(' ', tokenizer.consume());
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsTwoTextFilledComments() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/* This is a test. *//* This is a test. */"));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals( -1, tokenizer.consume());
//    }
//    
//    @Test
//    public void testConsumeCommentsTwoSeparatedCommentsSpace() throws IOException {
//        tokenizer = new Tokenizer(new StringReader("/**/ /**/ "));
//        
//        tokenizer.consumeComments();
//        
//        assertEquals(' ', tokenizer.consume());
//        assertEquals('/', tokenizer.consume());
//        assertEquals('*', tokenizer.consume());
//        assertEquals('*', tokenizer.consume());
//        assertEquals('/', tokenizer.consume());
//        assertEquals(' ', tokenizer.consume());
//        assertEquals( -1, tokenizer.consume());
//    }
    
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
    
    @Test
    public void testPrime() throws IOException {
        tokenizer = new Tokenizer(new StringReader("foo { bar: baz; }"));
        tokenizer.prime();
    }
    
    @Test
    public void testConsumeToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("foo { bar: baz; }"));
        tokenizer.prime();
        
        tokenizer.consumeToken();
    }
    
    @Test
    public void testConsumeToken_HashToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#abcdef "));
        tokenizer.prime();
        
        final LexicalToken token = tokenizer.consumeToken();
        
        assertEquals(LexicalToken.LexicalType.HASH_TOKEN, token.getLexicalType());
        final HashToken hashToken = (HashToken) token;
        assertEquals("abcdef", hashToken.getStringValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumericToken_IntegerPercentage() throws IOException {
        tokenizer = new Tokenizer(new StringReader("125% "));
        tokenizer.prime();
        
        final NumericValueToken numericToken = tokenizer.consumeNumericToken();
        
        assertEquals(LexicalToken.LexicalType.PERCENTAGE_TOKEN, numericToken.getLexicalType());
        assertEquals("125", numericToken.getStringValue());
        assertEquals(125d, numericToken.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeIdentLikeToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("foo "));
        tokenizer.prime();
        
        final LexicalToken identLikeToken = tokenizer.consumeIdentLikeToken();
        
        assertEquals(LexicalToken.LexicalType.IDENT_TOKEN, identLikeToken.getLexicalType());
        final IdentToken identToken = (IdentToken) identLikeToken;
        assertEquals("foo", identToken.getStringValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeIdentLikeToken_Function() throws IOException {
        tokenizer = new Tokenizer(new StringReader("foo(12) "));
        tokenizer.prime();
        
        final LexicalToken identLikeToken = tokenizer.consumeIdentLikeToken();
        
        assertEquals(LexicalToken.LexicalType.FUNCTION_TOKEN, identLikeToken.getLexicalType());
        final FunctionToken functionToken = (FunctionToken) identLikeToken;
        assertEquals("foo", functionToken.getStringValue());
        assertEquals('1', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeIdentLikeToken_URL() throws IOException {
        tokenizer = new Tokenizer(new StringReader("url(http://example.com/) "));
        tokenizer.prime();
        
        final LexicalToken identLikeToken = tokenizer.consumeIdentLikeToken();
        
        assertEquals(LexicalToken.LexicalType.URL_TOKEN, identLikeToken.getLexicalType());
        final URLToken urlToken = (URLToken) identLikeToken;
        assertEquals("http://example.com/", urlToken.getStringValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeStringToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\"foo bar baz\" "));
        tokenizer.prime();
        
        // This is solely so the assertion passes.
        tokenizer.consume();
        final LexicalToken token = tokenizer.consumeStringToken('"');
        
        assertEquals(LexicalToken.LexicalType.STRING_TOKEN, token.getLexicalType());
        final StringToken stringToken = (StringToken) token;
        assertEquals("foo bar baz", stringToken.getStringValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeURLToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("url(http://example.com/) "));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        tokenizer.consume();
        tokenizer.consume();
        tokenizer.consume();
        final LexicalToken token = tokenizer.consumeURLToken();
        
        assertEquals(LexicalToken.LexicalType.URL_TOKEN, token.getLexicalType());
        final URLToken urlToken = (URLToken) token;
        assertEquals("http://example.com/", urlToken.getStringValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeUnicodeRangeToken() throws IOException {
        tokenizer = new Tokenizer(new StringReader("u+0040 "));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        tokenizer.consume();
        final LexicalToken token = tokenizer.consumeUnicodeRangeToken();
        
        assertEquals(LexicalToken.LexicalType.UNICODE_RANGE_TOKEN, token.getLexicalType());
        final UnicodeRangeToken unicodeRangeToken = (UnicodeRangeToken) token;
        assertEquals(0x40, unicodeRangeToken.getStart());
        assertEquals(0x40, unicodeRangeToken.getEnd());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeEscapedCodePoint() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\\2018 foo"));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        final int escapedCodePoint = tokenizer.consumeEscapedCodePoint();
        
        assertEquals('\u2018', escapedCodePoint);
        assertEquals('f', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testIsValidEscape() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\\2018"));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        final boolean validEscape = tokenizer.isValidEscape();
        
        assertTrue(validEscape);
    }
    
    @Test
    public void testIsValidEscape_IntInt() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\\2018"));
        tokenizer.prime();

        final boolean validEscape = tokenizer.isValidEscape('\\', 'F');
        
        assertTrue(validEscape);
    }
    
    @Test
    public void testWouldStartIdentifier() throws IOException {
        tokenizer = new Tokenizer(new StringReader("abc"));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        final boolean startIdentifier = tokenizer.wouldStartIdentifier();
        
        assertTrue(startIdentifier);
    }
    
    @Test
    public void testWouldStartIdentifier_IntIntInt() throws IOException {
        tokenizer = new Tokenizer(new StringReader("abc"));
        tokenizer.prime();

        final boolean startIdentifier = tokenizer.wouldStartIdentifier('a', 'b', 'c');
        
        assertTrue(startIdentifier);
    }
    
    @Test
    public void testWouldStartNumber() throws IOException {
        tokenizer = new Tokenizer(new StringReader("123"));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        final boolean startNumber = tokenizer.wouldStartNumber();
        
        assertTrue(startNumber);
    }
    
    @Test
    public void testWouldStartNumber_IntIntInt() throws IOException {
        tokenizer = new Tokenizer(new StringReader("123"));
        tokenizer.prime();

        // This is solely so the assertion passes.
        tokenizer.consume();
        final boolean startNumber = tokenizer.wouldStartNumber('1', '2', '3');
        
        assertTrue(startNumber);
    }
    
    @Test
    public void testConsumeName() throws IOException {
        tokenizer = new Tokenizer(new StringReader("foo "));
        tokenizer.prime();
        
        final String name = tokenizer.consumeName();
        
        assertEquals("foo", name);
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_Zero() throws IOException {
        tokenizer = new Tokenizer(new StringReader("0 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("0", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(0), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveZero() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+0 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+0", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(0), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    @Ignore
    public void testConsumeNumber_NegativeZero() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-0 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-0", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(0), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PointZero() throws IOException {
        tokenizer = new Tokenizer(new StringReader(".0 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals(".0", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(0), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_Fraction() throws IOException {
        tokenizer = new Tokenizer(new StringReader(".125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals(".125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(0.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveFraction() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+.125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+.125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(0.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeFraction() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-.125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-.125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(-0.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_Integer() throws IOException {
        tokenizer = new Tokenizer(new StringReader("278 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("278", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(278), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveInteger() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+278 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+278", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(278), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeInteger() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-278 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-278", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.INTEGER, number.getNumericType());
        assertEquals(Double.valueOf(-278), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_Number() throws IOException {
        tokenizer = new Tokenizer(new StringReader("365.125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("365.125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(365.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveNumber() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+365.125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+365.125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(365.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeNumber() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-365.125 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-365.125", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(-365.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NumberExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("365.125e2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("365.125e2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveNumberExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+365.125e2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+365.125e2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeNumberExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-365.125e2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-365.125e2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(-36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NumberPositiveExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("365.125e+2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("365.125e+2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveNumberPositiveExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+365.125e+2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+365.125e+2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeNumberPositiveExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-365.125e+2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-365.125e+2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(-36512.5), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NumberNegativeExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("312.5e-2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("312.5e-2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(3.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_PositiveNumberNegativeExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("+312.5e-2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("+312.5e-2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(+3.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
    @Test
    public void testConsumeNumber_NegativeNumberNegativeExponent() throws IOException {
        tokenizer = new Tokenizer(new StringReader("-312.5e-2 "));
        tokenizer.prime();
        
        final NumberToken number = tokenizer.consumeNumber();
        
        assertEquals(LexicalToken.LexicalType.NUMBER_TOKEN, number.getLexicalType());
        assertEquals("-312.5e-2", number.getStringValue());
        assertEquals(TypedNumericValueToken.NumericType.NUMBER, number.getNumericType());
        assertEquals(Double.valueOf(-3.125), number.getNumericValue());
        assertEquals(' ', tokenizer.getNextInputCodePoint());
    }
    
}
