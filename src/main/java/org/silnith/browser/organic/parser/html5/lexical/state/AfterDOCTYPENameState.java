package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
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
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#after-doctype-name-state">
 *      12.2.4.55 After DOCTYPE name state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterDOCTYPENameState extends TokenizerState {
    
    public AfterDOCTYPENameState(final Tokenizer tokenizer) {
        super(tokenizer);
    }
    
    @Override
    public int getMaxPushback() {
        return 6;
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final int ch = consume();
        switch (ch) {
        case CHARACTER_TABULATION: // fall through
        case LINE_FEED: // fall through
        case FORM_FEED: // fall through
        case SPACE: {
            // ignore character
            return NOTHING;
        } // break;
        case GREATER_THAN_SIGN: {
            setTokenizerState(Tokenizer.State.DATA);
            final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
            return one(doctypeToken);
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                setForceQuirks();
                final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
                return one(doctypeToken);
            } else {
                throw new ParseErrorException("Unexpected end-of-file after DOCTYPE name.");
            }
        } // break;
        default: {
            final char[] buf = new char[6];
            buf[0] = (char) ch;
            final int numRead = consume(buf, 1, 5);
            
            if (numRead == 5 && (buf[0] == 'p' || buf[0] == 'P') && (buf[1] == 'u' || buf[1] == 'U')
                    && (buf[2] == 'b' || buf[2] == 'B') && (buf[3] == 'l' || buf[3] == 'L')
                    && (buf[4] == 'i' || buf[4] == 'I') && (buf[5] == 'c' || buf[5] == 'C')) {
                setTokenizerState(Tokenizer.State.AFTER_DOCTYPE_PUBLIC_KEYWORD);
                
                return NOTHING;
            }
            
            if (numRead == 5 && (buf[0] == 's' || buf[0] == 'S') && (buf[1] == 'y' || buf[1] == 'Y')
                    && (buf[2] == 's' || buf[2] == 'S') && (buf[3] == 't' || buf[3] == 'T')
                    && (buf[4] == 'e' || buf[4] == 'E') && (buf[5] == 'm' || buf[5] == 'M')) {
                setTokenizerState(Tokenizer.State.AFTER_DOCTYPE_SYSTEM_KEYWORD);
                
                return NOTHING;
            }
            
            if (isAllowParseErrors()) {
                // i = numRead - 1 + 1 due to already-consumed ch
                for (int i = numRead; i >= 0; i-- ) {
                    unconsume(buf[i]);
                }
                setForceQuirks();
                setTokenizerState(Tokenizer.State.BOGUS_DOCTYPE);
                return NOTHING;
            } else {
                throw new ParseErrorException(
                        "Unknown keyword after DOCTYPE name: " + String.valueOf(buf, 0, numRead + 1));
            }
        } // break;
        }
    }
    
}
