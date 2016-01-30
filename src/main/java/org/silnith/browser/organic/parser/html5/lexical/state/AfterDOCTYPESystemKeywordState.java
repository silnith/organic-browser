package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.APOSTROPHE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.QUOTATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.DOCTYPEToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#after-doctype-system-keyword-state">
 *      12.2.4.62 After DOCTYPE system keyword state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterDOCTYPESystemKeywordState extends TokenizerState {
    
    public AfterDOCTYPESystemKeywordState(final Tokenizer tokenizer) {
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
            setTokenizerState(Tokenizer.State.BEFORE_DOCTYPE_SYSTEM_IDENTIFIER);
            return NOTHING;
        } // break;
        case QUOTATION_MARK: {
            if (isAllowParseErrors()) {
                createSystemIdentifier();
                setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected quotation mark after DOCTYPE system keyword.");
            }
        } // break;
        case APOSTROPHE: {
            if (isAllowParseErrors()) {
                createSystemIdentifier();
                setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected apostrophe after DOCTYPE system keyword.");
            }
        } // break;
        case GREATER_THAN_SIGN: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                setForceQuirks();
                final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
                return one(doctypeToken);
            } else {
                throw new ParseErrorException("Unexpected '>' after DOCTYPE system keyword.");
            }
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                setForceQuirks();
                final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
                return one(doctypeToken);
            } else {
                throw new ParseErrorException("Unexpected end-of-file after DOCTYPE system keyword.");
            }
        } // break;
        default: {
            if (isAllowParseErrors()) {
                setForceQuirks();
                setTokenizerState(Tokenizer.State.BOGUS_DOCTYPE);
                return NOTHING;
            } else {
                throw new ParseErrorException("Unexpected character after DOCTYPE system keyword: " + (char) ch);
            }
        } // break;
        }
    }
    
}
