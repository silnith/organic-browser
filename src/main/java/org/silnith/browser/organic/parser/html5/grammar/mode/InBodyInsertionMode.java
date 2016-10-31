package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.grammar.Parser.FormattingElement;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inbody">
 *      12.2.5.4.7 The "in body" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InBodyInsertionMode extends InsertionMode {
    
    public InBodyInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            final char character = characterToken.getCharacter();
            switch (character) {
            case NULL: {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Null character in body.");
                }
            } // break;
            case CHARACTER_TABULATION: // fall through
            case LINE_FEED: // fall through
            case FORM_FEED: // fall through
            case CARRIAGE_RETURN: // fall through
            case SPACE: {
                reconstructActiveFormattingElements();
                insertCharacter(character);
                return TOKEN_HANDLED;
            } // break;
            default: {
                reconstructActiveFormattingElements();
                insertCharacter(character);
                setFramesetOKFlag(NOT_OK);
                return TOKEN_HANDLED;
            } // break;
            }
        } // break;
        case COMMENT: {
            final CommentToken commentToken = (CommentToken) token;
            insertComment(commentToken);
            return TOKEN_HANDLED;
        } // break;
        case DOCTYPE: {
            if (isAllowParseErrors()) {
                return IGNORE_TOKEN;
            } else {
                throw new ParseErrorException("Unexpected DOCTYPE in body: " + token);
            }
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "html": {
                if (isAllowParseErrors()) {
                    if (isStackOfOpenElementsContains("template")) {
                        return IGNORE_TOKEN;
                    }
                    for (final TagToken.Attribute attribute : startTagToken.getAttributes()) {
                        final String name = attribute.getName();
                        final Element htmlElement = getFirstElementInStackOfOpenElements();
                        if ( !htmlElement.hasAttribute(name)) {
                            htmlElement.setAttribute(name, attribute.getValue());
                        }
                    }
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in body: " + startTagToken);
                }
            } // break;
            case "base": // fall through
            case "basefont": // fall through
            case "bgsound": // fall through
            case "link": // fall through
            case "meta": // fall through
            case "noframes": // fall through
            case "script": // fall through
            case "style": // fall through
            case "template": // fall through
            case "title": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
            } // break;
            case "body": {
                if (isAllowParseErrors()) {
                    if (isStackOfOpenElementsHasOnlyOneNode()) {
                        return IGNORE_TOKEN;
                    }
                    final Element bodyElement = getSecondElementOfStackOfOpenElements();
                    if ( !isElementA(bodyElement, "body")) {
                        return IGNORE_TOKEN;
                    }
                    if (isStackOfOpenElementsContains("template")) {
                        return IGNORE_TOKEN;
                    }
                    setFramesetOKFlag(NOT_OK);
                    for (final TagToken.Attribute attribute : startTagToken.getAttributes()) {
                        final String name = attribute.getName();
                        if ( !bodyElement.hasAttribute(name)) {
                            bodyElement.setAttribute(name, attribute.getValue());
                        }
                    }
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in body: " + startTagToken);
                }
            } // break;
            case "frameset": {
                if (isAllowParseErrors()) {
                    if (isStackOfOpenElementsHasOnlyOneNode()) {
                        return IGNORE_TOKEN;
                    }
                    final Element bodyElement = getSecondElementOfStackOfOpenElements();
                    if ( !isElementA(bodyElement, "body")) {
                        return IGNORE_TOKEN;
                    }
                    if ( !isFramesetOkFlag()) {
                        return IGNORE_TOKEN;
                    }
                    if (bodyElement.getParentNode() != null) {
                        bodyElement.getParentNode().removeChild(bodyElement);
                    }
                    Element popped;
                    do {
                        popped = popCurrentNode();
                    } while (popped != bodyElement);
                    insertHTMLElement(startTagToken);
                    setInsertionMode(Parser.Mode.IN_FRAMESET);
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in body: " + token);
                }
            } // break;
            case "address": // fall through
            case "article": // fall through
            case "aside": // fall through
            case "blockquote": // fall through
            case "center": // fall through
            case "details": // fall through
            case "dialog": // fall through
            case "dir": // fall through
            case "div": // fall through
            case "dl": // fall through
            case "fieldset": // fall through
            case "figcaption": // fall through
            case "figure": // fall through
            case "footer": // fall through
            case "header": // fall through
            case "hgroup": // fall through
            case "main": // fall through
            case "menu": // fall through
            case "nav": // fall through
            case "ol": // fall through
            case "p": // fall through
            case "section": // fall through
            case "summary": // fall through
            case "ul": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "h1": // fall through
            case "h2": // fall through
            case "h3": // fall through
            case "h4": // fall through
            case "h5": // fall through
            case "h6": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                // if current node is h1, h2, h3, h4, h5, h6 element, parse
                // error
                if (isElementA(getCurrentNode(), "h1", "h2", "h3", "h4", "h5", "h6")) {
                    if (isAllowParseErrors()) {
                        popCurrentNode();
                        break;
                    } else {
                        throw new ParseErrorException("Found " + startTagToken.getTagName() + " nested inside "
                                + getCurrentNode().getTagName());
                    }
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "pre": // fall through
            case "listing": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                // peek next token, if it is LINE_FEED character, ignore it
                setFramesetOKFlag(NOT_OK);
                return TOKEN_HANDLED;
            } // break;
            case "form": {
                if (getFormElementPointer() != null && !isStackOfOpenElementsContains("template")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException();
                    }
                }
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                final Element formElement = insertHTMLElement(startTagToken);
                if ( !isStackOfOpenElementsContains("template")) {
                    setFormElementPointer(formElement);
                }
                return TOKEN_HANDLED;
            } // break;
            case "li": {
                setFramesetOKFlag(NOT_OK);
                int index = getStackOfOpenElementsSize() - 1;
                while (index >= 0) {
                    final Element node = getOpenElement(index);
                    if (isElementA(node, "li")) {
                        generateImpliedEndTagsExcept("li");
                        if ( !isElementA(getCurrentNode(), "li")) {
                            if (isAllowParseErrors()) {
                                // do nothing?
                            } else {
                                throw new ParseErrorException(
                                        "Unclosed element inside li element: " + getCurrentNode().getTagName());
                            }
                        }
                        Element popped = popCurrentNode();
                        do {
                            popped = popCurrentNode();
                        } while ( !isElementA(popped, "li"));
                        break;
                    } else if (isSpecialCategoryExcept(node.getTagName(), "address", "div", "p")) {
                        break;
                    } else {
                        // loop
                        index-- ;
                    }
                }
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "dd": // fall through
            case "dt": {
                setFramesetOKFlag(NOT_OK);
                int index = getStackOfOpenElementsSize() - 1;
                while (index >= 0) {
                    final Element node = getOpenElement(index);
                    if (isElementA(node, "dd")) {
                        generateImpliedEndTagsExcept("dd");
                        if ( !isElementA(getCurrentNode(), "dd")) {
                            if (isAllowParseErrors()) {
                                // do nothing?
                            } else {
                                throw new ParseErrorException(
                                        "Unclosed element inside dd element: " + getCurrentNode().getTagName());
                            }
                        }
                        Element popped = popCurrentNode();
                        do {
                            popped = popCurrentNode();
                        } while ( !isElementA(popped, "dd"));
                        break;
                    } else if (isElementA(node, "dt")) {
                        generateImpliedEndTagsExcept("dt");
                        if ( !isElementA(getCurrentNode(), "dt")) {
                            if (isAllowParseErrors()) {
                                // do nothing?
                            } else {
                                throw new ParseErrorException(
                                        "Unclosed element inside dt element: " + getCurrentNode().getTagName());
                            }
                        }
                        Element popped = popCurrentNode();
                        do {
                            popped = popCurrentNode();
                        } while ( !isElementA(popped, "dt"));
                        break;
                    } else if (isSpecialCategoryExcept(node.getTagName(), "address", "div", "p")) {
                        break;
                    } else {
                        // loop
                        index-- ;
                    }
                }
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "plaintext": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                setTokenizerState(Tokenizer.State.PLAINTEXT);
                return TOKEN_HANDLED;
                /*
                 * There is no way to switch out of the plaintext state.
                 */
            } // break;
            case "button": {
                // if stack of open elements has a button, parse error
                if (hasParticularElementInScope("button")) {
                    if (isAllowParseErrors()) {
                        generateImpliedEndTags();
                        Element popped;
                        do {
                            popped = popCurrentNode();
                        } while ( !isElementA(popped, "button"));
                    } else {
                        throw new ParseErrorException("Attempt to nest button element inside another button element.");
                    }
                }
                reconstructActiveFormattingElements();
                insertHTMLElement(startTagToken);
                setFramesetOKFlag(NOT_OK);
                return true;
            } // break;
            case "a": {
                // if list of active formatting elements contains "a" after last
                // marker
                if (isListOfActiveFormattingElementsContainsAfterLastMarker("a")) {
                    if (isAllowParseErrors()) {
                        adoptionAgencyAlgorithm("a");
                        // remove the anchor element from the list of active
                        // formatting elements
                        // remove the anchor element from the stack of open
                        // elements, if it is still there
                    } else {
                        throw new ParseErrorException();
                    }
                }
                reconstructActiveFormattingElements();
                final Element anchorElement = insertHTMLElement(startTagToken);
                pushOntoListOfActiveFormattingElements(startTagToken, anchorElement);
                return TOKEN_HANDLED;
            } // break;
            case "b": // fall through
            case "big": // fall through
            case "code": // fall through
            case "em": // fall through
            case "font": // fall through
            case "i": // fall through
            case "s": // fall through
            case "small": // fall through
            case "strike": // fall through
            case "strong": // fall through
            case "tt": // fall through
            case "u": {
                reconstructActiveFormattingElements();
                final Element formattingElement = insertHTMLElement(startTagToken);
                pushOntoListOfActiveFormattingElements(startTagToken, formattingElement);
                return TOKEN_HANDLED;
            } // break;
            case "nobr": {
                reconstructActiveFormattingElements();
                if (hasParticularElementInScope("nobr")) {
                    if (isAllowParseErrors()) {
                        adoptionAgencyAlgorithm("nobr");
                        reconstructActiveFormattingElements();
                    } else {
                        throw new ParseErrorException("Attempt to nest nobr elements in body.");
                    }
                }
                final Element nobrElement = insertHTMLElement(startTagToken);
                pushOntoListOfActiveFormattingElements(startTagToken, nobrElement);
                return TOKEN_HANDLED;
            } // break;
            case "applet": // fall through
            case "marquee": // fall through
            case "object": {
                reconstructActiveFormattingElements();
                insertHTMLElement(startTagToken);
                insertMarkerAtEndOfListOfActiveFormattingElements();
                setFramesetOKFlag(NOT_OK);
                return TOKEN_HANDLED;
            } // break;
            case "table": {
                if ( !isQuirksMode() && hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                setFramesetOKFlag(NOT_OK);
                setInsertionMode(Parser.Mode.IN_TABLE);
                return TOKEN_HANDLED;
            } // break;
            case "area": // fall through
            case "br": // fall through
            case "embed": // fall through
            case "img": // fall through
            case "keygen": // fall through
            case "wbr": {
                reconstructActiveFormattingElements();
                insertHTMLElement(startTagToken);
                popCurrentNode();
                acknowledgeTokenSelfClosingFlag(startTagToken);
                setFramesetOKFlag(NOT_OK);
                return TOKEN_HANDLED;
            } // break;
            case "input": {
                reconstructActiveFormattingElements();
                insertHTMLElement(startTagToken);
                popCurrentNode();
                acknowledgeTokenSelfClosingFlag(startTagToken);
                final TagToken.Attribute typeAttribute = getAttributeNamed(startTagToken, "type");
                if (typeAttribute == null || !typeAttribute.getValue().equalsIgnoreCase("hidden")) {
                    setFramesetOKFlag(NOT_OK);
                }
                return TOKEN_HANDLED;
            } // break;
            case "menuitem": // fall through
            case "param": // fall through
            case "source": // fall through
            case "track": {
                insertHTMLElement(startTagToken);
                popCurrentNode();
                acknowledgeTokenSelfClosingFlag(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "hr": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                insertHTMLElement(startTagToken);
                popCurrentNode();
                acknowledgeTokenSelfClosingFlag(startTagToken);
                setFramesetOKFlag(NOT_OK);
                return TOKEN_HANDLED;
            } // break;
            case "image": {
                throw new ParseErrorException("Unrecognized start tag token (did you mean \"img\"?): " + startTagToken);
            } // break;
            case "isindex": {
                if (isAllowParseErrors()) {
                    if ( !isStackOfOpenElementsContains("template") && getFormElementPointer() != null) {
                        return IGNORE_TOKEN;
                    }
                    acknowledgeTokenSelfClosingFlag(startTagToken);
                    setFramesetOKFlag(NOT_OK);
                    if (hasParticularElementInButtonScope("p")) {
                        closePElement();
                    }
                    final Element formElement = insertHTMLElement("form");
                    if ( !isStackOfOpenElementsContains("template")) {
                        setFormElementPointer(formElement);
                    }
                    final TagToken.Attribute actionAttribute = getAttributeNamed(startTagToken, "action");
                    if (actionAttribute != null) {
                        formElement.setAttribute("action", actionAttribute.getValue());
                    }
                    insertHTMLElement("hr");
                    popCurrentNode();
                    reconstructActiveFormattingElements();
                    insertHTMLElement("label");
                    // TODO: localize string below
                    for (final char ch : "This is a searchable index. Enter search keywords: ".toCharArray()) {
                        insertCharacter(ch);
                    }
                    final Element inputElement = insertHTMLElement("input");
                    for (final TagToken.Attribute attr : startTagToken.getAttributes()) {
                        final String name = attr.getName();
                        if ( !name.equals("name") && !name.equals("action") && !name.equals("prompt")) {
                            inputElement.setAttribute(name, attr.getValue());
                        }
                    }
                    inputElement.setAttribute("name", "isindex");
                    popCurrentNode();
                    // TODO: localize string above
                    popCurrentNode();
                    insertHTMLElement("hr");
                    popCurrentNode();
                    popCurrentNode();
                    if ( !isStackOfOpenElementsContains("template")) {
                        setFormElementPointer(null);
                    }
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in body: " + startTagToken);
                }
            } // break;
            case "textarea": {
                insertHTMLElement(startTagToken);
                // if next token is LINE_FEED, ignore it
                setTokenizerState(Tokenizer.State.RCDATA);
                setOriginalInsertionMode(getInsertionMode());
                setFramesetOKFlag(NOT_OK);
                setInsertionMode(Parser.Mode.TEXT);
                return TOKEN_HANDLED;
            } // break;
            case "xmp": {
                if (hasParticularElementInButtonScope("p")) {
                    closePElement();
                }
                reconstructActiveFormattingElements();
                setFramesetOKFlag(NOT_OK);
                genericRawTextElementParsingAlgorithm(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "iframe": {
                setFramesetOKFlag(NOT_OK);
                genericRawTextElementParsingAlgorithm(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "noembed": {
                genericRawTextElementParsingAlgorithm(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "noscript": {
                if (isScriptingEnabled()) {
                    genericRawTextElementParsingAlgorithm(startTagToken);
                    return TOKEN_HANDLED;
                } else {
                    return anyOtherStartTag(startTagToken);
                }
            } // break;
            case "select": {
                reconstructActiveFormattingElements();
                insertHTMLElement(startTagToken);
                setFramesetOKFlag(NOT_OK);
                switch (getInsertionMode()) {
                case IN_TABLE: // fall through
                case IN_CAPTION: // fall through
                case IN_TABLE_BODY: // fall through
                case IN_ROW: // fall through
                case IN_CELL: {
                    setInsertionMode(Parser.Mode.IN_SELECT_IN_TABLE);
                }
                    break;
                default: {
                    setInsertionMode(Parser.Mode.IN_SELECT);
                }
                    break;
                }
                return TOKEN_HANDLED;
            } // break;
            case "optgroup": // fall through
            case "option": {
                if (isElementA(getCurrentNode(), "option")) {
                    popCurrentNode();
                }
                return anyOtherStartTag(startTagToken);
            } // break;
            case "rp": // fall through
            case "rt": {
                if (hasParticularElementInScope("ruby")) {
                    generateImpliedEndTags();
                }
                if (isAllowParseErrors() && !isElementA(getCurrentNode(), "ruby")) {
                    throw new ParseErrorException();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "math": {
                reconstructActiveFormattingElements();
                adjustMathMLAttributes(startTagToken);
                adjustForeignAttributes(startTagToken);
                insertForeignElement(startTagToken, MATHML_NAMESPACE);
                if (startTagToken.isSelfClosing()) {
                    popCurrentNode();
                    acknowledgeTokenSelfClosingFlag(startTagToken);
                }
                return TOKEN_HANDLED;
            } // break;
            case "svg": {
                reconstructActiveFormattingElements();
                adjustSVGAttributes(startTagToken);
                adjustForeignAttributes(startTagToken);
                insertForeignElement(startTagToken, SVG_NAMESPACE);
                if (startTagToken.isSelfClosing()) {
                    popCurrentNode();
                    acknowledgeTokenSelfClosingFlag(startTagToken);
                }
                return TOKEN_HANDLED;
            } // break;
            case "caption": // fall through
            case "col": // fall through
            case "colgroup": // fall through
            case "frame": // fall through
            case "head": // fall through
            case "tbody": // fall through
            case "td": // fall through
            case "tfoot": // fall through
            case "th": // fall through
            case "thead": // fall through
            case "tr": {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in body: " + startTagToken);
                }
            } // break;
            default: {
                return anyOtherStartTag(startTagToken);
            } // break;
            }
        } // break;
        case END_TAG: {
            final EndTagToken endTagToken = (EndTagToken) token;
            final String tagName = endTagToken.getTagName();
            switch (tagName) {
            case "template": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, endTagToken);
            } // break;
            case "body": {
                if ( !hasParticularElementInScope("body")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Body end tag token encountered without body start tag token.");
                    }
                }
                final Set<String> acceptable = new HashSet<>(Arrays.asList("dd", "dt", "li", "optgroup", "option", "p",
                        "rp", "rt", "tbody", "td", "tfoot", "th", "thead", "tr", "body", "html"));
                if (isStackOfOpenElementsContainsOtherThan(acceptable)) {
                    throw new ParseErrorException("Unclosed element at body end.");
                }
//				for (final Element openElement : getStackOfOpenElements()) {
//					if ( !acceptable.contains(openElement.getTagName())) {
//						throw new ParseErrorException("Unclosed element at body end: " + openElement.getTagName());
//					}
//				}
                setInsertionMode(Parser.Mode.AFTER_BODY);
                return TOKEN_HANDLED;
            } // break;
            case "html": {
                if ( !hasParticularElementInScope("body")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Body end tag token encountered without body start tag token.");
                    }
                }
                final Set<String> acceptable = new HashSet<>(Arrays.asList("dd", "dt", "li", "optgroup", "option", "p",
                        "rp", "rt", "tbody", "td", "tfoot", "th", "thead", "tr", "body", "html"));
                if (isStackOfOpenElementsContainsOtherThan(acceptable)) {
                    throw new ParseErrorException("Unclosed element at body end.");
                }
//				for (final Element openElement : getStackOfOpenElements()) {
//					if ( !acceptable.contains(openElement.getTagName())) {
//						throw new ParseErrorException("Unclosed element at html end: " + openElement.getTagName());
//					}
//				}
                setInsertionMode(Parser.Mode.AFTER_BODY);
                return REPROCESS_TOKEN;
            } // break;
            case "address": // fall through
            case "article": // fall through
            case "aside": // fall through
            case "blockquote": // fall through
            case "button": // fall through
            case "center": // fall through
            case "details": // fall through
            case "dialog": // fall through
            case "dir": // fall through
            case "div": // fall through
            case "dl": // fall through
            case "fieldset": // fall through
            case "figcaption": // fall through
            case "figure": // fall through
            case "footer": // fall through
            case "header": // fall through
            case "hgroup": // fall through
            case "listing": // fall through
            case "main": // fall through
            case "menu": // fall through
            case "nav": // fall through
            case "ol": // fall through
            case "pre": // fall through
            case "section": // fall through
            case "summary": // fall through
            case "ul": {
                if ( !hasParticularElementInScope(tagName)) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("End tag token without matching open element: " + endTagToken);
                    }
                }
                generateImpliedEndTags();
                if ( !isElementA(getCurrentNode(), tagName)) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("End tag token does not match current open element ("
                                + getCurrentNode().getTagName() + "): " + endTagToken);
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, tagName));
                return TOKEN_HANDLED;
            } // break;
            case "form": {
                // do wacky shit
                if ( !isStackOfOpenElementsContains("template")) {
                    final Element node = getFormElementPointer();
                    setFormElementPointer(null);
                    if (node == null || !hasParticularElementInScope(node.getTagName())) {
                        if (isAllowParseErrors()) {
                            return IGNORE_TOKEN;
                        } else {
                            throw new ParseErrorException("uhhhh");
                        }
                    }
                    generateImpliedEndTags();
                    if (node != getCurrentNode()) {
                        throw new ParseErrorException();
                    }
                    removeNodeFromStackOfOpenElements(node);
                } else {
                    if ( !hasParticularElementInScope("form")) {
                        if (isAllowParseErrors()) {
                            return IGNORE_TOKEN;
                        } else {
                            throw new ParseErrorException();
                        }
                    }
                    generateImpliedEndTags();
                    if ( !isElementA(getCurrentNode(), "form")) {
                        if (isAllowParseErrors()) {
                            // do nothing?
                        } else {
                            throw new ParseErrorException();
                        }
                    }
                    Element popped;
                    do {
                        popped = popCurrentNode();
                    } while ( !isElementA(popped, "form"));
                }
                return TOKEN_HANDLED;
            } // break;
            case "p": {
                if ( !hasParticularElementInButtonScope("p")) {
                    if (isAllowParseErrors()) {
                        insertHTMLElement("p");
                    } else {
                        throw new ParseErrorException(
                                "End tag token for p element with no matching p element in scope.");
                    }
                }
                closePElement();
                return TOKEN_HANDLED;
            } // break;
            case "li": {
                if ( !hasParticularElementInListItemScope("li")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("End tag token for li with no matching li element in scope.");
                    }
                }
                generateImpliedEndTagsExcept("li");
                if ( !isElementA(getCurrentNode(), "li")) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("End tag token for li element when current element is: "
                                + getCurrentNode().getTagName());
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "li"));
                return TOKEN_HANDLED;
            } // break;
            case "dd": // fall through
            case "dt": {
                if ( !hasParticularElementInListItemScope(tagName)) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "End tag token for " + tagName + " with no matching " + tagName + " element in scope.");
                    }
                }
                generateImpliedEndTagsExcept(tagName);
                if ( !isElementA(getCurrentNode(), tagName)) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("End tag token for " + tagName
                                + " element when current element is: " + getCurrentNode().getTagName());
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, tagName));
                return TOKEN_HANDLED;
            } // break;
            case "h1": // fall through
            case "h2": // fall through
            case "h3": // fall through
            case "h4": // fall through
            case "h5": // fall through
            case "h6": {
                if ( !hasParticularElementInScope("h1") && !hasParticularElementInScope("h2")
                        && !hasParticularElementInScope("h3") && !hasParticularElementInScope("h4")
                        && !hasParticularElementInScope("h5") && !hasParticularElementInScope("h6")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "End tag token for " + tagName + " element with no heading element in scope.");
                    }
                }
                generateImpliedEndTags();
                if ( !isElementA(getCurrentNode(), tagName)) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("End tag token for " + tagName
                                + " element when current element is: " + getCurrentNode().getTagName());
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "h1", "h2", "h3", "h4", "h5", "h6"));
                return TOKEN_HANDLED;
            } // break;
            case "a": // fall through
            case "b": // fall through
            case "big": // fall through
            case "code": // fall through
            case "em": // fall through
            case "font": // fall through
            case "i": // fall through
            case "nobr": // fall through
            case "s": // fall through
            case "small": // fall through
            case "strike": // fall through
            case "strong": // fall through
            case "tt": // fall through
            case "u": {
                adoptionAgencyAlgorithm(tagName);
                return TOKEN_HANDLED;
            } // break;
            case "applet": // fall through
            case "marquee": // fall through
            case "object": {
                if ( !isStackOfOpenElementsContains(tagName)) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Unexpected end tag token in body: " + endTagToken);
                    }
                }
                generateImpliedEndTags();
                if ( !isElementA(getCurrentNode(), tagName)) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException();
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, tagName));
                clearListOfActiveFormattingElementsUpToLastMarker();
                return TOKEN_HANDLED;
            } // break;
            case "br": {
                if (isAllowParseErrors()) {
                    reconstructActiveFormattingElements();
                    insertHTMLElement("br");
                    popCurrentNode();
                    setFramesetOKFlag(NOT_OK);
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException();
                }
            } // break;
            default: {
                return anyOtherEndTag(tagName);
            } // break;
            }
        } // break;
        case EOF: {
            // handle stack of open elements
            final Set<String> acceptable = new HashSet<>(
                    Arrays.asList("dd", "dt", "li", "p", "tbody", "td", "tfoot", "th", "thead", "tr", "body", "html"));
            if (isStackOfOpenElementsContainsOtherThan(acceptable)) {
                if (isAllowParseErrors()) {
                    // do nothing?
                } else {
                    throw new ParseErrorException("Unclosed element at end-of-file.");
                }
            }
//			for (final Element openElement : getStackOfOpenElements()) {
//				if ( !acceptable.contains(openElement.getTagName())) {
//					if (isAllowParseErrors()) {
//						// do nothing?
//					} else {
//						throw new ParseErrorException("Unclosed element at end-of-file: " + openElement.getTagName());
//					}
//				}
//			}
            if ( !isStackOfTemplateInsertionModesEmpty()) {
                return processUsingRulesFor(Parser.Mode.IN_TEMPLATE, token);
            }
            stopParsing();
            return TOKEN_HANDLED;
        } // break;
        default: {
            throw new ParseErrorException();
        } // break;
        }
    }
    
    private boolean anyOtherStartTag(final StartTagToken startTagToken) {
        reconstructActiveFormattingElements();
        insertHTMLElement(startTagToken);
        return TOKEN_HANDLED;
    }
    
    private boolean anyOtherEndTag(final String tagName) {
        int index = getStackOfOpenElementsSize() - 1;
        while (index >= 0) {
            final Element node = getOpenElement(index);
            if (isElementA(node, tagName)) {
                generateImpliedEndTagsExcept(tagName);
                if (node != getCurrentNode() && !isAllowParseErrors()) {
                    throw new ParseErrorException();
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while (popped != node);
                return TOKEN_HANDLED;
            } else {
                if (isSpecialCategory(node)) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException();
                    }
                } else {
                    // loop
                    index-- ;
                }
            }
        }
        return TOKEN_HANDLED;
    }
    
    /**
     * @param subject
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#adoption-agency-algorithm">
     *      adoption agency algorithm</a>
     */
    protected void adoptionAgencyAlgorithm(final String subject) {
        // 1
        final Element currentNode = getCurrentNode();
        if (isElementA(currentNode, subject) && !listOfActiveFormattingElementsContains(currentNode)) {
            popCurrentNode();
            return;
        }
        // 2
//		int outerLoopCounter = 0;
        // 3
        // OUTER LOOP:
        for (int outerLoopCounter = 0; outerLoopCounter < 8; outerLoopCounter++ ) {
//		if (outerLoopCounter >= 8) {
//			return;
//		}
            // 4
//		outerLoopCounter++;
            // 5
            int formattingElementIndexInList = parser.listOfActiveFormattingElements.size() - 1;
            StartTagToken formattingElementToken = null;
            Element formattingElement = null;
            while (formattingElementIndexInList >= 0) {
                final FormattingElement temp = parser.listOfActiveFormattingElements.get(formattingElementIndexInList);
                if (parser.isMarker(temp)) {
                    break;
                }
                final StartTagToken tok = temp.getKey();
                final Element el = temp.getValue();
                if (isElementA(el, subject)) {
                    formattingElementToken = tok;
                    formattingElement = el;
                    break;
                }
                formattingElementIndexInList-- ;
            }
            if (formattingElement == null) {
                anyOtherEndTag(subject);
                return;
            }
            // 6
            final int formattingElementIndexInStack = parser.getIndexOfOpenElement(formattingElement);
            if (formattingElementIndexInStack == -1) {
                if (isAllowParseErrors()) {
                    assert parser.listOfActiveFormattingElements.get(formattingElementIndexInList) == formattingElement;
                    parser.listOfActiveFormattingElements.remove(formattingElementIndexInList);
                    return;
                } else {
                    throw new ParseErrorException();
                }
            }
            // 7
            assert parser.containsOpenElement(formattingElement);
            if ( !hasParticularElementInScope(subject)) {
                if (isAllowParseErrors()) {
                    return;
                } else {
                    throw new ParseErrorException();
                }
            }
            // 8
            if (formattingElement != getCurrentNode() && !isAllowParseErrors()) {
                throw new ParseErrorException();
            }
            // 9
            Element furthestBlock = null;
            int furthestBlockIndex = formattingElementIndexInStack + 1;
            while (furthestBlockIndex < parser.getNumOpenElements()) {
                final Element temp = parser.getOpenElement(furthestBlockIndex);
                if (isSpecialCategory(temp)) {
                    furthestBlock = temp;
                    break;
                }
                furthestBlockIndex++ ;
            }
            // 10
            if (furthestBlock == null) {
                Element popped;
                do {
                    popped = popCurrentNode();
                } while (popped != formattingElement);
                parser.listOfActiveFormattingElements.remove(formattingElementIndexInList);
                return;
            }
            // 11
            final int commonAncestorIndex = formattingElementIndexInStack - 1;
            final Element commonAncestor = parser.getOpenElement(commonAncestorIndex);
            // 12
            int bookmark = indexOfInActiveList(formattingElement);
            FormattingElement beforeBookmark;
            if (bookmark > 0) {
                beforeBookmark = parser.listOfActiveFormattingElements.get(bookmark - 1);
            } else {
                beforeBookmark = null;
            }
            FormattingElement afterBookmark;
            if (bookmark + 1 < parser.listOfActiveFormattingElements.size()) {
                afterBookmark = parser.listOfActiveFormattingElements.get(bookmark + 1);
            } else {
                afterBookmark = null;
            }
            // 13
            int nodeIndex = furthestBlockIndex;
            int lastNodeIndex = furthestBlockIndex;
            Element lastNode = parser.getOpenElement(lastNodeIndex);
            // 13.1
            int innerLoopCounter = 0;
            // 13.2
            // INNER LOOP:
            while (true) {
                innerLoopCounter++ ;
                // 13.3
                nodeIndex-- ;
                Element node = parser.getOpenElement(nodeIndex);
                // 13.4
                if (node == formattingElement) {
                    // go to 14:
                    break;
                }
                // 13.5
                if (innerLoopCounter > 3 && listOfActiveFormattingElementsContains(node)) {
                    removeFromListOfActiveFormattingElements(node);
                }
                // 13.6
                if ( !listOfActiveFormattingElementsContains(node)) {
                    parser.removeOpenElement(node);
                    // go to INNER LOOP:
                    continue;
                }
                // 13.7
                int indexInFormattingList = indexOfInActiveList(node);
                final FormattingElement wrapped = parser.listOfActiveFormattingElements.get(indexInFormattingList);
                final StartTagToken nodeToken = wrapped.getKey();
                final Element newElement = createElementForToken(nodeToken, HTML_NAMESPACE, commonAncestor);
                final FormattingElement wrapper = new FormattingElement(nodeToken, newElement);
                indexInFormattingList = indexOfInActiveList(node);
                parser.listOfActiveFormattingElements.set(indexInFormattingList, wrapper);
                parser.setOpenElement(nodeIndex, newElement);
                node = newElement;
                // 13.8
                if (lastNode == furthestBlock) {
                    // TODO: bookmark moved after node in
                    // parser.listOfActiveFormattingElements
                    bookmark = indexInFormattingList;
                    if (bookmark > 0) {
                        beforeBookmark = parser.listOfActiveFormattingElements.get(bookmark - 1);
                    } else {
                        beforeBookmark = null;
                    }
                    if (bookmark + 1 < parser.listOfActiveFormattingElements.size()) {
                        afterBookmark = parser.listOfActiveFormattingElements.get(bookmark + 1);
                    } else {
                        afterBookmark = null;
                    }
                }
                // 13.9
                node.appendChild(lastNode);
                // 13.10
                lastNodeIndex = nodeIndex;
                lastNode = node;
                // 13.11
                // go to INNER LOOP:
            }
            // 14
            getAppropriatePlaceForInsertingNode(commonAncestor).insert(lastNode);
            // 15
            final Element thatNewElement = createElementForToken(formattingElementToken, HTML_NAMESPACE, furthestBlock);
            // 16
            final NodeList childrenToMove = furthestBlock.getChildNodes();
            for (int i = 0; i < childrenToMove.getLength(); i++ ) {
                thatNewElement.appendChild(childrenToMove.item(i));
            }
            // 17
            furthestBlock.appendChild(thatNewElement);
            // 18
            removeFromListOfActiveFormattingElements(formattingElement);
            // insert thatNewElement at bookmark
            final FormattingElement thatNewWrapper = new FormattingElement(formattingElementToken, thatNewElement);
            if (beforeBookmark != null) {
                final int indexOfBefore = parser.listOfActiveFormattingElements.indexOf(beforeBookmark);
                parser.listOfActiveFormattingElements.add(indexOfBefore + 1, thatNewWrapper);
            } else {
                final int indexOfAfter = parser.listOfActiveFormattingElements.indexOf(afterBookmark);
                parser.listOfActiveFormattingElements.add(indexOfAfter, thatNewWrapper);
            }
            // 19
            parser.removeOpenElement(formattingElement);
            furthestBlockIndex = parser.getIndexOfOpenElement(furthestBlock);
            parser.insertOpenElement(furthestBlockIndex, thatNewElement);
            // 20
            // go to OUTER LOOP:
        }
    }
    
    private int indexOfInActiveList(final Element formattingElement) {
        for (int i = 0; i < parser.listOfActiveFormattingElements.size(); i++ ) {
            final FormattingElement wrapper = parser.listOfActiveFormattingElements.get(i);
            if (parser.isMarker(wrapper)) {
                continue;
            }
            final Element element = wrapper.getValue();
            if (element == formattingElement) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean listOfActiveFormattingElementsContains(final Element formattingElement) {
        for (final FormattingElement wrapper : parser.listOfActiveFormattingElements) {
            if (parser.isMarker(wrapper)) {
                continue;
            }
            final Element element = wrapper.getValue();
            if (element == formattingElement) {
                return true;
            }
        }
        return false;
    }
    
    private void removeFromListOfActiveFormattingElements(final Element formattingElement) {
        final Iterator<FormattingElement> iterator = parser.listOfActiveFormattingElements.iterator();
        while (iterator.hasNext()) {
            final FormattingElement wrapper = iterator.next();
            if (parser.isMarker(wrapper)) {
                continue;
            }
            final Element element = wrapper.getValue();
            if (formattingElement == element) {
                iterator.remove();
//				return;
            }
        }
    }
    
    /**
     * @param target
     * @see <a href=
     *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#close-a-p-element">
     *      close a p element</a>
     */
    private void closePElement() {
        generateImpliedEndTagsExcept("p");
        if ( !getCurrentNode().getTagName().equals("p")) {
            if (isAllowParseErrors()) {
                // do nothing?
            } else {
                throw new ParseErrorException("Expected current node to be a p element when closing element, was: "
                        + getCurrentNode().getTagName());
            }
        }
        Element popped;
        do {
            popped = popCurrentNode();
        } while ( !isElementA(popped, "p"));
    }
    
}
