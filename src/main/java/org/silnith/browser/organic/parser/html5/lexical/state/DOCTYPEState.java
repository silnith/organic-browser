package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#doctype-state">
 *      12.2.4.52 DOCTYPE state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DOCTYPEState extends TokenizerState {
    
    public DOCTYPEState(final Tokenizer tokenizer) {
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
        case CHARACTER_TABULATION: // fall through
        case LINE_FEED: // fall through
        case FORM_FEED: // fall through
        case SPACE: {
            setTokenizerState(Tokenizer.State.BEFORE_DOCTYPE_NAME);
            return NOTHING;
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                final DOCTYPEToken doctypeToken = new DOCTYPEToken();
                doctypeToken.setForceQuirks();
                return one(doctypeToken);
            } else {
                throw new ParseErrorException("Unexpected end-of-file in DOCTYPE.");
            }
        } // break;
        default: {
            if (isAllowParseErrors()) {
                unconsume(ch);
                setTokenizerState(Tokenizer.State.BEFORE_DOCTYPE_NAME);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected character in DOCTYPE: " + (char) ch);
            }
        } // break;
        }
    }
    
}
