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
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#between-doctype-public-and-system-identifiers-state">12.2.4.61 Between DOCTYPE public and system identifiers state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BetweenDOCTYPEPublicAndSystemIdentifiersState extends
		TokenizerState {

	public BetweenDOCTYPEPublicAndSystemIdentifiersState(final Tokenizer tokenizer) {
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
			// ignore character
			return NOTHING;
		} // break;
		case GREATER_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.DATA);
			final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
			return one(doctypeToken);
		} // break;
		case QUOTATION_MARK: {
			createSystemIdentifier();
			setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED);
			return NOTHING;
		} // break;
		case APOSTROPHE: {
			createSystemIdentifier();
			setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED);
			return NOTHING;
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				setForceQuirks();
				final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
				return one(doctypeToken);
			} else {
				throw new ParseErrorException("Unexpected end-of-file between DOCTYPE public and system identifiers.");
			}
		} // break;
		default: {
			if (isAllowParseErrors()) {
				setForceQuirks();
				setTokenizerState(Tokenizer.State.BOGUS_DOCTYPE);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected character between DOCTYPE public and system identifiers: " + (char) ch);
			}
		} // break;
		}
	}

}
