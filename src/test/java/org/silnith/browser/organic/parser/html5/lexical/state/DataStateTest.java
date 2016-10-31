package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


public class DataStateTest {
    
    private Tokenizer tokenizer;
    
    private DataState dataState;
    
    @Test
    public void testGetNextTokensAmpersand() throws IOException {
        tokenizer = new Tokenizer(new StringReader("&"));
        tokenizer.setState(Tokenizer.State.DATA);
        tokenizer.setAllowParseErrors(false);
        dataState = new DataState(tokenizer);
        
        final List<Token> tokens = dataState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensLessThan() throws IOException {
        tokenizer = new Tokenizer(new StringReader("<"));
        tokenizer.setAllowParseErrors(false);
        dataState = new DataState(tokenizer);
        
        final List<Token> tokens = dataState.getNextTokens();
        
        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
        
        assertEquals(Tokenizer.State.TAG_OPEN, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensEndOfFile() throws IOException {
        tokenizer = new Tokenizer(new StringReader(""));
        tokenizer.setAllowParseErrors(false);
        dataState = new DataState(tokenizer);
        
        final List<Token> tokens = dataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.EOF, token.getType());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensCharacter() throws IOException {
        tokenizer = new Tokenizer(new StringReader("a"));
        tokenizer.setAllowParseErrors(false);
        dataState = new DataState(tokenizer);
        
        final List<Token> tokens = dataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('a', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokensNullCharacterAllowError() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\u0000"));
        tokenizer.setAllowParseErrors(true);
        dataState = new DataState(tokenizer);
        
        final List<Token> tokens = dataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u0000', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokensNullCharacter() throws IOException {
        tokenizer = new Tokenizer(new StringReader("\u0000"));
        tokenizer.setAllowParseErrors(false);
        dataState = new DataState(tokenizer);
        
        dataState.getNextTokens();
    }
    
}
