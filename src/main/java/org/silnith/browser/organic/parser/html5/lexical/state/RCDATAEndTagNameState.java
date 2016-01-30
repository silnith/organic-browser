package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SOLIDUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#rcdata-end-tag-name-state">
 *      12.2.4.13 RCDATA end tag name state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class RCDATAEndTagNameState extends TokenizerState {
    
    public RCDATAEndTagNameState(final Tokenizer tokenizer) {
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
            if (isAppropriateEndTag()) {
                setTokenizerState(Tokenizer.State.BEFORE_ATTRIBUTE_NAME);
                return NOTHING;
            } else {
                return defaultCase(ch);
            }
        } // break;
        case SOLIDUS: {
            if (isAppropriateEndTag()) {
                setTokenizerState(Tokenizer.State.SELF_CLOSING_START_TAG);
                return NOTHING;
            } else {
                return defaultCase(ch);
            }
        } // break;
        case GREATER_THAN_SIGN: {
            if (isAppropriateEndTag()) {
                setTokenizerState(Tokenizer.State.DATA);
                final TagToken pendingToken = clearPendingTag();
                return one(pendingToken);
            } else {
                return defaultCase(ch);
            }
        } // break;
        case 'A': // fall through
        case 'B': // fall through
        case 'C': // fall through
        case 'D': // fall through
        case 'E': // fall through
        case 'F': // fall through
        case 'G': // fall through
        case 'H': // fall through
        case 'I': // fall through
        case 'J': // fall through
        case 'K': // fall through
        case 'L': // fall through
        case 'M': // fall through
        case 'N': // fall through
        case 'O': // fall through
        case 'P': // fall through
        case 'Q': // fall through
        case 'R': // fall through
        case 'S': // fall through
        case 'T': // fall through
        case 'U': // fall through
        case 'V': // fall through
        case 'W': // fall through
        case 'X': // fall through
        case 'Y': // fall through
        case 'Z': {
            appendToTagName(toLower((char) ch));
            appendToTemporaryBuffer((char) ch);
            return NOTHING;
        } // break;
        case 'a': // fall through
        case 'b': // fall through
        case 'c': // fall through
        case 'd': // fall through
        case 'e': // fall through
        case 'f': // fall through
        case 'g': // fall through
        case 'h': // fall through
        case 'i': // fall through
        case 'j': // fall through
        case 'k': // fall through
        case 'l': // fall through
        case 'm': // fall through
        case 'n': // fall through
        case 'o': // fall through
        case 'p': // fall through
        case 'q': // fall through
        case 'r': // fall through
        case 's': // fall through
        case 't': // fall through
        case 'u': // fall through
        case 'v': // fall through
        case 'w': // fall through
        case 'x': // fall through
        case 'y': // fall through
        case 'z': {
            appendToTagName((char) ch);
            appendToTemporaryBuffer((char) ch);
            return NOTHING;
        } // break;
        default: {
            return defaultCase(ch);
        } // break;
        }
    }
    
    private List<Token> defaultCase(final int ch) throws IOException {
        unconsume(ch);
        setTokenizerState(Tokenizer.State.RCDATA);
        final String content = "</" + clearTemporaryBuffer();
        return CharacterToken.toTokens(content);
    }
    
}
