package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.HYPHEN_MINUS;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#script-data-escaped-state">12.2.4.22 Script data escaped state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ScriptDataEscapedState extends TokenizerState {

	public ScriptDataEscapedState(final Tokenizer tokenizer) {
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
			setTokenizerState(Tokenizer.State.SCRIPT_DATA_ESCAPED_DASH);
			return one(new CharacterToken(HYPHEN_MINUS));
		} // break;
		case LESS_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.SCRIPT_DATA_ESCAPED_LESS_THAN_SIGN);
			return NOTHING;
		} // break;
		case NULL: {
			if (isAllowParseErrors()) {
				return one(new CharacterToken(REPLACEMENT_CHARACTER));
			} else {
				throw new ParseErrorException("Null character in script data escaped state.");
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected end-of-file in script data escaped state.");
			}
		} // break;
		default: {
			return one(new CharacterToken((char) ch));
		} // break;
		}
	}

}
