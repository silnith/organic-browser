package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken.Attribute;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-inforeign">
 *      12.2.5.5 The rules for parsing tokens in foreign content</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ForeignContentMode extends InsertionMode {
    
    public ForeignContentMode(final Parser parser) {
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
                    insertCharacter(REPLACEMENT_CHARACTER);
                    return TOKEN_HANDLED;
                } else {
                    throw new ParseErrorException("Null character in foreign content.");
                }
            } // break;
            case CHARACTER_TABULATION: // fall through
            case LINE_FEED: // fall through
            case FORM_FEED: // fall through
            case CARRIAGE_RETURN: // fall through
            case SPACE: {
                insertCharacter(character);
                return TOKEN_HANDLED;
            } // break;
            default: {
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
                throw new ParseErrorException("Unexpected DOCTYPE token in foreign content: " + token);
            }
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "b": // fall through
            case "big": // fall through
            case "blockquote": // fall through
            case "body": // fall through
            case "br": // fall through
            case "center": // fall through
            case "code": // fall through
            case "dd": // fall through
            case "div": // fall through
            case "dl": // fall through
            case "dt": // fall through
            case "em": // fall through
            case "embed": // fall through
            case "h1": // fall through
            case "h2": // fall through
            case "h3": // fall through
            case "h4": // fall through
            case "h5": // fall through
            case "h6": // fall through
            case "head": // fall through
            case "hr": // fall through
            case "i": // fall through
            case "img": // fall through
            case "li": // fall through
            case "listing": // fall through
            case "menu": // fall through
            case "meta": // fall through
            case "nobr": // fall through
            case "ol": // fall through
            case "p": // fall through
            case "pre": // fall through
            case "ruby": // fall through
            case "s": // fall through
            case "small": // fall through
            case "span": // fall through
            case "strong": // fall through
            case "strike": // fall through
            case "sub": // fall through
            case "sup": // fall through
            case "table": // fall through
            case "tt": // fall through
            case "u": // fall through
            case "ul": // fall through
            case "var": {
                throw new ParseErrorException();
            } // break;
            case "font": {
                final Attribute colorAttribute = getAttributeNamed(startTagToken, "color");
                final Attribute faceAttribute = getAttributeNamed(startTagToken, "face");
                final Attribute sizeAttribute = getAttributeNamed(startTagToken, "size");
                if (colorAttribute != null || faceAttribute != null || sizeAttribute != null) {
                    throw new ParseErrorException();
                } else {
                    return anyOtherStartTag(startTagToken);
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
            case "script": {
                // if isElementA(getCurrentNode(), "script", SVG_NAMESPACE)
                // then do lots of stuff
                // else
                return anyOtherEndTag(endTagToken);
            } // break;
            default: {
                return anyOtherEndTag(endTagToken);
            } // break;
            }
        } // break;
        default: {
        }
            break;
        }
        return false;
    }
    
    private boolean anyOtherStartTag(final StartTagToken startTagToken) {
        return false;
    }
    
    private boolean anyOtherEndTag(final EndTagToken endTagToken) {
        return false;
    }
    
}
