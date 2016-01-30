package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
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
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#tag-name-state">
 *      12.2.4.10 Tag name state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class TagNameState extends TokenizerState {
    
    public TagNameState(final Tokenizer tokenizer) {
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
        case NULL: {
            if (isAllowParseErrors()) {
                appendToTagName(REPLACEMENT_CHARACTER);
                return NOTHING;
            } else {
                throw new ParseErrorException("Null character in tag name.");
            }
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected end-of-file while parsing tag name.");
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
            return NOTHING;
        } // break;
        default: {
            appendToTagName((char) ch);
            return NOTHING;
        } // break;
        }
    }
    
}
