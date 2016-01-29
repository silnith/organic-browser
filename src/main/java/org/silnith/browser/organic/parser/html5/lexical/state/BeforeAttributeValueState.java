package org.silnith.browser.organic.parser.html5.lexical.state;

import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.AMPERSAND;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.APOSTROPHE;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.CHARACTER_TABULATION;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.EQUALS_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.FORM_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GRAVE_ACCENT;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.GREATER_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LESS_THAN_SIGN;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.LINE_FEED;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.NULL;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.QUOTATION_MARK;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.REPLACEMENT_CHARACTER;
import static org.silnith.browser.organic.parser.util.UnicodeCodePoints.SPACE;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.ParseErrorException;
import org.silnith.browser.organic.parser.html5.lexical.Tokenizer;
import org.silnith.browser.organic.parser.html5.lexical.token.TagToken;
import org.silnith.browser.organic.parser.html5.lexical.token.Token;

/**
 * @see <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/tokenization.html#before-attribute-value-state">12.2.4.37 Before attribute value state</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BeforeAttributeValueState extends TokenizerState {

	public BeforeAttributeValueState(final Tokenizer tokenizer) {
		super(tokenizer);
	}

	@Override
	public int getMaxPushback() {
		return 1;
	}

	@Override
	public List<Token> getNextTokens() throws IOException {
		final int ch = consume();
		switch (ch) {
		case CHARACTER_TABULATION: // fall through
		case LINE_FEED: // fall through
		case FORM_FEED: // fall through
		case SPACE: {
			// ignore the character
			return NOTHING;
		} // break;
		case QUOTATION_MARK: {
			setTokenizerState(Tokenizer.State.ATTRIBUTE_VALUE_DOUBLE_QUOTED);
			return NOTHING;
		} // break;
		case AMPERSAND: {
			unconsume(ch);
			setTokenizerState(Tokenizer.State.ATTRIBUTE_VALUE_UNQUOTED);
			return NOTHING;
		} // break;
		case APOSTROPHE: {
			setTokenizerState(Tokenizer.State.ATTRIBUTE_VALUE_SINGLE_QUOTED);
			return NOTHING;
		} // break;
		case NULL: {
			if (isAllowParseErrors()) {
				appendToAttributeValue(REPLACEMENT_CHARACTER);
				setTokenizerState(Tokenizer.State.ATTRIBUTE_VALUE_UNQUOTED);
				return NOTHING;
			} else {
				throw new ParseErrorException("Null character before attribute value.");
			}
		} // break;
		case GREATER_THAN_SIGN: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				final TagToken pendingToken = clearPendingTag();
				return one(pendingToken);
			} else {
				throw new ParseErrorException("Illegal character before attribute value: >");
			}
		} // break;
		case LESS_THAN_SIGN: // fall through
		case EQUALS_SIGN: // fall through
		case GRAVE_ACCENT: {
			if (isAllowParseErrors()) {
				return defaultCase(ch);
			} else {
				throw new ParseErrorException("Illegal character before attribute value: " + (char) ch);
			}
		} // break;
		case EOF: {
			if (isAllowParseErrors()) {
				setTokenizerState(Tokenizer.State.DATA);
				return NOTHING;
			} else {
				throw new ParseErrorException("Unexpected end-of-file before attribute value.");
			}
		} // break;
		default: {
			return defaultCase(ch);
		} // break;
		}
	}

	private List<Token> defaultCase(final int ch) {
		appendToAttributeValue((char) ch);
		setTokenizerState(Tokenizer.State.ATTRIBUTE_VALUE_UNQUOTED);
		return NOTHING;
	}

}
