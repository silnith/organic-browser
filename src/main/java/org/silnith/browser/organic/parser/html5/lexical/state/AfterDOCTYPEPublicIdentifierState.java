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
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#after-doctype-public-identifier-state">12.2.4.60 After DOCTYPE public identifier state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class AfterDOCTYPEPublicIdentifierState extends TokenizerState {

	public AfterDOCTYPEPublicIdentifierState(final Tokenizer tokenizer) {
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
			setTokenizerState(Tokenizer.State.BETWEEN_DOCTYPE_PUBLIC_AND_SYSTEM_IDENTIFIERS);
			return NOTHING;
		} // break;
		case GREATER_THAN_SIGN: {
			setTokenizerState(Tokenizer.State.DATA);
			final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
			return one(doctypeToken);
		} // break;
		case QUOTATION_MARK: {
			if (isAllowParseErrors()) {
				createSystemIdentifier();
				setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_DOUBLE_QUOTED);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected quotation mark after DOCTYPE public identifier.");
			}
		} // break;
		case APOSTROPHE: {
			if (isAllowParseErrors()) {
				createSystemIdentifier();
				setTokenizerState(Tokenizer.State.DOCTYPE_SYSTEM_IDENTIFIER_SINGLE_QUOTED);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected apostrophe after DOCTYPE public identifier.");
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				setForceQuirks();
				final DOCTYPEToken doctypeToken = clearDOCTYPEToken();
				return one(doctypeToken);
			} else {
				throw new ParseErrorException("Unexpected end-of-file after DOCTYPE public identifier.");
			}
		} // break;
		default: {
			if (isAllowParseErrors()) {
				setForceQuirks();
				setTokenizerState(Tokenizer.State.BOGUS_DOCTYPE);
				return NOTHING;
			} else {
				throw new ParseErrorException();
			}
		} // break;
		}
	}

}
