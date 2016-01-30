package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SOLIDUS;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#rcdata-less-than-sign-state">
 *      12.2.4.11 RCDATA less-than sign state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class RCDATALessThanSignState extends TokenizerState {
    
    public RCDATALessThanSignState(final Tokenizer tokenizer) {
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
        case SOLIDUS: {
            createTemporaryBuffer();
            setTokenizerState(Tokenizer.State.RCDATA_END_TAG_OPEN);
            return NOTHING;
        } // break;
        default: {
            unconsume(ch);
            setTokenizerState(Tokenizer.State.RCDATA);
            return one(new CharacterToken(LESS_THAN_SIGN));
        } // break;
        }
    }
    
}
