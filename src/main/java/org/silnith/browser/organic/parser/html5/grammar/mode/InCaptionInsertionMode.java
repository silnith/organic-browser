package org.silnith.browser.organic.parser.html5.grammar.mode;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-incaption">
 *      12.2.5.4.11 The "in caption" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InCaptionInsertionMode extends InsertionMode {
    
    public InCaptionInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "caption": // fall through
            case "col": // fall through
            case "colgroup": // fall through
            case "tbody": // fall through
            case "td": // fall through
            case "tfoot": // fall through
            case "th": // fall through
            case "thead": // fall through
            case "tr": {
                if ( !hasParticularElementInTableScope("caption")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "Unexpected start tag token outside of caption element in table scope: "
                                        + startTagToken);
                    }
                }
                generateImpliedEndTags();
                if (isElementA(getCurrentNode(), "caption")) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("Expected current node to be a caption element.");
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "caption"));
                clearListOfActiveFormattingElementsUpToLastMarker();
                setInsertionMode(Parser.Mode.IN_TABLE);
                return REPROCESS_TOKEN;
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
            case "caption": {
                if ( !hasParticularElementInTableScope("caption")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "Unexpected end tag token outside of caption element in table scope: " + endTagToken);
                    }
                }
                generateImpliedEndTags();
                if (isElementA(getCurrentNode(), "caption")) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("Expected current node to be a caption element.");
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "caption"));
                clearListOfActiveFormattingElementsUpToLastMarker();
                setInsertionMode(Parser.Mode.IN_TABLE);
                return TOKEN_HANDLED;
            } // break;
            case "table": {
                if ( !hasParticularElementInTableScope("caption")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException(
                                "Unexpected end tag token outside of caption element in table scope: " + endTagToken);
                    }
                }
                generateImpliedEndTags();
                if (isElementA(getCurrentNode(), "caption")) {
                    if (isAllowParseErrors()) {
                        // do nothing?
                    } else {
                        throw new ParseErrorException("Expected current node to be a caption element.");
                    }
                }
                Element popped;
                do {
                    popped = popCurrentNode();
                } while ( !isElementA(popped, "caption"));
                clearListOfActiveFormattingElementsUpToLastMarker();
                setInsertionMode(Parser.Mode.IN_TABLE);
                return REPROCESS_TOKEN;
            } // break;
            case "body": // fall through
            case "col": // fall through
            case "colgroup": // fall through
            case "html": // fall through
            case "tbody": // fall through
            case "td": // fall through
            case "tfoot": // fall through
            case "th": // fall through
            case "thead": // fall through
            case "tr": {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected end tag token in caption: " + endTagToken);
                }
            } // break;
            default: {
                return anythingElse(endTagToken);
            } // break;
            }
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        return processUsingRulesFor(Parser.Mode.IN_BODY, token);
    }
    
}
