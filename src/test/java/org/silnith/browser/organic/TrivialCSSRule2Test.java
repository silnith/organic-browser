package org.silnith.browser.organic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.QualifiedRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Rule;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;


public class TrivialCSSRule2Test {
    
    private static DOMImplementationRegistry registry;
    
    private TrivialCSSRule2 rule;
    
    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
        registry = DOMImplementationRegistry.newInstance();
    }
    
    @Before
    public void setUp() throws IOException {
        try (final Reader reader = new StringReader("ignored { font-size: x-small; }")) {
            final Parser parser = new Parser(new Tokenizer(reader));
            parser.prime();
            final Rule parsedRule = parser.parseRule();
            if (parsedRule instanceof QualifiedRuleNode) {
                final QualifiedRuleNode qualifiedRuleNode = (QualifiedRuleNode) parsedRule;
                
                final List<Token> prelude = qualifiedRuleNode.getPrelude();
                final List<Token> declarations = qualifiedRuleNode.getBlock().getValue();
                
                rule = new TrivialCSSRule2(prelude, declarations);
            }
        }
    }
    
    @Test
    public void testApplyToDocument() {
        final String documentSource = "<html><head><title>test</title></head><body><h1>Heading</h1><p>This is a <em>test</em>.</p></body></html>";
        
        final DOMImplementation domImplementation = registry.getDOMImplementation("Core 3.0 +LS 3.0");
        final DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS", "3.0");
        final LSInput input = domImplementationLS.createLSInput();
        input.setStringData(documentSource);
        final LSParser parser = domImplementationLS.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
        
        final Document document = parser.parse(input);
        
        final StyleTreeBuilder styleTreeBuilder = new StyleTreeBuilder();
        final StyledElement styleInformation = styleTreeBuilder.addStyleInformation(document);
        
        // method under test
        rule.applyToDocument(styleInformation);
        
        final StyledDOMElement bodyStyle = (StyledDOMElement) styleInformation.getChildren().get(1);
        final StyledDOMElement pStyle = (StyledDOMElement) bodyStyle.getChildren().get(1);
        final StyledDOMElement emStyle = (StyledDOMElement) pStyle.getChildren().get(1);
        final List<Token> parsedSpecifiedValue = emStyle.getStyleData().getSpecifiedValue(PropertyName.FONT_SIZE);
        
        assertEquals(2, parsedSpecifiedValue.size());
        final Token token = parsedSpecifiedValue.get(1);
        assertEquals(Token.Type.LEXICAL_TOKEN, token.getType());
        final LexicalToken lexicalToken = (LexicalToken) token;
        assertEquals(LexicalToken.LexicalType.IDENT_TOKEN, lexicalToken.getLexicalType());
        final IdentToken identToken = (IdentToken) lexicalToken;
        assertEquals("x-small", identToken.getStringValue());
    }
    
}
