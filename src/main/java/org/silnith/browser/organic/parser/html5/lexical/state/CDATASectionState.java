package org.silnith.browser.organic.parser.html5.lexical.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.CharacterToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#cdata-section-state">12.2.4.68 CDATA section state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CDATASectionState extends TokenizerState {

	public CDATASectionState(final Tokenizer tokenizer) {
		super(tokenizer);
	}

	@Override
	public int getMaxPushback() {
		return 0;
	}

	@Override
	public List<Token> getNextTokens() throws IOException {
		setTokenizerState(Tokenizer.State.DATA);
		final StringBuilder stringBuilder = new StringBuilder();
		int ch = consume();
		while (ch != -1) {
			stringBuilder.append((char) ch);
			final int length = stringBuilder.length();
			if (length >= 3) {
				final int realLength = length - 3;
				final String suffix = stringBuilder.substring(realLength);
				if (suffix.equals("]]>")) {
					stringBuilder.setLength(realLength);
					break;
				}
			}
			ch = consume();
		}
		final List<Token> tokens = new ArrayList<>();
		for (final char character : stringBuilder.toString().toCharArray()) {
			tokens.add(new CharacterToken(character));
		}
		return tokens;
	}

}
