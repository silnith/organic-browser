package org.silnith.browser.organic.parser.html4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.junit.Test;
import org.silnith.browser.organic.parser.html4.token.Comment;
import org.silnith.browser.organic.parser.html4.token.EndTag;
import org.silnith.browser.organic.parser.html4.token.ProcessingInstruction;
import org.silnith.browser.organic.parser.html4.token.StartTag;
import org.silnith.browser.organic.parser.html4.token.Token;


public class Tokenizer2Test {
    
    @Test
    public void testReadComment() throws IOException {
        final String expected = " This is a test. ";
        final Reader reader = new StringReader("<!--" + expected + "-->");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final Comment comment = tokenizer.readComment();
        
        assertEquals(expected, comment.getContent());
    }
    
    @Test
    public void testReadCommentVeryLong() throws IOException {
        final String expected =
                " This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. ";
        final Reader reader = new StringReader("<!--" + expected + "-->");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final Comment comment = tokenizer.readComment();
        
        assertEquals(expected, comment.getContent());
    }
    
    @Test
    public void testReadCommentWhitespaceInEnd() throws IOException {
        final String expected = " This is a test. ";
        final Reader reader = new StringReader("<!--" + expected + "--    \t   \n\r  >");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final Comment comment = tokenizer.readComment();
        
        assertEquals(expected, comment.getContent());
    }
    
    @Test
    public void testReadCommentVeryLongWithWhitespaceInEnd() throws IOException {
        final String expected =
                " This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. ";
        final Reader reader = new StringReader("<!--" + expected + "--    \t   \n\r  >");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final Comment comment = tokenizer.readComment();
        
        assertEquals(expected, comment.getContent());
    }
    
    @Test
    public void testReadProcessingInstruction() throws IOException {
        final String expected = " This is a test. ";
        final Reader reader = new StringReader("<?" + expected + ">");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final ProcessingInstruction processingInstruction = tokenizer.readProcessingInstruction();
        
        assertEquals(expected, processingInstruction.getContent());
    }
    
    @Test
    public void testReadProcessingInstructionVeryLong() throws IOException {
        final String expected =
                " This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. ";
        final Reader reader = new StringReader("<?" + expected + ">");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final ProcessingInstruction processingInstruction = tokenizer.readProcessingInstruction();
        
        assertEquals(expected, processingInstruction.getContent());
    }
    
    @Test
    public void testReadName() throws IOException {
        final String expected = "abcd:efg._-abcdefg";
        final Reader reader = new StringReader(expected + "]]");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final String name = tokenizer.readName();
        
        assertEquals(expected, name);
    }
    
    @Test
    public void testParseBreak() throws IOException {
        final Reader reader = new StringReader("<BR>");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final StartTag startTag = tokenizer.readStartTag();
        
        assertEquals("BR", startTag.getName());
    }
    
    @Test
    public void testParseImage() throws IOException {
        final Reader reader = new StringReader("<IMG src='foo.jpg' alt='This is a test.'>");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final StartTag startTag = tokenizer.readStartTag();
        
        assertEquals("IMG", startTag.getName());
        assertFalse(startTag.isNetEnabling());
        final Map<String, String> attributes = startTag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("foo.jpg", attributes.get("src"));
        assertEquals("This is a test.", attributes.get("alt"));
    }
    
    @Test
    public void testParseAnchor() throws IOException {
        final Reader reader = new StringReader("<A href='foo.html'>This is a test.</A>");
        final Tokenizer2 tokenizer = new Tokenizer2(reader);
        
        final StartTag startTag = tokenizer.readStartTag();
        final Token.Type nextTokenType = tokenizer.sniffNextToken();
        final String content = tokenizer.readContent();
        final EndTag endTag = tokenizer.readEndTag();
        
        assertEquals("A", startTag.getName());
        assertFalse(startTag.isNetEnabling());
        final Map<String, String> attributes = startTag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("foo.html", attributes.get("href"));
        
        assertNull(nextTokenType);
        
        assertEquals("This is a test.", content);
        
        assertEquals("A", endTag.getName());
    }
    
}
