package org.silnith.browser.organic.parser.html5.grammar.mode;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.grammar.Parser;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tree-construction.html#parsing-main-intabletext">
 *      12.2.5.4.10 The "in table text" insertion mode</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class InTableTextInsertionMode extends InsertionMode {
    
    public InTableTextInsertionMode(final Parser parser) {
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
                    throw new ParseErrorException("Null character in table text.");
                }
            } // break;
            default: {
                appendToPendingTableCharacterTokens(characterToken);
                return TOKEN_HANDLED;
            } // break;
            }
        } // break;
        default: {
            return anythingElse(token);
        } // break;
        }
    }
    
    private boolean anythingElse(final Token token) {
        // if pending table character tokens contains non-space characters
        if (isPendingTableCharacterTokensListContainsCharactersThatAreNotSpaceCharacters()) {
            // reprocess the pending table character tokens according to the
            // "anything else" case of the IN_TABLE mode
            if (isAllowParseErrors()) {
                enableFosterParenting();
                final boolean returnValue = processUsingRulesFor(Parser.Mode.IN_BODY, token);
                disableFosterParenting();
                return returnValue;
            } else {
                throw new ParseErrorException("Unexpected token in table text: " + token);
            }
        }
        for (final CharacterToken characterToken : getPendingTableCharacterTokens()) {
            insertCharacter(characterToken);
        }
        /*
         * The "spec" does not actually say to clear the pending table character
         * token list at this point. I am assuming it was implied.
         */
        setPendingTableCharacterTokens();
        setInsertionMode(getOriginalInsertionMode());
        return REPROCESS_TOKEN;
    }
    
}
