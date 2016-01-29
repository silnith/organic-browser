package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#self-closing-start-tag-state">12.2.4.43 Self-closing start tag state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SelfClosingStartTagState extends TokenizerState {

	public SelfClosingStartTagState(final Tokenizer tokenizer) {
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
		case GREATER_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.DATA);
			final TagToken pendingToken = clearPendingTag();
			pendingToken.setSelfClosing();
			return one(pendingToken);
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				return NOTHING;
			} else {
				throw new ParseErrorException();
			}
		} // break;
		default: {
			if (isAllowParseErrors()) {
				unconsume(ch);
				setTokenizerState(Tokenizer.State.BEFORE_ATTRIBUTE_NAME);
				return NOTHING;
			} else {
				throw new ParseErrorException();
			}
		} // break;
		}
	}

}
