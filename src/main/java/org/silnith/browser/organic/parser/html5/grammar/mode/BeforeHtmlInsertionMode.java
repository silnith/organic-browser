package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CARRIAGE_RETURN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.grammar.dom.AfterLastChildInsertionPosition;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.StartTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#the-before-html-insertion-mode">
 *      12.2.5.4.2 The "before html" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BeforeHtmlInsertionMode extends InsertionMode {
    
    public BeforeHtmlInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case DOCTYPE: {
            if (isAllowParseErrors()) {
                return IGNORE_TOKEN;
            } else {
                throw new ParseErrorException("Unexpected DOCTYPE token before html: " + token);
            }
        } // break;
        case COMMENT: {
            final CommentToken commentToken = (CommentToken) token;
            insertComment(commentToken, new AfterLastChildInsertionPosition(getDocument()));
            return TOKEN_HANDLED;
        } // break;
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            final char character = characterToken.getCharacter();
            switch (character) {
            case CHARACTER_TABULATION: // fall through
            case LINE_FEED: // fall through
            case FORM_FEED: // fall through
            case CARRIAGE_RETURN: // fall through
            case SPACE: {
                return IGNORE_TOKEN;
            } // break;
            default: {
                return anythingElse(characterToken);
            } // break;
            }
        } // break;
        case START_TAG: {
            final StartTagToken startTagToken = (StartTagToken) token;
            final String tagName = startTagToken.getTagName();
            switch (tagName) {
            case "html": {
                // The correct algorithm, DOM throws up.
//				final Element htmlElement = createElementForToken(startTagToken, HTML_NAMESPACE, getDocument());
//				getDocument().appendChild(htmlElement);
//				addToStackOfOpenElements(htmlElement);
//				setInsertionMode(Parser.Mode.BEFORE_HEAD);
                
                final Element htmlElement = createHtmlElement();
                for (final TagToken.Attribute attribute : startTagToken.getAttributes()) {
                    final String name = attribute.getName();
                    final String value = attribute.getValue();
                    htmlElement.setAttribute(name, value);
                }
                return TOKEN_HANDLED;
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
            case "head": // fall through
            case "body": // fall through
            case "html": // fall through
            case "br": {
                return anythingElse(endTagToken);
            } // break;
            default: {
                if (isAllowParseErrors()) {
                    return IGNORE_TOKEN;
                } else {
                    throw new ParseErrorException("Unexpected end tag before html: " + endTagToken);
                }
            } // break;
            }
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private Element createHtmlElement() {
        final Document document = getDocument();
//		final Element htmlElement = document.createElementNS(HTML_NAMESPACE, "html");
        final Element htmlElement = document.getDocumentElement();
//		document.appendChild(htmlElement);
        addToStackOfOpenElements(htmlElement);
        setInsertionMode(Parser.Mode.BEFORE_HEAD);
        return htmlElement;
    }
    
    private boolean anythingElse(final Token token) {
        createHtmlElement();
        return REPROCESS_TOKEN;
    }
    
}
