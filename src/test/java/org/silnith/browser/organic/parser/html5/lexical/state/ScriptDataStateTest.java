package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndOfFileToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


public class ScriptDataStateTest {
    
    private Tokenizer tokenizer;
    
    private ScriptDataState scriptDataState;
    
    @Test
    public void testGetNextTokensLessThanSign() throws IOException {
        tokenizer = new Tokenizer(new StringReader("<b"));
        tokenizer.setState(Tokenizer.State.SCRIPT_DATA);
        tokenizer.setAllowParseErrors(true);
        scriptDataState = new ScriptDataState(tokenizer);
        
        final List<Token> nextTokens = scriptDataState.getNextTokens();
        
        assertNotNull(nextTokens);
        assertTrue(nextTokens.isEmpty());
        
        assertEquals(Tokenizer.State.SCRIPT_DATA_LESS_THAN_SIGN, tokenizer.getState());
        assertEquals('b', tokenizer.consume());
        assertEquals(-1, tokenizer.consume());
    }

    @Test
    public void testGetNextTokensEOF() throws IOException {
        tokenizer = new Tokenizer(new StringReader(""));
        tokenizer.setState(Tokenizer.State.SCRIPT_DATA);
        tokenizer.setAllowParseErrors(true);
        scriptDataState = new ScriptDataState(tokenizer);
        
        final List<Token> nextTokens = scriptDataState.getNextTokens();
        
        assertNotNull(nextTokens);
        assertEquals(1, nextTokens.size());
        final Token token = nextTokens.get(0);
        assertEquals(Token.Type.EOF, token.getType());
        
        assertEquals(Tokenizer.State.SCRIPT_DATA, tokenizer.getState());
        assertEquals(-1, tokenizer.consume());
    }

    @Test
    public void testGetNextTokensCharacter() throws IOException {
        tokenizer = new Tokenizer(new StringReader("ab"));
        tokenizer.setState(Tokenizer.State.SCRIPT_DATA);
        tokenizer.setAllowParseErrors(true);
        scriptDataState = new ScriptDataState(tokenizer);
        
        final List<Token> nextTokens = scriptDataState.getNextTokens();
        
        assertNotNull(nextTokens);
        assertEquals(1, nextTokens.size());
        final Token token = nextTokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('a', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.SCRIPT_DATA, tokenizer.getState());
        assertEquals('b', tokenizer.consume());
        assertEquals(-1, tokenizer.consume());
    }
    
}
