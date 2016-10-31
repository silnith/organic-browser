package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


public class AfterAttributeNameStateTest {
    
    private Tokenizer tokenizer;
    
    private AfterAttributeNameState afterAttributeNameState;
    
    @Test
    public void testGetNextTokensTab() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\t"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.AFTER_ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensLineFeed() throws IOException {
        tokenizer = new Tokenizer(new StringReader(String.valueOf("\n")));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.AFTER_ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensFormFeed() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\f"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.AFTER_ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensSpace() throws IOException {
        tokenizer = new Tokenizer(new StringReader(" "));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.AFTER_ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensSolidus() throws IOException {
        tokenizer = new Tokenizer(new StringReader("/"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.SELF_CLOSING_START_TAG, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensEqualsSign() throws IOException {
        tokenizer = new Tokenizer(new StringReader("="));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.BEFORE_ATTRIBUTE_VALUE, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensGreaterThanSign() throws IOException {
        tokenizer = new Tokenizer(new StringReader(">"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        assertSame(pendingTagToken, tokens.get(0));
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensUppercaseASCII() throws IOException {
        tokenizer = new Tokenizer(new StringReader("M"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("m", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensNull() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\u0000"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        afterAttributeNameState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokensNullLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\u0000"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(true);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("\uFFFD", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensQuotationMark() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\""));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        afterAttributeNameState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokensQuotationMarkLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\""));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(true);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("\"", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensApostrophe() throws IOException {
        tokenizer = new Tokenizer(new StringReader("'"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        afterAttributeNameState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokensApostropheLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("'"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(true);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("'", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensLessThanSign() throws IOException {
        tokenizer = new Tokenizer(new StringReader("<"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        afterAttributeNameState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokensLessThanSignLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("<"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(true);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("<", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensEOF() throws IOException {
        tokenizer = new Tokenizer(new StringReader(""));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        afterAttributeNameState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokensEOFLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader(""));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(true);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensLowercaseASCII() throws IOException {
        tokenizer = new Tokenizer(new StringReader("m"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("m", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensOther() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\u014D"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals("\u014D", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensColon() throws IOException {
        tokenizer = new Tokenizer(new StringReader(":"));
        tokenizer.setState(Tokenizer.State.AFTER_ATTRIBUTE_NAME);
        tokenizer.setAllowParseErrors(false);
        afterAttributeNameState = new AfterAttributeNameState(tokenizer);
        
        final TagToken pendingTagToken = new StartTagToken();
        tokenizer.setPendingToken(pendingTagToken);
        
        final List<Token> tokens = afterAttributeNameState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(":", pendingTagToken.getCurrentAttribute().getName());
        
        assertEquals(Tokenizer.State.ATTRIBUTE_NAME, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
}
