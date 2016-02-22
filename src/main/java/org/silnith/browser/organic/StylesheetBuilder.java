package org.silnith.browser.organic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.Declaration;
import org.silnith.browser.organic.parser.css3.grammar.token.DeclarationNode;
import org.silnith.browser.organic.parser.css3.grammar.token.QualifiedRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Rule;
import org.silnith.browser.organic.parser.css3.grammar.token.SimpleBlock;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
import org.silnith.browser.organic.parser.css3.lexical.token.DelimToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.selector.Selector;
import org.silnith.browser.organic.parser.css3.selector.SelectorParser;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StylesheetBuilder {
    
    public Collection<Stylesheet> buildStylesheets(final Document document, final URI baseURL) {
        final Element htmlElement = document.getDocumentElement();
        final Element headElement = findHeadElement(htmlElement);
        final String defaultStyleType = findDefaultStylesheetLanguage(null, headElement);
        final Collection<ExternalStylesheetLink> externalStylesheetLinks = findLinkElements(headElement);
        
        final List<Stylesheet> stylesheets = new ArrayList<>();
        try {
            stylesheets.add(new Stylesheet(defaultHTMLStylesheet(), new ArrayList<>()));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        
        for (final ExternalStylesheetLink link : externalStylesheetLinks) {
            try {
                stylesheets.add(readStylesheet(baseURL, link));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
        stylesheets.addAll(findStyleElements(headElement));
        
        return stylesheets;
    }

    private Stylesheet readStylesheet(final URI baseURL, final ExternalStylesheetLink link) throws MalformedURLException, IOException, UnsupportedEncodingException {
        final URI href = link.getHref();
        final URI resolvedURI = baseURL.resolve(href);
        final URL url = resolvedURI.toURL();
        final URLConnection openConnection = url.openConnection();
        final String contentEncoding = openConnection.getContentEncoding();
        
        try (final InputStream inputStream = openConnection.getInputStream()) {
            final Reader reader;
            if (contentEncoding == null) {
                reader = new InputStreamReader(inputStream, "UTF-8");
            } else {
                reader = new InputStreamReader(inputStream, contentEncoding);
            }
            
            System.out.println(resolvedURI);
            final List<CSSRule> parsedCSSRules = readStylesheetRules(reader);
            final List<CSSPseudoElementRuleSet> pseudoElementRules = new ArrayList<>();
            return new Stylesheet(parsedCSSRules, pseudoElementRules);
        }
    }

    private List<CSSRule> readStylesheetRules(final Reader reader) throws IOException {
        final Tokenizer cssTokenizer = new Tokenizer(reader);
        cssTokenizer.setAllowParseErrors(true);
        final Parser stylesheetParser = new Parser(cssTokenizer);
        stylesheetParser.prime();
        
        final List<Rule> rules = stylesheetParser.parseStylesheet();
        final List<CSSRule> parsedCSSRules = new ArrayList<>();
        for (final Rule rule : rules) {
            if (rule instanceof QualifiedRuleNode) {
                final QualifiedRuleNode qualifiedRuleNode = (QualifiedRuleNode) rule;
                
                // parse as selector list
                final List<Token> prelude = qualifiedRuleNode.getPrelude();
                System.out.println("prelude: " + join(prelude));
                final SelectorParser selectorParser = new SelectorParser(new TokenListStream(prelude));
                try {
                    final Selector selector = selectorParser.parse();
                    System.out.println("parsed selector: " + selector);
                    // parse as list of declarations
                    final SimpleBlock block = qualifiedRuleNode.getBlock();
                    final Parser declarationParser = new Parser(new TokenListStream(block.getValue()));
                    declarationParser.prime();
                    final List<Declaration> declarations = declarationParser.parseListOfDeclarations();
                    
                    for (final Declaration declaration : declarations) {
                        if (declaration instanceof DeclarationNode) {
                            final DeclarationNode declarationNode = (DeclarationNode) declaration;
                            
                            final String name = declarationNode.getName();
                            System.out.println("declaration name: " + name);
                            final List<Token> value = declarationNode.getValue();
                            
                            // for each declaration, invoke a property-specific parse
                            final PropertyName propertyName = PropertyName.getPropertyName(name);
                            
                            /*
                             * Making a copy because this check mangles the list
                             * and if it fails we need to use the original value.
                             */
                            final List<Token> valueCopy = new ArrayList<>(value);
                            final ListIterator<Token> listIterator = valueCopy.listIterator(valueCopy.size());
                            boolean important = false;
                            while (listIterator.hasPrevious()) {
                                final Token lastToken = listIterator.previous();
                                if (isWhitespaceToken(lastToken)) {
                                    listIterator.remove();
                                    continue;
                                }
                                if (!important && isKeywordImportant(lastToken)) {
                                    important = true;
                                    listIterator.remove();
                                    continue;
                                }
                                if (!important) {
                                    break;
                                }
                                assert important;
                                assert !isWhitespaceToken(lastToken);
                                if (isExclamationMarkDelimiter(lastToken)) {
                                    // use pruned list
                                    listIterator.remove();
                                    break;
                                }
                            }
                            final List<Token> propertyValue;
                            if (important) {
                                propertyValue = valueCopy;
                            } else {
                                propertyValue = value;
                            }
                            final Iterator<Token> iterator = propertyValue.iterator();
                            boolean inherit = false;
                            while (iterator.hasNext()) {
                                final Token token = iterator.next();
                                if (isWhitespaceToken(token)) {
                                    continue;
                                }
                                if (isKeywordInherit(token)) {
                                    inherit = true;
                                    continue;
                                }
                                inherit = false;
                                break;
                            }
                            System.out.print("property value: ");
                            if (inherit) {
                                System.out.print("inherit");
                            } else {
                                System.out.print(propertyValue);
                            }
                            if (important) {
                                System.out.print(" ! important");
                            }
                            System.out.println();
                            
                            final ParsedCSSRule parsedCSSRule;
                            if (inherit) {
                                parsedCSSRule = new ParsedCSSRule(selector, propertyName, important);
                            } else {
                                parsedCSSRule = new ParsedCSSRule(selector, propertyName, propertyValue, important);
                            }
                            parsedCSSRules.add(parsedCSSRule);
                        }
                    }
                } catch (final RuntimeException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return parsedCSSRules;
    }
    
    private boolean isWhitespaceToken(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case WHITESPACE_TOKEN: {
                return true;
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isKeywordImportant(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                return "important".equals(identToken.getStringValue());
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isKeywordInherit(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                return "inherit".equals(identToken.getStringValue());
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private boolean isExclamationMarkDelimiter(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case DELIM_TOKEN: {
                final DelimToken delimToken = (DelimToken) lexicalToken;
                return "!".equals(delimToken.getChar());
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        return false;
    }
    
    private String join(final List<Token> tokens) {
        final StringBuilder builder = new StringBuilder();
        for (final Token token : tokens) {
            builder.append(token.toString());
        }
//        if (!tokens.isEmpty()) {
//            final Iterator<Token> iterator = tokens.iterator();
//            final Token token = iterator.next();
//            builder.append(token.toString());
//            while (iterator.hasNext()) {
//                final Token next = iterator.next();
//                builder.append(next.toString());
//            }
//        }
        return builder.toString();
    }

    private Element findHeadElement(final Element htmlElement) {
        final NodeList childNodes = htmlElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            switch (childNode.getNodeType()) {
            case Node.ELEMENT_NODE: {
                final Element element = (Element) childNode;
                final String tagName = element.getTagName();
                if ("HEAD".equalsIgnoreCase(tagName)) {
                    return element;
                }
            } break;
            default: {} break;
            }
        }
        
        throw new IllegalArgumentException("There is no HEAD element.");
    }
    
    /**
     * This is used for inline style attributes on various elements.
     * 
     * @param contentStyleTypeHeader
     * @param headElement
     * @return
     */
    private String findDefaultStylesheetLanguage(final String contentStyleTypeHeader, final Element headElement) {
        String contentStyleType = "text/css";
        
        if (contentStyleTypeHeader != null) {
            contentStyleType = contentStyleTypeHeader;
        }
        
        final NodeList childNodes = headElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            switch (childNode.getNodeType()) {
            case Node.ELEMENT_NODE: {
                final Element element = (Element) childNode;
                final String httpEquiv = element.getAttribute("http-equiv");
                if (httpEquiv.equalsIgnoreCase("Content-Style-Type")) {
                    final String content = element.getAttribute("content");
                    contentStyleType = content;
                }
                /*
                 * Do not break because if the header is specified multiple times
                 * we want the LAST one to be used.
                 */
            } break;
            default: {} break;
            }
        }
        
        return contentStyleType;
    }
    
    private String findPreferredStylesheet(final String defaultStyleHeader, final Element headElement) {
        // "Default-Style"
        return defaultStyleHeader;
    }
    
    private Collection<ExternalStylesheetLink> findLinkElements(final Element headElement) {
        final List<ExternalStylesheetLink> links = new ArrayList<>();
        
        final NodeList childNodes = headElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            switch (childNode.getNodeType()) {
            case Node.ELEMENT_NODE: {
                final Element element = (Element) childNode;
                final String tagName = element.getTagName();
                if ("LINK".equalsIgnoreCase(tagName)) {
                    final String href = element.getAttribute("href");
                    final URI stylesheetLocation = URI.create(href);
                    final String type = element.getAttribute("type");
                    final String rel = element.getAttribute("rel");
                    final String title = element.getAttribute("title");
                    final String media = element.getAttribute("media");
                    
                    if (type.equals("text/css")) {
                        links.add(new ExternalStylesheetLink(stylesheetLocation, rel, type, media, title));
                    }
                }
            } break;
            default: {} break;
            }
        }
        
        return links;
    }

    private Collection<Stylesheet> findStyleElements(final Element headElement) {
        final List<Stylesheet> stylesheets = new ArrayList<>();
        
        final NodeList childNodes = headElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            switch (childNode.getNodeType()) {
            case Node.ELEMENT_NODE: {
                final Element element = (Element) childNode;
                final String tagName = element.getTagName();
                if ("STYLE".equalsIgnoreCase(tagName)) {
                    // the type attribute is mandatory
                    final String type = element.getAttribute("type");
                    // the title attribute on a STYLE element does NOT identify it as an alternate stylesheet
                    final String title = element.getAttribute("title");
                    final String media = element.getAttribute("media");
                    
                    if (type.equals("text/css")) {
                        System.out.println("inline");
                        final String stylesheetText = element.getTextContent();
                        try (final StringReader reader = new StringReader(stylesheetText)) {
                            stylesheets.add(new Stylesheet(readStylesheetRules(reader), new ArrayList<>()));
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } break;
            default: {} break;
            }
        }
        
        return stylesheets;
    }

    private static Collection<CSSPseudoElementRuleSet> createGeneratedContentRules() {
        final ArrayList<CSSPseudoElementRuleSet> pseudoRules = new ArrayList<>();
        final ArrayList<CSSRule> beforeRules = new ArrayList<>();
        final ArrayList<CSSRule> afterRules = new ArrayList<>();
        
        pseudoRules.add(new CSSPseudoElementRuleSet("div", "Div: ", null));
        beforeRules.clear();
        afterRules.clear();
//        afterRules.add(new RawCSSRule(":after", PropertyName.DISPLAY, "none"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
//        afterRules.add(new RawCSSRule(":after", PropertyName.COLOR, "red"));
        pseudoRules.add(new CSSPseudoElementRuleSet("p", null, "yomama", beforeRules, afterRules));
        
        return pseudoRules;
    }
    
    private Collection<CSSRule> defaultHTMLStylesheet() throws IOException {
        return readStylesheetRules(new InputStreamReader(StylesheetBuilder.class.getResourceAsStream("html4.css"), Charset.forName("UTF-8")));
    }

}
