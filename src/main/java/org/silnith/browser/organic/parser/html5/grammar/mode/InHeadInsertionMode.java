package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.grammar.dom.InsertionPosition;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inhead">
 *      12.2.5.4.4 The "in head" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InHeadInsertionMode extends InsertionMode {
    
    public InHeadInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            final char character = characterToken.getCharacter();
            switch (character) {
            case CHARACTER_TABULATION: // fall through
            case LINE_FEED: // fall through
            case FORM_FEED: // fall through
            case CARRIAGE_RETURN: // fall through
            case SPACE: {
                insertCharacter(character);
                return TOKEN_HANDLED;
            } // break;
            default: {
                return anythingElse(characterToken);
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
                throw new ParseErrorException("Unexpected DOCTYPE in head: " + token);
            }
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "html": {
                return processUsingRulesFor(Parser.Mode.IN_BODY, startTagToken);
            } // break;
            case "base": // fall through
            case "basefont": // fall through
            case "bgsound": // fall through
            case "link": {
                final Element element = insertHTMLElement(startTagToken);
                final Element popped = popCurrentNode();
                assert element == popped;
                acknowledgeTokenSelfClosingFlag(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "meta": {
                final Element element = insertHTMLElement(startTagToken);
                final Element popped = popCurrentNode();
                assert element == popped;
                acknowledgeTokenSelfClosingFlag(startTagToken);
                // handle charset special behavior
                return TOKEN_HANDLED;
            } // break;
            case "title": {
                genericRCDATAElementParsingAlgorithm(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "noframes": // fall through
            case "style": {
                genericRawTextElementParsingAlgorithm(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "noscript": {
                if (isScriptingEnabled()) {
                    genericRawTextElementParsingAlgorithm(startTagToken);
                    return TOKEN_HANDLED;
                } else {
                    insertHTMLElement(startTagToken);
                    setInsertionMode(Parser.Mode.IN_HEAD_NOSCRIPT);
                    return TOKEN_HANDLED;
                }
            } // break;
            case "script": {
                final InsertionPosition adjustedInsertionLocation = getAppropriatePlaceForInsertingNode();
                final Element element = createElementForToken(startTagToken, HTML_NAMESPACE,
                        adjustedInsertionLocation.getContainingNode());
                // mark as parser-inserted
                // unset "force-async" flag
                // if fragment, "already started"
                adjustedInsertionLocation.insert(element);
                addToStackOfOpenElements(element);
                setTokenizerState(Tokenizer.State.SCRIPT_DATA);
                setOriginalInsertionMode(getInsertionMode());
                setInsertionMode(Parser.Mode.TEXT);
                return TOKEN_HANDLED;
            } // break;
            case "template": {
                // do stuff
                insertHTMLElement(startTagToken);
                insertMarkerAtEndOfListOfActiveFormattingElements();
                setFramesetOKFlag(NOT_OK);
                setInsertionMode(Parser.Mode.IN_TEMPLATE);
                pushOntoStackOfTemplateInsertionModes(Parser.Mode.IN_TEMPLATE);
                return TOKEN_HANDLED;
            } // break;
            case "head": {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in head: " + startTagToken);
                }
            } // break;
            default: {
                return anythingElse(startTagToken);
            } // break;
            }
        } // break;
        case END_TAG: {
            final EndTagToken endTagToken = (EndTagToken) token;
            final String tagName = endTagToken.getTagName();
            switch (tagName) {
            case "head": {
                final Element head = popCurrentNode();
                assert isElementA(head, "head");
                setInsertionMode(Parser.Mode.AFTER_HEAD);
                return TOKEN_HANDLED;
            } // break;
            case "body": // fall through
            case "html": // fall through
            case "br": {
                return anythingElse(endTagToken);
            } // break;
            case "template": {
                // confirm stack of open elements contains a "template"
                if ( !isStackOfOpenElementsContains("template")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "Expected to find a template element on the stack of open elements.");
                    }
                }
                generateImpliedEndTags();
                if (isAllowParseErrors() && !isElementA(getCurrentNode(), "template")) {
                    throw new ParseErrorException(
                            "Expected current element to be a template element, was: " + getCurrentNode().getTagName());
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "template"));
                clearListOfActiveFormattingElementsUpToLastMarker();
                popCurrentTemplateInsertionMode();
                resetInsertionModeAppropriately();
                return TOKEN_HANDLED;
            } // break;
            default: {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected end tag token in head: " + endTagToken);
                }
            } // break;
            }
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        final Element popped = popCurrentNode();
        assert isElementA(popped, "head");
        setInsertionMode(Parser.Mode.AFTER_HEAD);
        return REPROCESS_TOKEN;
    }
    
}
