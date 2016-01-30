package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SOLIDUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#after-attribute-value-%28quoted%29-state">
 *      12.2.4.42 After attribute value (quoted) state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterAttributeValueQuotedState extends TokenizerState {
    
    public AfterAttributeValueQuotedState(final Tokenizer tokenizer) {
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
            setTokenizerState(Tokenizer.State.BEFORE_ATTRIBUTE_NAME);
            return NOTHING;
        } // break;
        case SOLIDUS: {
            setTokenizerState(Tokenizer.State.SELF_CLOSING_START_TAG);
            return NOTHING;
        } // break;
        case GREATER_THAN_SIGN: {
            setTokenizerState(Tokenizer.State.DATA);
            final TagToken pendingToken = clearPendingTag();
            return one(pendingToken);
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected end-of-file after quoted attribute value.");
            }
        } // break;
        default: {
            if (isAllowParseErrors()) {
                unconsume(ch);
                setTokenizerState(Tokenizer.State.BEFORE_ATTRIBUTE_NAME);
                return NOTHING;
            } else {
                throw new ParseErrorException("Illegal character after quoted attribute value: " + (char) ch);
            }
        } // break;
        }
    }
    
}
