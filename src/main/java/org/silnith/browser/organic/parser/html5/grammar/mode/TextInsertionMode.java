package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;
import org.w3c.dom.Element;


/**
 * @see <a href="https://www.w3.org/TR/html5/syntax.html#parsing-main-incdata">8.2.5.4.8 The "text" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class TextInsertionMode extends InsertionMode {
    
    public TextInsertionMode(final Parser parser) {
        super(parser);
    }
    
    @Override
    public boolean insert(final Token token) {
        switch (token.getType()) {
        case CHARACTER: {
            final CharacterToken characterToken = (CharacterToken) token;
            assert characterToken.getCharacter() != NULL;
            insertCharacter(characterToken);
            return TOKEN_HANDLED;
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                // if current element is a script, mark it as "already started"
                popCurrentNode();
                setInsertionMode(getOriginalInsertionMode());
                return REPROCESS_TOKEN;
            } else {
                throw new ParseErrorException("Unexpected end-of-file in text inserting mode.");
            }
        } // break;
        case END_TAG: {
            final EndTagToken endTagToken = (EndTagToken) token;
            final String tagName = endTagToken.getTagName();
            switch (tagName) {
            case "script": {
                // do lots and lots and lots of shit
                // if stack of script settings is empty, perform a microtask
                // checkpoint
                // if stack of script settings is empty, provide a stable state
                final Element script = getCurrentNode();
                assert isElementA(script, "script");
                popCurrentNode();
                setInsertionMode(getOriginalInsertionMode());
//				InsertionPosition oldInsertionPosition = current insertion position
                // increment script nesting level
                // prepare script
                // decrement script nesting level
                // set insertion point to old insertion point
                // if pending parsing-blocking script, do stuff
                return TOKEN_HANDLED;
            } // break;
            default: {
                popCurrentNode();
                setInsertionMode(getOriginalInsertionMode());
                return TOKEN_HANDLED;
            } // break;
            }
        } // break;
        default: {
            return defaultCase(token);
        } // break;
        }
    }
    
    private boolean defaultCase(final Token token) {
        throw new ParseErrorException("Unexpected token in text insertion mode: " + token);
//		return false;
    }
    
}
