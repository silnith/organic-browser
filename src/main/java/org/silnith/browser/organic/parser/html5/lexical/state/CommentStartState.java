package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#comment-start-state">
 *      12.2.4.46 Comment start state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CommentStartState extends TokenizerState {
    
    public CommentStartState(final Tokenizer tokenizer) {
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
        case HYPHEN_MINUS: {
            setTokenizerState(Tokenizer.State.COMMENT_START_DASH);
            return NOTHING;
        } // break;
        case NULL: {
            if (isAllowParseErrors()) {
                appendToCommentToken(REPLACEMENT_CHARACTER);
                setTokenizerState(Tokenizer.State.COMMENT);
                return NOTHING;
            } else {
                throw new ParseErrorException("Null character in comment start.");
            }
        } // break;
        case GREATER_THAN_SIGN: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                final CommentToken commentToken = clearCommentToken();
                return one(commentToken);
            } else {
                throw new ParseErrorException("Unexpected '>' in comment start.");
            }
        } // break;
        case EOF: {
            if (isAllowParseErrors()) {
                setTokenizerState(Tokenizer.State.DATA);
                final CommentToken commentToken = clearCommentToken();
                return one(commentToken);
            } else {
                throw new ParseErrorException("Unexpected end-of-file in comment start.");
            }
        } // break;
        default: {
            appendToCommentToken((char) ch);
            setTokenizerState(Tokenizer.State.COMMENT);
            return NOTHING;
        } // break;
        }
    }
    
}
