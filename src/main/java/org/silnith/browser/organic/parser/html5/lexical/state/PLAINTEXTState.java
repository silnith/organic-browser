package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndOfFileToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * Currently unused.
 * 
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#plaintext-state">
 *      12.2.4.7 PLAINTEXT state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PLAINTEXTState extends TokenizerState {
    
    public PLAINTEXTState(final Tokenizer tokenizer) {
        super(tokenizer);
    }
    
    @Override
    public int getMaxPushback() {
        return 0;
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final int ch = consume();
        switch (ch) {
        case NULL: {
            if (isAllowParseErrors()) {
                return one(new CharacterToken(REPLACEMENT_CHARACTER));
            } else {
                throw new ParseErrorException("Null character.");
            }
        } // break;
        case EOF: {
            return one(new EndOfFileToken());
        } // break;
        default: {
            return one(new CharacterToken((char) ch));
        } // break;
        }
    }
    
}
