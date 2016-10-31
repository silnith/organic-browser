package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LEFT_SQUARE_BRACKET;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;


/**
 * @see <a href=
 *      "http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#markup-declaration-open-state">
 *      12.2.4.45 Markup declaration open state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class MarkupDeclarationOpenState extends TokenizerState {
    
    public MarkupDeclarationOpenState(final Tokenizer tokenizer) {
        super(tokenizer);
    }
    
    @Override
    public int getMaxPushback() {
        return 7;
    }
    
    @Override
    public List<Token> getNextTokens() throws IOException {
        final char[] buf = new char[7];
        final int numRead = consume(buf, 7);
        
        if (numRead >= 2 && buf[0] == HYPHEN_MINUS && buf[1] == HYPHEN_MINUS) {
            for (int i = numRead - 1; i >= 2; i-- ) {
                unconsume(buf[i]);
            }
            createCommentToken();
            setTokenizerState(Tokenizer.State.COMMENT_START);
            return NOTHING;
        }
        
        if (numRead == 7 && (buf[0] == 'd' || buf[0] == 'D') && (buf[1] == 'o' || buf[1] == 'O')
                && (buf[2] == 'c' || buf[2] == 'C') && (buf[3] == 't' || buf[3] == 'T')
                && (buf[4] == 'y' || buf[4] == 'Y') && (buf[5] == 'p' || buf[5] == 'P')
                && (buf[6] == 'e' || buf[6] == 'E')) {
            setTokenizerState(Tokenizer.State.DOCTYPE);
            return NOTHING;
        }
        
        if (numRead == 7 && buf[0] == LEFT_SQUARE_BRACKET && buf[1] == 'C' && buf[2] == 'D' && buf[3] == 'A'
                && buf[4] == 'T' && buf[5] == 'A' && buf[6] == LEFT_SQUARE_BRACKET) {
            // TODO:
            // also need to check that there is an "adjusted current node"
            // and that said node is not an element in the HTML namespace
            setTokenizerState(Tokenizer.State.CDATA_SECTION);
            return NOTHING;
        }
        
        if (isAllowParseErrors()) {
            for (int i = numRead - 1; i >= 0; i-- ) {
                unconsume(buf[i]);
            }
            createCommentToken();
            setTokenizerState(Tokenizer.State.BOGUS_COMMENT);
            return NOTHING;
        } else {
            throw new ParseErrorException("Unknown markup declaration: " + String.valueOf(buf));
        }
    }
    
}
