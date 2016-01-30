package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


public class CharacterReferenceInDataStateTest {
    
    private Tokenizer tokenizer;
    
    private CharacterReferenceInDataState characterReferenceInDataState;
    
    @Test
    public void testGetNextTokenAmp() throws IOException {
        tokenizer = new Tokenizer(new StringReader("amp;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('&', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenLessThan() throws IOException {
        tokenizer = new Tokenizer(new StringReader("lt;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('<', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenLessThanDoubleQuote() throws IOException {
        tokenizer = new Tokenizer(new StringReader("ldquo;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u201C', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenMathFrakturCapitalA() throws IOException {
        tokenizer = new Tokenizer(new StringReader("Afr;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        final char[] expectedChars = Character.toChars(0x1D504);
        
        assertNotNull(tokens);
        assertEquals(2, tokens.size());
        final Token firstToken = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, firstToken.getType());
        final CharacterToken firstCharacterToken = (CharacterToken) firstToken;
        assertEquals(expectedChars[0], firstCharacterToken.getCharacter());
        final Token secondToken = tokens.get(1);
        assertEquals(Token.Type.CHARACTER, secondToken.getType());
        final CharacterToken secondCharacterToken = (CharacterToken) secondToken;
        assertEquals(expectedChars[1], secondCharacterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenEmpty() throws IOException {
        tokenizer = new Tokenizer(new StringReader(";"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('&', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals(';', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenNumericEmpty() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenNumericEmptyLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('&', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals('#', tokenizer.consume());
        assertEquals(';', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenNumeric() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#97;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('a', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenNumericUnclosed() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#97 "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenNumericUnclosedLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#97 "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('a', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenHexadecimalEmpty() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#x;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenHexadecimalEmptyLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#x;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('&', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals('#', tokenizer.consume());
        assertEquals('x', tokenizer.consume());
        assertEquals(';', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenHexadecimalLowercase() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#x201c;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u201C', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenHexadecimalLowercaseUnclosed() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#x201c "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenHexadecimalLowercaseUnclosedLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#x201c "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u201C', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test
    public void testGetNextTokenHexadecimalUppercase() throws IOException {
        tokenizer = new Tokenizer(new StringReader("#X201C;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u201C', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenFake() throws IOException {
        tokenizer = new Tokenizer(new StringReader("fake;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenFakeLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("fake;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('&', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals('f', tokenizer.consume());
        assertEquals('a', tokenizer.consume());
        assertEquals('k', tokenizer.consume());
        assertEquals('e', tokenizer.consume());
        assertEquals(';', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenNotAMatch() throws IOException {
        tokenizer = new Tokenizer(new StringReader("notit;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenNotAMatchLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("notit;"));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u00AC', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals('i', tokenizer.consume());
        assertEquals('t', tokenizer.consume());
        assertEquals(';', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
    @Test(expected = ParseErrorException.class)
    public void testGetNextTokenNotAMatchUnclosed() throws IOException {
        tokenizer = new Tokenizer(new StringReader("notit "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(false);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        characterReferenceInDataState.getNextTokens();
    }
    
    @Test
    public void testGetNextTokenNotAMatchUnclosedLenient() throws IOException {
        tokenizer = new Tokenizer(new StringReader("notit "));
        tokenizer.setState(Tokenizer.State.CHARACTER_REFERENCE_IN_DATA);
        tokenizer.setAllowParseErrors(true);
        characterReferenceInDataState = new CharacterReferenceInDataState(tokenizer);
        
        final List<Token> tokens = characterReferenceInDataState.getNextTokens();
        
        assertNotNull(tokens);
        assertEquals(1, tokens.size());
        final Token token = tokens.get(0);
        assertEquals(Token.Type.CHARACTER, token.getType());
        final CharacterToken characterToken = (CharacterToken) token;
        assertEquals('\u00AC', characterToken.getCharacter());
        
        assertEquals(Tokenizer.State.DATA, tokenizer.getState());
        assertEquals('i', tokenizer.consume());
        assertEquals('t', tokenizer.consume());
        assertEquals(' ', tokenizer.consume());
        assertEquals( -1, tokenizer.consume());
    }
    
}
