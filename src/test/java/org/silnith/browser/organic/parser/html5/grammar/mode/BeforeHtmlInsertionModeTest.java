package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;


public class BeforeHtmlInsertionModeTest {
    
    private DOMImplementationRegistry registry;
    
    private Tokenizer tokenizer;
    
    private Parser parser;
    
    private BeforeHtmlInsertionMode beforeHtmlInsertionMode;
    
    @Before
    public void setUp()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
        registry = DOMImplementationRegistry.newInstance();
    }
    
    @Test
    public void testInsert() {
        tokenizer = new Tokenizer(new StringReader("<html lang='la'>"));
        parser = new Parser(tokenizer, registry);
        beforeHtmlInsertionMode = new BeforeHtmlInsertionMode(parser);
        
        final StartTagToken startTagToken = new StartTagToken();
        startTagToken.appendToTagName('h');
        startTagToken.appendToTagName('t');
        startTagToken.appendToTagName('m');
        startTagToken.appendToTagName('l');
        
        final boolean accepted = beforeHtmlInsertionMode.insert(startTagToken);
        
        assertTrue(accepted);
    }
    
}
