package org.silnith.browser.organic;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.TokenListStream;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.Declaration;
import org.silnith.browser.organic.parser.css3.grammar.token.DeclarationNode;
import org.silnith.browser.organic.parser.css3.grammar.token.QualifiedRuleNode;
import org.silnith.browser.organic.parser.css3.grammar.token.Rule;
import org.silnith.browser.organic.parser.css3.grammar.token.SimpleBlock;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
import org.silnith.browser.organic.property.accessor.PropertyAccessor;
import org.silnith.browser.organic.property.accessor.PropertyAccessorFactory;
import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StylesheetBuilder {
    
    public Stylesheet buildStylesheet(final Document document, final URI baseURL) {
        final Element htmlElement = document.getDocumentElement();
        final Element headElement = findHeadElement(htmlElement);
        final String defaultStyleType = findDefaultStylesheetLanguage(null, headElement);
        final Collection<ExternalStylesheetLink> externalStylesheetLinks = findLinkElements(headElement);
        findStyleElements(headElement);
        
        for (final ExternalStylesheetLink link : externalStylesheetLinks) {
            try {
                final URI href = link.getHref();
                System.out.println(href);
                final URI resolvedURI = baseURL.resolve(href);
                System.out.println(resolvedURI);
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
                    
                    final Tokenizer cssTokenizer = new Tokenizer(reader);
                    cssTokenizer.setAllowParseErrors(true);
                    final Parser stylesheetParser = new Parser(cssTokenizer);
                    stylesheetParser.prime();
                    
                    final List<Rule> rules = stylesheetParser.parseStylesheet();
                    for (final Rule rule : rules) {
                        if (rule instanceof QualifiedRuleNode) {
                            final QualifiedRuleNode qualifiedRuleNode = (QualifiedRuleNode) rule;
                            
                            // parse as selector list
                            final List<Token> prelude = qualifiedRuleNode.getPrelude();
                            System.out.println("prelude=" + prelude);
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
                                    
                                    System.out.println("name=" + name + ", value=" + value);
                                    
                                    // for each declaration, invoke a property-specific parse
                                    final PropertyName propertyName = PropertyName.getPropertyName(name);
                                    final PropertyAccessorFactory factory = PropertyAccessorFactory.getInstance();
                                    final PropertyAccessor<?> propertyAccessor = factory.getPropertyAccessor(propertyName);
                                    ;
                                }
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
        return createStaticStylesheet();
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

    private void findStyleElements(final Element headElement) {
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
                }
            } break;
            default: {} break;
            }
        }
    }
    
    private static Stylesheet createStaticStylesheet() {
        return new Stylesheet(createStyleList(), createGeneratedContentRules());
    }

    private static Collection<CSSRule> createStyleList() {
        final Collection<CSSRule> cssRules = new ArrayList<>();
        cssRules.add(new CSSRule("html", PropertyName.DISPLAY, "block"));
        cssRules.add(new CSSRule("html", PropertyName.COLOR, "black"));
        cssRules.add(new CSSRule("head", PropertyName.DISPLAY, "none"));
        cssRules.add(new CSSRule("head", PropertyName.FONT_SIZE, "x-small"));
        cssRules.add(new CSSRule("title", PropertyName.DISPLAY, "inline"));
        cssRules.add(new CSSRule("style", PropertyName.DISPLAY, "inherit"));
        cssRules.add(new CSSRule("body", PropertyName.DISPLAY, "block"));
        cssRules.add(new CSSRule("body", PropertyName.FONT_SIZE, "12pt"));
        cssRules.add(new CSSRule("body", PropertyName.BACKGROUND_COLOR, "silver"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_TOP_COLOR, "blue"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_RIGHT_COLOR, "blue"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_BOTTOM_COLOR, "blue"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new CSSRule("body", PropertyName.BORDER_LEFT_COLOR, "blue"));
        cssRules.add(new CSSRule("p", PropertyName.DISPLAY, "block"));
        cssRules.add(new CSSRule("p", PropertyName.FONT_SIZE, "16pt"));
        cssRules.add(new CSSRule("p", PropertyName.MARGIN_TOP, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.MARGIN_RIGHT, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.MARGIN_BOTTOM, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.MARGIN_LEFT, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.BACKGROUND_COLOR, "white"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_TOP_COLOR, "lime"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_RIGHT_COLOR, "lime"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_BOTTOM_COLOR, "lime"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new CSSRule("p", PropertyName.BORDER_LEFT_COLOR, "lime"));
        cssRules.add(new CSSRule("p", PropertyName.PADDING_TOP, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.PADDING_RIGHT, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.PADDING_BOTTOM, "3px"));
        cssRules.add(new CSSRule("p", PropertyName.PADDING_LEFT, "3px"));
        cssRules.add(new CSSRule("em", PropertyName.FONT_STYLE, "italic"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_WIDTH, "5px"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_TOP_COLOR, "fuchsia"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_WIDTH, "5px"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_RIGHT_COLOR, "fuchsia"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_WIDTH, "5px"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_BOTTOM_COLOR, "fuchsia"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_WIDTH, "5px"));
        cssRules.add(new CSSRule("em", PropertyName.BORDER_LEFT_COLOR, "fuchsia"));
        cssRules.add(new CSSRule("strong", PropertyName.FONT_WEIGHT, "bold"));
        cssRules.add(new CSSRule("strong", PropertyName.BACKGROUND_COLOR, "red"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_WIDTH, "8px"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_TOP_COLOR, "gray"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_WIDTH, "8px"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_RIGHT_COLOR, "gray"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_WIDTH, "8px"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_BOTTOM_COLOR, "gray"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_WIDTH, "8px"));
        cssRules.add(new CSSRule("strong", PropertyName.BORDER_LEFT_COLOR, "gray"));
        cssRules.add(new CSSRule("div", PropertyName.DISPLAY, "block"));
        cssRules.add(new CSSRule("div", PropertyName.MARGIN_TOP, "2em"));
        cssRules.add(new CSSRule("div", PropertyName.MARGIN_RIGHT, "2em"));
        cssRules.add(new CSSRule("div", PropertyName.MARGIN_BOTTOM, "2em"));
        cssRules.add(new CSSRule("div", PropertyName.MARGIN_LEFT, "2em"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_WIDTH, "15px"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_TOP_COLOR, "red"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_WIDTH, "15px"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_RIGHT_COLOR, "red"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_WIDTH, "15px"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_BOTTOM_COLOR, "red"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_WIDTH, "15px"));
        cssRules.add(new CSSRule("div", PropertyName.BORDER_LEFT_COLOR, "red"));
        cssRules.add(new CSSRule("div", PropertyName.PADDING_TOP, "6px"));
        cssRules.add(new CSSRule("div", PropertyName.PADDING_RIGHT, "6px"));
        cssRules.add(new CSSRule("div", PropertyName.PADDING_BOTTOM, "6px"));
        cssRules.add(new CSSRule("div", PropertyName.PADDING_LEFT, "6px"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_STYLE, "solid"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_WIDTH, "3px"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_TOP_COLOR, "aqua"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_STYLE, "solid"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_WIDTH, "3px"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_RIGHT_COLOR, "aqua"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_STYLE, "solid"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_WIDTH, "3px"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_BOTTOM_COLOR, "aqua"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_STYLE, "solid"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_WIDTH, "3px"));
        cssRules.add(new CSSRule("a", PropertyName.BORDER_LEFT_COLOR, "aqua"));
        cssRules.add(new CSSRule("a", PropertyName.PADDING_TOP, "1px"));
        cssRules.add(new CSSRule("a", PropertyName.PADDING_RIGHT, "1px"));
        cssRules.add(new CSSRule("a", PropertyName.PADDING_BOTTOM, "1px"));
        cssRules.add(new CSSRule("a", PropertyName.PADDING_LEFT, "1px"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_STYLE, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_WIDTH, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_TOP_COLOR, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_STYLE, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_WIDTH, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_RIGHT_COLOR, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_STYLE, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_WIDTH, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_BOTTOM_COLOR, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_STYLE, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_WIDTH, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.BORDER_LEFT_COLOR, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.PADDING_TOP, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.PADDING_RIGHT, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.PADDING_BOTTOM, "inherit"));
        cssRules.add(new CSSRule("abbr", PropertyName.PADDING_LEFT, "inherit"));
        
        return cssRules;
    }
    
    private static Collection<CSSPseudoElementRuleSet> createGeneratedContentRules() {
        final ArrayList<CSSPseudoElementRuleSet> pseudoRules = new ArrayList<>();
        final ArrayList<CSSRule> beforeRules = new ArrayList<>();
        final ArrayList<CSSRule> afterRules = new ArrayList<>();
        
        pseudoRules.add(new CSSPseudoElementRuleSet("div", "Div: ", null));
        beforeRules.clear();
        afterRules.clear();
        afterRules.add(new CSSRule(":after", PropertyName.DISPLAY, "none"));
        afterRules.add(new CSSRule(":after", PropertyName.BACKGROUND_COLOR, "aqua"));
        afterRules.add(new CSSRule(":after", PropertyName.BORDER_TOP_STYLE, "solid"));
        afterRules.add(new CSSRule(":after", PropertyName.COLOR, "red"));
        pseudoRules.add(new CSSPseudoElementRuleSet("p", null, "yomama", beforeRules, afterRules));
        
        return pseudoRules;
    }
    
}
