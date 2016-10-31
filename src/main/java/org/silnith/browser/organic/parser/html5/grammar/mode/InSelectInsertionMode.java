package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inselect">
 *      12.2.5.4.16 The "in select" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InSelectInsertionMode extends InsertionMode {
    
    public InSelectInsertionMode(final Parser parser) {
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
                    throw new ParseErrorException("Null character in select.");
                }
            } // break;
            default: {
                insertCharacter(character);
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
                throw new ParseErrorException("Unexpected DOCTYPE token in select: " + token);
            }
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "html": {
                return processUsingRulesFor(Parser.Mode.IN_BODY, startTagToken);
            } // break;
            case "option": {
                if (isElementA(getCurrentNode(), "option")) {
                    popCurrentNode();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "optgroup": {
                if (isElementA(getCurrentNode(), "option")) {
                    popCurrentNode();
                }
                if (isElementA(getCurrentNode(), "optgroup")) {
                    popCurrentNode();
                }
                insertHTMLElement(startTagToken);
                return TOKEN_HANDLED;
            } // break;
            case "select": {
                if (isAllowParseErrors()) {
                    Element popped;
                    do {
                        popped = popCurrentNode();
                    } while ( !isElementA(popped, "select"));
                    resetInsertionModeAppropriately();
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in select: " + startTagToken);
                }
            } // break;
            case "input": // fall through
            case "keygen": // fall through
            case "textarea": {
                if (isAllowParseErrors()) {
                    if ( !hasParticularElementInSelectScope("select")) {
                        return IGNORE_TOKEN;
                    }
                    Element popped;
                    do {
                        popped = popCurrentNode();
                    } while ( !isElementA(popped, "select"));
                    resetInsertionModeAppropriately();
                    return REPROCESS_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected start tag token in select: " + startTagToken);
                }
            } // break;
            case "template": // fall through
            case "script": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, startTagToken);
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
            case "optgroup": {
                if (isElementA(getCurrentNode(), "option")
                        && isElementA(getNodeImmediatelyBeforeCurrentNode(), "optgroup")) {
                    popCurrentNode();
                }
                if (isElementA(getCurrentNode(), "optgroup")) {
                    popCurrentNode();
                    return TOKEN_HANDLED;
                } else {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Expected current node to be an optgroup element, instead was: "
                                + getCurrentNode().getTagName());
                    }
                }
            } // break;
            case "option": {
                if (isElementA(getCurrentNode(), "option")) {
                    popCurrentNode();
                    return TOKEN_HANDLED;
                } else {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Expected current node to be an option element, instead was: "
                                + getCurrentNode().getTagName());
                    }
                }
            } // break;
            case "select": {
                // verify stack of open elements has select element in select
                // scope
                if ( !hasParticularElementInSelectScope("select")) {
                    if (isAllowParseErrors()) {
                        return IGNORE_TOKEN;
                    } else {
                        throw new ParseErrorException("Expected select element in select scope.");
                    }
                }
                Element poppedElement;
                do {
                    poppedElement = popCurrentNode();
                } while ( !isElementA(poppedElement, "select"));
                resetInsertionModeAppropriately();
                return TOKEN_HANDLED;
            } // break;
            case "template": {
                return processUsingRulesFor(Parser.Mode.IN_HEAD, endTagToken);
            } // break;
            default: {
                return anythingElse(endTagToken);
            } // break;
            }
        } // break;
        case EOF: {
            return processUsingRulesFor(Parser.Mode.IN_BODY, token);
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        if (isAllowParseErrors()) {
            return true;
        } else {
            throw new ParseErrorException("Unexpected token in select: " + token);
        }
    }
    
}
