package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SOLIDUS;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.EndTagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#rcdata-end-tag-open-state">
 *      12.2.4.12 RCDATA end tag open state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class RCDATAEndTagOpenState extends TokenizerState {
    
    public RCDATAEndTagOpenState(final Tokenizer tokenizer) {
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
            setPendingTag(new EndTagToken());
            appendToTagName(toLower((char) ch));
            appendToTemporaryBuffer((char) ch);
            setTokenizerState(Tokenizer.State.RCDATA_END_TAG_NAME);
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
            setPendingTag(new EndTagToken());
            appendToTagName((char) ch);
            appendToTemporaryBuffer((char) ch);
            setTokenizerState(Tokenizer.State.RCDATA_END_TAG_NAME);
            return NOTHING;
        } // break;
        default: {
            unconsume(ch);
            setTokenizerState(Tokenizer.State.RCDATA);
            return CharacterToken.toTokens(LESS_THAN_SIGN, SOLIDUS);
        } // break;
        }
    }
    
}
