package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CommentToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#bogus-comment-state">12.2.4.44 Bogus comment state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BogusCommentState extends TokenizerState {

	public BogusCommentState(final Tokenizer tokenizer) {
		super(tokenizer);
	}

	@Override
	public int getMaxPushback() {
		return 0;
	}

	@Override
	public List<Token> getNextTokens() throws IOException {
		final StringBuilder content = new StringBuilder();
		int ch = consume();
		while (ch != EOF && ch != GREATER_THAN_SIGN) {
			if (ch == NULL) {
				content.append(REPLACEMENT_CHARACTER);
			} else {
				content.append(ch);
			}
			ch = consume();
		}
		setTokenizerState(Tokenizer.State.DATA);
		return one(new CommentToken(content.toString()));
	}

}
