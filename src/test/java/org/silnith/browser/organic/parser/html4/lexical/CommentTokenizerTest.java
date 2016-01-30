package org.silnith.browser.organic.parser.html4.lexical;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import org.junit.Test;


public class CommentTokenizerTest {
    
    private final CommentTokenizer tokenizer = new CommentTokenizer();
    
    @Test
    public void testGetNextTokenDashSpace() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("- "), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.TEXT, token.getType());
        assertEquals("-", token.getContent());
        
        assertEquals(' ', reader.read());
    }
    
    @Test
    public void testGetNextTokenCommentDelimiterSpace() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("-- "), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.COMMENT_DELIMITER, token.getType());
        assertEquals("--", token.getContent());
        
        assertEquals(' ', reader.read());
    }
    
    @Test
    public void testGetNextTokenCommentDelimiterGreaterThan() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("-->"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.COMMENT_DELIMITER, token.getType());
        assertEquals("--", token.getContent());
        
        assertEquals('>', reader.read());
    }
    
    @Test
    public void testGetNextTokenSpaceDash() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader(" -"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.WHITESPACE, token.getType());
        assertEquals(" ", token.getContent());
        
        assertEquals('-', reader.read());
    }
    
    @Test
    public void testGetNextTokenSpacesDash() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("   -"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.WHITESPACE, token.getType());
        assertEquals("   ", token.getContent());
        
        assertEquals('-', reader.read());
    }
    
    @Test
    public void testGetNextTokenWhitespacesDash() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader(" \n \t\r -"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.WHITESPACE, token.getType());
        assertEquals(" \n \t\r ", token.getContent());
        
        assertEquals('-', reader.read());
    }
    
    @Test
    public void testGetNextTokenWhitespacesCommentDelimiter() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader(" \n \t\r --"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.WHITESPACE, token.getType());
        assertEquals(" \n \t\r ", token.getContent());
        
        assertEquals('-', reader.read());
        assertEquals('-', reader.read());
    }
    
    @Test
    public void testGetNextTokenADashSpace() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("a- "), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.TEXT, token.getType());
        assertEquals("a-", token.getContent());
        
        assertEquals(' ', reader.read());
    }
    
    @Test
    public void testGetNextTokenDashASpace() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("-a "), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.TEXT, token.getType());
        assertEquals("-a", token.getContent());
        
        assertEquals(' ', reader.read());
    }
    
    @Test
    public void testGetNextTokenStringSpace() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("thisiswildtext1902768 "), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.TEXT, token.getType());
        assertEquals("thisiswildtext1902768", token.getContent());
        
        assertEquals(' ', reader.read());
    }
    
    @Test
    public void testGetNextTokenStringCommentDelimiter() throws IOException {
        final PushbackReader reader = new PushbackReader(new StringReader("thisiswildtext1902768--"), 2);
        
        final Token token = tokenizer.getNextToken(reader);
        
        assertEquals(Token.Type.TEXT, token.getType());
        assertEquals("thisiswildtext1902768", token.getContent());
        
        assertEquals('-', reader.read());
        assertEquals('-', reader.read());
    }
    
}
