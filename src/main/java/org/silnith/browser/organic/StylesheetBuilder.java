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

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.Declaration;
import org.silnith.browser.organic.parser.css3.grammar.token.DeclarationNode;
import org.silnith.browser.organic.parser.css3.grammar.token.QualifiedRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Rule;
import org.silnith.browser.organic.parser.css3.grammar.token.SimpleBlock;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
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
        stylesheets.add(createDefaultStylesheet());
//        try {
//            stylesheets.add(new Stylesheet(defaultHTMLStylesheet(), new ArrayList<>()));
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
        stylesheets.add(createStaticStylesheet());
        
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
                            final List<Token> value = declarationNode.getValue();
                            
                            // for each declaration, invoke a property-specific parse
                            final PropertyName propertyName = PropertyName.getPropertyName(name);
                            
                            final ParsedCSSRule parsedCSSRule = new ParsedCSSRule(selector, propertyName, value);
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

    private static Stylesheet createDefaultStylesheet() {
        return new Stylesheet(defaultHTMLRules(), new ArrayList<>());
    }

    private static Stylesheet createStaticStylesheet() {
        return new Stylesheet(createStyleList(), createGeneratedContentRules());
    }

    private static Collection<CSSRule> createStyleList() {
        final Collection<CSSRule> cssRules = new ArrayList<>();
        cssRules.add(new RawCSSRule("html", PropertyName.COLOR, "black"));
        cssRules.add(new RawCSSRule("head", PropertyName.FONT_SIZE, "x-small"));
        cssRules.add(new RawCSSRule("body", PropertyName.FONT_SIZE, "12pt"));
        cssRules.add(new RawCSSRule("body", PropertyName.BACKGROUND_COLOR, "silver"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_TOP_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_RIGHT_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_BOTTOM_COLOR, "blue"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("body", PropertyName.BORDER_LEFT_COLOR, "blue"));
        cssRules.add(new RawCSSRule("p", PropertyName.FONT_SIZE, "16pt"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_TOP, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_RIGHT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_BOTTOM, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.MARGIN_LEFT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BACKGROUND_COLOR, "white"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_TOP_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_RIGHT_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_BOTTOM_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("p", PropertyName.BORDER_LEFT_COLOR, "lime"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_TOP, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_RIGHT, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_BOTTOM, "3px"));
        cssRules.add(new RawCSSRule("p", PropertyName.PADDING_LEFT, "3px"));
        cssRules.add(new RawCSSRule("em", PropertyName.FONT_STYLE, "italic"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("em", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("strong", PropertyName.FONT_WEIGHT, "bold"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BACKGROUND_COLOR, "red"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_TOP_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_RIGHT_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_BOTTOM_COLOR, "gray"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_WIDTH, "8px"));
        cssRules.add(new RawCSSRule("strong", PropertyName.BORDER_LEFT_COLOR, "gray"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_TOP, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_RIGHT, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_BOTTOM, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.MARGIN_LEFT, "2em"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_TOP_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_RIGHT_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_BOTTOM_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new RawCSSRule("div", PropertyName.BORDER_LEFT_COLOR, "red"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_TOP, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_RIGHT, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_BOTTOM, "6px"));
        cssRules.add(new RawCSSRule("div", PropertyName.PADDING_LEFT, "6px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_TOP_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_RIGHT_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_BOTTOM_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_WIDTH, "3px"));
        cssRules.add(new RawCSSRule("a", PropertyName.BORDER_LEFT_COLOR, "aqua"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_TOP, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_RIGHT, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_BOTTOM, "1px"));
        cssRules.add(new RawCSSRule("a", PropertyName.PADDING_LEFT, "1px"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_TOP_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_RIGHT_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_BOTTOM_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_STYLE, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_WIDTH, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.BORDER_LEFT_COLOR, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_TOP, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_RIGHT, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_BOTTOM, "inherit"));
        cssRules.add(new RawCSSRule("abbr", PropertyName.PADDING_LEFT, "inherit"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_TOP_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_RIGHT_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_BOTTOM_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.BORDER_LEFT_COLOR, "black"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("ol", PropertyName.PADDING_LEFT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.PADDING_LEFT, "5px"));
        cssRules.add(new RawCSSRule("ul", PropertyName.LIST_STYLE_POSITION, "inside"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_TOP_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_RIGHT_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_BOTTOM_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.BORDER_LEFT_COLOR, "orange"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_TOP, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_RIGHT, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_BOTTOM, "5px"));
        cssRules.add(new RawCSSRule("li", PropertyName.PADDING_LEFT, "5px"));
        
        return cssRules;
    }

    private static Collection<CSSPseudoElementRuleSet> createGeneratedContentRules() {
        final ArrayList<CSSPseudoElementRuleSet> pseudoRules = new ArrayList<>();
        final ArrayList<CSSRule> beforeRules = new ArrayList<>();
        final ArrayList<CSSRule> afterRules = new ArrayList<>();
        
        pseudoRules.add(new CSSPseudoElementRuleSet("div", "Div: ", null));
        beforeRules.clear();
        afterRules.clear();
        afterRules.add(new RawCSSRule(":after", PropertyName.DISPLAY, "none"));
        afterRules.add(new RawCSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
        afterRules.add(new RawCSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
        afterRules.add(new RawCSSRule(":after", PropertyName.COLOR, "red"));
        pseudoRules.add(new CSSPseudoElementRuleSet("p", null, "yomama", beforeRules, afterRules));
        
        return pseudoRules;
    }
    
    private Collection<CSSRule> defaultHTMLStylesheet() throws IOException {
        return readStylesheetRules(new InputStreamReader(StylesheetBuilder.class.getResourceAsStream("html4.css"), Charset.forName("UTF-8")));
    }

    private static Collection<CSSRule> defaultHTMLRules() {
        final List<CSSRule> rules = new ArrayList<>();
        
        rules.add(new RawCSSRule("html", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("address", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("blockquote", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("body", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dd", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("div", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dl", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dt", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("fieldset", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("form", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("frame", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("frameset", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h1", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h2", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h3", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h4", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h5", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("h6", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("noframes", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("ol", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("p", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("ul", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("center", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("dir", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("hr", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("menu", PropertyName.DISPLAY, "block"));
        rules.add(new RawCSSRule("pre", PropertyName.DISPLAY, "block"));
        
        rules.add(new RawCSSRule("html", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("address", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("blockquote", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("body", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dd", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("div", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dl", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dt", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("fieldset", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("form", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("frame", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("frameset", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h1", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h2", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h3", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h4", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h5", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("h6", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("noframes", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("ol", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("p", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("ul", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("center", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("dir", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("hr", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("menu", PropertyName.UNICODE_BIDI, "embed"));
        rules.add(new RawCSSRule("pre", PropertyName.UNICODE_BIDI, "embed"));
        
        rules.add(new RawCSSRule("li", PropertyName.DISPLAY, "list-item"));
        rules.add(new RawCSSRule("head", PropertyName.DISPLAY, "none"));
        rules.add(new RawCSSRule("table", PropertyName.DISPLAY, "table"));
        rules.add(new RawCSSRule("tr", PropertyName.DISPLAY, "table-row"));
        rules.add(new RawCSSRule("thead", PropertyName.DISPLAY, "table-header-group"));
        rules.add(new RawCSSRule("tbody", PropertyName.DISPLAY, "table-row-group"));
        rules.add(new RawCSSRule("tfoot", PropertyName.DISPLAY, "table-footer-group"));
        rules.add(new RawCSSRule("col", PropertyName.DISPLAY, "table-column"));
        rules.add(new RawCSSRule("colgroup", PropertyName.DISPLAY, "table-column-group"));
        rules.add(new RawCSSRule("td", PropertyName.DISPLAY, "table-cell"));
        rules.add(new RawCSSRule("th", PropertyName.DISPLAY, "table-cell"));
        rules.add(new RawCSSRule("caption", PropertyName.DISPLAY, "table-caption"));
        rules.add(new RawCSSRule("th", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("th", PropertyName.TEXT_ALIGN, "center"));
        rules.add(new RawCSSRule("caption", PropertyName.TEXT_ALIGN, "center"));
        
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_TOP, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_RIGHT, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_BOTTOM, "8px"));
        rules.add(new RawCSSRule("body", PropertyName.MARGIN_LEFT, "8px"));
        
        rules.add(new RawCSSRule("h1", PropertyName.FONT_SIZE, "2em"));
        rules.add(new RawCSSRule("h1", PropertyName.MARGIN_TOP, ".67em"));
        rules.add(new RawCSSRule("h1", PropertyName.MARGIN_BOTTOM, ".67em"));
        
        rules.add(new RawCSSRule("h2", PropertyName.FONT_SIZE, "1.5em"));
        rules.add(new RawCSSRule("h2", PropertyName.MARGIN_TOP, ".75em"));
        rules.add(new RawCSSRule("h2", PropertyName.MARGIN_BOTTOM, ".75em"));
        
        rules.add(new RawCSSRule("h3", PropertyName.FONT_SIZE, "1.17em"));
        rules.add(new RawCSSRule("h3", PropertyName.MARGIN_TOP, ".83em"));
        rules.add(new RawCSSRule("h3", PropertyName.MARGIN_BOTTOM, ".83em"));
        
        rules.add(new RawCSSRule("h4", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("h4", PropertyName.MARGIN_BOTTOM, "1.12em"));
        
        rules.add(new RawCSSRule("p", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("p", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("ul", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("ul", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("fieldset", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("fieldset", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("form", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("form", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("ol", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("ol", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("dl", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("dl", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_BOTTOM, "1.12em"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_TOP, "1.12em"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_BOTTOM, "1.12em"));
        
        rules.add(new RawCSSRule("h5", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("h5", PropertyName.MARGIN_TOP, "1.5em"));
        rules.add(new RawCSSRule("h5", PropertyName.MARGIN_BOTTOM, "1.5em"));
        
        rules.add(new RawCSSRule("h6", PropertyName.FONT_SIZE, ".75em"));
        rules.add(new RawCSSRule("h6", PropertyName.MARGIN_TOP, "1.67em"));
        rules.add(new RawCSSRule("h6", PropertyName.MARGIN_BOTTOM, "1.67em"));
        
        rules.add(new RawCSSRule("h1", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h2", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h3", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h4", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h5", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("h6", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("b", PropertyName.FONT_WEIGHT, "bolder"));
        rules.add(new RawCSSRule("strong", PropertyName.FONT_WEIGHT, "bolder"));
        
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_RIGHT, "40px"));
        rules.add(new RawCSSRule("blockquote", PropertyName.MARGIN_LEFT, "40px"));
        
        rules.add(new RawCSSRule("i", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("cite", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("em", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("var", PropertyName.FONT_STYLE, "italic"));
        rules.add(new RawCSSRule("address", PropertyName.FONT_STYLE, "italic"));
        
        rules.add(new RawCSSRule("pre", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("tt", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("code", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("kbd", PropertyName.FONT_FAMILY, "monospace"));
        rules.add(new RawCSSRule("samp", PropertyName.FONT_FAMILY, "monospace"));
        
        rules.add(new RawCSSRule("pre", PropertyName.WHITE_SPACE, "pre"));
        
        rules.add(new RawCSSRule("button", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("textarea", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("input", PropertyName.DISPLAY, "inline-block"));
        rules.add(new RawCSSRule("select", PropertyName.DISPLAY, "inline-block"));
        
        rules.add(new RawCSSRule("big", PropertyName.FONT_SIZE, "1.17em"));
        rules.add(new RawCSSRule("small", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sub", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sup", PropertyName.FONT_SIZE, ".83em"));
        rules.add(new RawCSSRule("sub", PropertyName.VERTICAL_ALIGN, "sub"));
        rules.add(new RawCSSRule("sup", PropertyName.VERTICAL_ALIGN, "super"));
        
        rules.add(new RawCSSRule("table", PropertyName.BORDER_SPACING, "2px"));
        rules.add(new RawCSSRule("thead", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("tbody", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("tfoot", PropertyName.VERTICAL_ALIGN, "middle"));
        rules.add(new RawCSSRule("td", PropertyName.VERTICAL_ALIGN, "inherit"));
        rules.add(new RawCSSRule("th", PropertyName.VERTICAL_ALIGN, "inherit"));
        rules.add(new RawCSSRule("tr", PropertyName.VERTICAL_ALIGN, "inherit"));
        
        rules.add(new RawCSSRule("s", PropertyName.TEXT_DECORATION, "line-through"));
        rules.add(new RawCSSRule("strike", PropertyName.TEXT_DECORATION, "line-through"));
        rules.add(new RawCSSRule("del", PropertyName.TEXT_DECORATION, "line-through"));
        
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_TOP_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_RIGHT_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_BOTTOM_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_LEFT_WIDTH, "1px"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_TOP_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_RIGHT_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_BOTTOM_STYLE, "inset"));
        rules.add(new RawCSSRule("hr", PropertyName.BORDER_LEFT_STYLE, "inset"));
        
//              rules.add(new CSSRule("ol", PropertyName.MARGIN_LEFT, "40px"));
//              rules.add(new CSSRule("ul", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("dir", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("menu", PropertyName.MARGIN_LEFT, "40px"));
        rules.add(new RawCSSRule("dd", PropertyName.MARGIN_LEFT, "40px"));
        
        rules.add(new RawCSSRule("ol", PropertyName.LIST_STYLE_TYPE, "decimal"));
        
        rules.add(new RawCSSRule("u", PropertyName.TEXT_DECORATION, "underline"));
        rules.add(new RawCSSRule("ins", PropertyName.TEXT_DECORATION, "underline"));
        
        rules.add(new RawCSSRule("br:before", PropertyName.CONTENT, "\\A"));
        rules.add(new RawCSSRule("br:before", PropertyName.WHITE_SPACE, "pre-line"));
        
        rules.add(new RawCSSRule("center", PropertyName.TEXT_ALIGN, "center"));
        
        rules.add(new RawCSSRule(":link", PropertyName.TEXT_DECORATION, "underline"));
        rules.add(new RawCSSRule(":visited", PropertyName.TEXT_DECORATION, "underline"));
        
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_WIDTH, "thin"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_STYLE, "dotted"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_TOP_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_RIGHT_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_BOTTOM_COLOR, "invert"));
        rules.add(new RawCSSRule(":focus", PropertyName.BORDER_LEFT_COLOR, "invert"));
        
        return rules;
    }
    
}
