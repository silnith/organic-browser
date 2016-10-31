package org.silnith.browser.organic.parser.css3.lexical;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class InputStreamPreprocessorTest {
    
    private static final char NULL = '\u0000';
    private static final char LF = '\n';
    private static final char FF = '\f';
    private static final char CR = '\r';
    private static final char REPLACEMENT_CHARACTER = '\ufffd';
    private static final int EOF = -1;
    
    private static final String NULL_S = String.valueOf(NULL);
    private static final String LF_S = String.valueOf(LF);
    private static final String FF_S = String.valueOf(FF);
    private static final String CR_S = String.valueOf(CR);
    
    private InputStreamPreprocessor inputStream;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    @Ignore
    public void testRead() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testRead_CarriageReturn() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S));
        
        assertEquals(LF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    public void testRead_FormFeed() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(FF_S));
        
        assertEquals(LF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    public void testRead_LineFeed() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(LF_S));
        
        assertEquals(LF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    public void testRead_CarriageReturnLineFeedPair() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S + LF_S));
        
        assertEquals(LF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    public void testRead_NullCharacter() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(NULL_S));
        
        assertEquals(REPLACEMENT_CHARACTER, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    public void testRead_SequenceOfCarriageReturnsAndLineFeeds() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S + CR_S + LF_S + LF_S + FF_S + CR_S + LF_S));
        
        assertEquals(LF, inputStream.read());
        assertEquals(LF, inputStream.read());
        assertEquals(LF, inputStream.read());
        assertEquals(LF, inputStream.read());
        assertEquals(LF, inputStream.read());
        assertEquals(EOF, inputStream.read());
    }
    
    @Test
    @Ignore
    public void testReadCharArray() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testReadCharArray_SequenceOfCarriageReturnsAndLineFeeds() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S + CR_S + LF_S + LF_S + FF_S + CR_S + LF_S));
        
        final char[] buf = new char[16];
        final int read = inputStream.read(buf);
        
        assertEquals(5, read);
        assertArrayEquals(new char[] {LF, LF, LF, LF, LF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buf);
    }
    
    @Test
    @Ignore
    public void testReadCharArrayIntInt() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testReadCharArrayIntInt_SequenceOfCarriageReturnsAndLineFeeds() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S + CR_S + LF_S + LF_S + FF_S + CR_S + LF_S));
        
        final char[] buf = new char[16];
        final int read = inputStream.read(buf, 2, 8);
        
        assertEquals(5, read);
        assertArrayEquals(new char[] {0, 0, LF, LF, LF, LF, LF, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buf);
    }
    
    @Test
    public void testReadCharArrayIntInt_SequenceOfCarriageReturnsAndLineFeeds_Truncated() throws IOException {
        inputStream = new InputStreamPreprocessor(new StringReader(CR_S + CR_S + LF_S + LF_S + FF_S + CR_S + LF_S));
        
        final char[] buf = new char[16];
        final int read = inputStream.read(buf, 2, 4);
        
        assertEquals(4, read);
        assertArrayEquals(new char[] {0, 0, LF, LF, LF, LF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buf);
    }
    
    @Test
    @Ignore
    public void testSkip() {
        fail("Not yet implemented");
    }
    
    @Test
    @Ignore
    public void testReady() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testMarkSupported() {
        inputStream = new InputStreamPreprocessor(new StringReader("foo { bar: baz; }"));
        
        assertFalse(inputStream.markSupported());
    }
    
    @Test
    @Ignore
    public void testReset() {
        fail("Not yet implemented");
    }
    
    @Test
    @Ignore
    public void testClose() {
        fail("Not yet implemented");
    }
    
    @Test
    @Ignore
    public void testInputStreamPreprocessor() {
        fail("Not yet implemented");
    }
    
}
