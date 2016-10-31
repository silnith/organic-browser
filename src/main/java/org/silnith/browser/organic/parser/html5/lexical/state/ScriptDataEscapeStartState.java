package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#script-data-escape-start-state">
 *      12.2.4.20 Script data escape start state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ScriptDataEscapeStartState extends TokenizerState {
    
    public ScriptDataEscapeStartState(final Tokenizer tokenizer) {
        super(tokenizer);
    }
    
    @Override
    public int getMaxPushback() {
        return 1;
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final int ch = consume();
        switch (ch) {
        case HYPHEN_MINUS: {
            setTokenizerState(Tokenizer.State.SCRIPT_DATA_ESCAPE_START_DASH);
            return one(new CharacterToken(HYPHEN_MINUS));
        } // break;
        default: {
            unconsume(ch);
            setTokenizerState(Tokenizer.State.SCRIPT_DATA);
            return NOTHING;
        } // break;
        }
    }
    
}
